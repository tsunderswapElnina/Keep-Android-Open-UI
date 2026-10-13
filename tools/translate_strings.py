#!/usr/bin/env python3
"""Translate app/.../values/strings.xml into every SupportedLocales language.

Source of truth:
  app/src/main/res/values/strings.xml (English)
Output:
  app/src/main/res/values-<qualifier>/strings.xml for each locale

Uses the Mozhi Google proxy. Results are cached in a JSON file so runs are
resumable and re-runs are cheap. Placeholders (%1$s) and URLs are validated;
if a translation drops one, that single string falls back to English.
"""

import concurrent.futures as futures
import html
import json
import os
import re
import sys
import time
import urllib.parse
import urllib.request
import xml.etree.ElementTree as ET

HERE = os.path.dirname(os.path.abspath(__file__))
RES = os.path.join(HERE, "..", "app", "src", "main", "res")
SOURCE = os.path.join(RES, "values", "strings.xml")
CACHE_PATH = os.environ.get(
    "KAO_TRANSLATE_CACHE",
    "/data/data/com.termux/files/usr/tmp/opencode/kao-translate-cache.json",
)
# Direct Google endpoint that accepts several `q` params per request and returns
# a JSON array of translations (fast, no separator hacks).
ENDPOINT = "https://clients5.google.com/translate_a/t"
BATCH_CHARS = 3000
WORKERS = 8
PLACEHOLDER_RE = re.compile(r"%\d+\$[a-zA-Z]")
URL_RE = re.compile(r"https?://[^\s\"'<>)]+")

# Skip English variants (they fall back to the default English resources).
SKIP_PREFIXES = ("en",)

# Android/Google language code differences.
GOOGLE_OVERRIDES = {
    "es-MX": "es", "es-US": "es", "es-419": "es",
    "pt-BR": "pt", "pt-PT": "pt-PT",
    "fr-CA": "fr-CA", "fr-CH": "fr",
    "de-AT": "de", "de-CH": "de",
    "zh-CN": "zh-CN", "zh-HK": "zh-TW", "zh-TW": "zh-TW",
    "he": "iw", "fil": "tl", "tl": "tl", "nb": "nb",
}


def google_code(tag):
    if tag in GOOGLE_OVERRIDES:
        return GOOGLE_OVERRIDES[tag]
    return tag.split("-")[0]


def qualifier(tag):
    if "-" in tag:
        return "values-b+" + "+".join(tag.split("-"))
    return "values-" + tag


def unescape_android(s):
    out = []
    i = 0
    while i < len(s):
        c = s[i]
        if c == "\\" and i + 1 < len(s):
            n = s[i + 1]
            if n == "n":
                out.append("\n"); i += 2; continue
            if n == "t":
                out.append("\t"); i += 2; continue
            if n in ("'", '"', "\\", "@", "?"):
                out.append(n); i += 2; continue
        out.append(c); i += 1
    return "".join(out)


def escape_android(s):
    s = s.replace("\\", "\\\\")
    s = s.replace("'", "\\'")
    s = s.replace('"', '\\"')
    s = s.replace("\r", "")
    s = s.replace("\n", "\\n")
    s = s.replace("&", "&amp;")
    s = s.replace("<", "&lt;")
    s = s.replace(">", "&gt;")
    if s[:1] in ("@", "?"):
        s = "\\" + s
    return s


def read_source(path):
    tree = ET.parse(path)
    root = tree.getroot()
    entries = []
    for el in root:
        if el.tag == "string":
            if el.attrib.get("translatable") == "false":
                continue
            name = el.attrib["name"]
            text = el.text or ""
            entries.append({"kind": "string", "name": name, "items": [text]})
        elif el.tag == "string-array":
            name = el.attrib["name"]
            items = [it.text or "" for it in el.findall("item")]
            entries.append({"kind": "array", "name": name, "items": items})
    return entries


def required_tokens(text):
    return PLACEHOLDER_RE.findall(text) + URL_RE.findall(text)


def valid(src, out):
    for tok in required_tokens(src):
        if tok not in out:
            return False
    return True


def http_translate(texts, code, timeout=45):
    parts = [("client", "dict-chrome-ex"), ("sl", "en"), ("tl", code)]
    for t in texts:
        parts.append(("q", t))
    url = ENDPOINT + "?" + urllib.parse.urlencode(parts)
    req = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0"})
    with urllib.request.urlopen(req, timeout=timeout) as r:
        data = json.loads(r.read().decode("utf-8"))
    if isinstance(data, str):
        data = [data]
    out = []
    for item in data:
        if isinstance(item, list):
            item = "".join(str(x) for x in item)
        out.append(html.unescape(str(item)))
    return out


def translate_one(text, code, attempts=3):
    for a in range(attempts):
        try:
            out = http_translate([text], code)
            if out and out[0].strip() and valid(text, out[0].strip()):
                return out[0].strip()
        except Exception:
            time.sleep(0.6 + a)
    return None


class Translator:
    def __init__(self):
        self.cache = {}
        if os.path.exists(CACHE_PATH):
            with open(CACHE_PATH, "r", encoding="utf-8") as f:
                self.cache = json.load(f)

    def save(self):
        tmp = CACHE_PATH + ".tmp"
        with open(tmp, "w", encoding="utf-8") as f:
            json.dump(self.cache, f, ensure_ascii=False)
        os.replace(tmp, CACHE_PATH)

    def translate_batch(self, keys, texts, code):
        """keys -> unique cache keys; texts -> source strings (newline-real)."""
        out = {}
        for attempt in range(4):
            try:
                parts = http_translate(texts, code)
                if len(parts) != len(texts):
                    # occasionally Google splits/merges; retry once at same size
                    if attempt < 3:
                        time.sleep(1.0)
                        continue
                    break
                for key, src, t in zip(keys, texts, parts):
                    t = t.strip()
                    if t and valid(src, t):
                        out[key] = t
                return out
            except Exception:
                time.sleep(0.8 * (attempt + 1))
        return out


def main():
    entries = read_source(SOURCE)
    tags = [t.strip() for t in os.environ.get("KAO_LOCALES", "").split(",") if t.strip()]
    if not tags:
        sys.stderr.write("Set KAO_LOCALES to a comma-separated list of tags\n")
        return 1

    tr = Translator()

    for tag in tags:
        if tag.startswith(SKIP_PREFIXES):
            print(f"skip {tag}")
            continue
        code = google_code(tag)
        qual = qualifier(tag)
        print(f"== {tag} ({code}) -> {qual}")

        # 1. figure out what still needs translating
        # unit = (cache_key, source_text)
        units = {}
        for e in entries:
            for i, item in enumerate(e["items"]):
                src = unescape_android(item)
                if not src.strip():
                    continue
                key = f"{tag}\t{e['name']}\t{i}"
                units[key] = src

        missing = [k for k, v in units.items() if k not in tr.cache]
        # 2. batch the missing units by total character budget
        batches = []
        cur_keys, cur_texts, cur_len = [], [], 0
        for k in missing:
            t = units[k]
            if cur_keys and cur_len + len(t) > BATCH_CHARS:
                batches.append((cur_keys, cur_texts))
                cur_keys, cur_texts, cur_len = [], [], 0
            cur_keys.append(k)
            cur_texts.append(t)
            cur_len += len(t) + 4
        if cur_keys:
            batches.append((cur_keys, cur_texts))

        if batches:
            with futures.ThreadPoolExecutor(max_workers=WORKERS) as ex:
                futs = [ex.submit(tr.translate_batch, ks, ts, code) for ks, ts in batches]
                done = 0
                for f in futures.as_completed(futs):
                    res = f.result()
                    for k, v in res.items():
                        tr.cache[k] = v
                    done += 1
                    if done % 10 == 0:
                        tr.save()
            tr.save()
            # individual retry for anything still missing
            still = [k for k in missing if k not in tr.cache]
            if still:
                print(f"   retrying {len(still)} strings individually")
                with futures.ThreadPoolExecutor(max_workers=WORKERS) as ex:
                    futs = {ex.submit(translate_one, units[k], code): k for k in still}
                    for f in futures.as_completed(futs):
                        k = futs[f]
                        v = f.result()
                        if v:
                            tr.cache[k] = v
                tr.save()

        # 3. write the locale file
        lines = ['<?xml version="1.0" encoding="utf-8"?>', "<resources>"]
        missing_count = 0
        for e in entries:
            if e["kind"] == "string":
                src = unescape_android(e["items"][0])
                key = f"{tag}\t{e['name']}\t0"
                text = tr.cache.get(key, e["items"][0])
                if key not in tr.cache:
                    missing_count += 1
                has_args = bool(PLACEHOLDER_RE.search(src))
                attr = "" if has_args else ' formatted="false"'
                lines.append(f'    <string name="{e["name"]}"{attr}>{escape_android(text)}</string>')
            else:
                lines.append(f'    <string-array name="{e["name"]}">')
                for i, item in enumerate(e["items"]):
                    key = f"{tag}\t{e['name']}\t{i}"
                    text = tr.cache.get(key, item)
                    if key not in tr.cache and unescape_android(item).strip():
                        missing_count += 1
                    lines.append(f"        <item>{escape_android(text)}</item>")
                lines.append("    </string-array>")
        lines.append("</resources>")
        lines.append("")

        out_dir = os.path.join(RES, qual)
        os.makedirs(out_dir, exist_ok=True)
        with open(os.path.join(out_dir, "strings.xml"), "w", encoding="utf-8") as f:
            f.write("\n".join(lines))
        print(f"   wrote {qual}/strings.xml ({missing_count} untranslated fallbacks)")

    return 0


if __name__ == "__main__":
    raise SystemExit(main())
