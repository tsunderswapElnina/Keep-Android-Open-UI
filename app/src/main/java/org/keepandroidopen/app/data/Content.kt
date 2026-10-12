package org.keepandroidopen.app.data

data class TimelineEvent(
    val date: String,
    val title: String,
    val detail: String,
)

data class VerificationPath(
    val name: String,
    val identityRequired: String,
    val distribution: String,
    val userExperience: String,
    val bestFor: String,
)

data class Contact(
    val region: String,
    val name: String,
    val detail: String,
    val url: String? = null,
    val email: String? = null,
)

object Content {

    const val ROLLOUT_SUMMARY =
        "Android Developer Verification is a Google program that links every app to a verified " +
            "developer identity before it can be installed on certified Android devices. " +
            "Developers must verify who they are and register the package names they publish. " +
            "Apps without a registered, verified developer can no longer be installed the normal way " +
            "once the protections roll out."

    const val WHAT_IT_MEANS =
        "It lands hardest on independent and open-source distribution such as F-Droid, direct APK " +
            "downloads, and hobby projects. Google says it is about safety and stopping anonymous " +
            "malware. Critics including the Keep Android Open campaign, F-Droid and the EFF say it is " +
            "a gatekeeper kill switch: central registration, government ID, a fee, and full control " +
            "over what your device is allowed to trust."

    val timeline: List<TimelineEvent> = listOf(
        TimelineEvent(
            date = "August 25, 2025",
            title = "The program is announced",
            detail = "Google announces that beginning in 2027, apps for Android must be registered " +
                "to a verified developer, including apps installed outside Google Play.",
        ),
        TimelineEvent(
            date = "March 19, 2026",
            title = "The \u201cadvanced flow\u201d is revealed",
            detail = "Power users get a deliberately cumbersome escape hatch: enable Developer " +
                "Options, confirm you are not being coerced, restart, then wait 24 hours before you " +
                "may allow unverified installs.",
        ),
        TimelineEvent(
            date = "June 2026",
            title = "Android Developer Verifier ships to devices",
            detail = "A background system service (com.google.android.verifier) is pushed through " +
                "Google System Updates to certified devices and stays dormant until enforcement.",
        ),
        TimelineEvent(
            date = "June 18, 2026",
            title = "Initial launch confirmed",
            detail = "Google confirms enforcement starts September 30, 2026, across seven stores and " +
                "four countries.",
        ),
        TimelineEvent(
            date = "July 2026",
            title = "Developer APIs open up",
            detail = "The Android Developer ID Status API launches globally and early access begins " +
                "for the Android Developer Console API and limited distribution accounts.",
        ),
        TimelineEvent(
            date = "August 2026",
            title = "Global console, limited accounts, advanced flow",
            detail = "Limited distribution accounts (free, up to 20 devices, no government ID) and " +
                "the Console API launch worldwide. The advanced flow for unverified sideloading " +
                "begins rolling out.",
        ),
        TimelineEvent(
            date = "September 30, 2026",
            title = "Enforcement begins in four countries",
            detail = "Protections go live for users in Brazil, Indonesia, Singapore and Thailand on " +
                "certified devices running Android 7+. Apps from unverified developers are blocked " +
                "on the participating stores: Google Play, HONOR App Market, OPPO App Market, " +
                "Galaxy Store, Palm Store, V-Appstore and GetApps.",
        ),
        TimelineEvent(
            date = "2027",
            title = "Global rollout",
            detail = "Verification is expanded to all apps and regions. Keep Android Open counts " +
                "down to January 1, 2027, when Android becomes a locked-down platform.",
        ),
    )

    val paths: List<VerificationPath> = listOf(
        VerificationPath(
            name = "Full distribution",
            identityRequired = "Yes \u2014 government ID and a fee",
            distribution = "Any store or channel you choose",
            userExperience = "Nothing changes for your users",
            bestFor = "Organizations and professional developers",
        ),
        VerificationPath(
            name = "Limited distribution",
            identityRequired = "No \u2014 no ID or registration fee",
            distribution = "Up to 20 devices via QR code or link handshake",
            userExperience = "Users accept an install invite from a developer they know",
            bestFor = "Students, hobbyists and personal use",
        ),
        VerificationPath(
            name = "Sideloading unregistered apps",
            identityRequired = "No \u2014 but high friction",
            distribution = "ADB (exempt) or the advanced flow",
            userExperience = "Developer Options, restart, 24-hour wait, then install anyway",
            bestFor = "Unverified developers and power users",
        ),
    )

    const val ADVANCED_FLOW_NOTE =
        "The advanced flow is delivered through Google Play services, not the Android OS, so " +
            "Google can change, restrict or remove it at any time without an OS update or user " +
            "consent. The 24-hour delay is intentionally disruptive and is one more step that stands " +
            "between you and software you choose to run."

    // ---- Act Now ----------------------------------------------------------

    val usContacts: List<Contact> = listOf(
        Contact(
            region = "United States",
            name = "US Department of Justice \u2014 Antitrust Division",
            detail = "Submit an antitrust report online.",
            url = "https://www.justice.gov/atr/webform/submit-your-antitrust-report-online",
        ),
        Contact(
            region = "United States",
            name = "US Federal Trade Commission \u2014 Antitrust",
            detail = "File an antitrust complaint.",
            url = "https://www.ftc.gov/advice-guidance/competition-guidance/antitrust-complaint-intake",
        ),
        Contact(
            region = "United States",
            name = "Find your House Representative",
            detail = "Contact your representative in the US House.",
            url = "https://www.house.gov/representatives/find-your-representative",
        ),
        Contact(
            region = "United States",
            name = "Contact your US Senators",
            detail = "Find and contact both of your state's senators.",
            url = "https://www.senate.gov/senators/senators-contact.htm",
        ),
    )

    val worldContacts: List<Contact> = listOf(
        Contact(
            region = "European Union",
            name = "EU Antitrust (DG COMP)",
            detail = "Email the European Commission competition authority.",
            email = "COMP-GREFFE-ANTITRUST@ec.europa.eu",
            url = "https://competition-policy.ec.europa.eu/antitrust-and-cartels/contact_en",
        ),
        Contact(
            region = "European Union",
            name = "Digital Markets Act team",
            detail = "Contact the EU DMA citizen helpdesk.",
            url = "https://digital-markets-act.ec.europa.eu/contact-us-eu-citizen_en",
        ),
        Contact(
            region = "United Kingdom",
            name = "Competition & Markets Authority",
            detail = "Report to the UK CMA.",
            email = "general.enquiries@cma.gov.uk",
            url = "https://contact-the-cma.service.gov.uk/wizard/classify",
        ),
        Contact(
            region = "Brazil",
            name = "CADE",
            detail = "Brazil is in the first enforcement wave.",
            email = "superintendencia@cade.gov.br",
            url = "https://www.gov.br/cade/pt-br",
        ),
        Contact(
            region = "Singapore",
            name = "CCCS",
            detail = "Singapore is in the first enforcement wave.",
            email = "cccs_feedback@cccs.gov.sg",
            url = "https://www.cccs.gov.sg",
        ),
        Contact(
            region = "Thailand",
            name = "Trade Competition Commission (OTCC)",
            detail = "Thailand is in the first enforcement wave.",
            email = "saraban@tcct.or.th",
            url = "https://www.tcct.or.th",
        ),
        Contact(
            region = "Indonesia",
            name = "KPPU",
            detail = "Indonesia is in the first enforcement wave.",
            email = "infokom@kppu.go.id",
            url = "https://eng.kppu.go.id",
        ),
        Contact(
            region = "Australia",
            name = "ACCC",
            detail = "Report to the Australian Competition and Consumer Commission.",
            email = "international@accc.gov.au",
            url = "https://www.accc.gov.au/about-us/contact-us-or-report-an-issue",
        ),
        Contact(
            region = "Canada",
            name = "Competition Bureau Canada",
            detail = "File a complaint with the Competition Bureau.",
            url = "https://competition-bureau.canada.ca/en/contact-competition-bureau-canada/complaint-form",
        ),
        Contact(
            region = "India",
            name = "Competition Commission of India",
            detail = "Contact the CCI about anti-competitive conduct.",
            email = "atdregistry@cci.gov.in",
            url = "https://www.cci.gov.in",
        ),
        Contact(
            region = "Japan",
            name = "Japan Fair Trade Commission",
            detail = "Contact the JFTC / Mobile Software Competition Act desk.",
            email = "intnldiv@jftc.go.jp",
            url = "https://www.jftc.go.jp",
        ),
        Contact(
            region = "South Korea",
            name = "Korea Fair Trade Commission",
            detail = "Contact the KFTC.",
            email = "kftc@korea.kr",
            url = "https://www.ftc.go.kr",
        ),
        Contact(
            region = "Mexico",
            name = "Comisi\u00f3n Nacional Antimonopolio",
            detail = "File an antitrust complaint in Mexico.",
            email = "denuncias@antimonopolio.gob.mx",
            url = "https://www.antimonopolio.gob.mx",
        ),
        Contact(
            region = "Aotearoa / New Zealand",
            name = "Commerce Commission",
            detail = "Report anti-competitive conduct.",
            email = "contact@comcom.govt.nz",
            url = "https://www.comcom.govt.nz/report-a-concern/",
        ),
        Contact(
            region = "Taiwan",
            name = "Fair Trade Commission",
            detail = "Contact the Taiwan FTC.",
            email = "ftc@ftc.gov.tw",
            url = "https://www.ftc.gov.tw",
        ),
        Contact(
            region = "Switzerland",
            name = "Competition Commission (WEKO)",
            detail = "Report unlawful practices.",
            url = "https://www.weko.admin.ch/en/information-about-unlawful-practices",
        ),
    )

    val advocacyGroups: List<Contact> = listOf(
        Contact("Global", "Electronic Frontier Foundation", "App gatekeeping and internet censorship.", url = "https://www.eff.org"),
        Contact("Global", "Free Software Foundation", "Software freedom and user control.", url = "https://www.fsf.org"),
        Contact("Europe", "Free Software Foundation Europe", "Software freedom in Europe.", url = "https://fsfe.org"),
        Contact("Europe", "European Digital Rights (EDRi)", "Digital rights advocacy.", url = "https://edri.org"),
        Contact("Global", "Software Freedom Conservancy", "Community-driven free software.", url = "https://sfconservancy.org"),
    )

    /** Generic letter that users can edit before sending to any regulator. */
    fun letterTemplate(region: String = "my government"): String = """
        Subject: Stop Google's Android Developer Verification lockdown

        Dear $region representative,

        I am writing as a concerned Android user and as a citizen in your jurisdiction.

        Google's Android Developer Verification program, enforced starting September 30, 2026 in
        several markets and expanding globally in 2027, will require every Android app to be
        registered to a centrally verified developer before it can be installed on certified
        devices. Developers must submit government identification, agree to Google's terms, pay a
        fee and list every current and future app identifier.

        This does not meaningfully improve security. It hands a single private company the power to
        decide which software people are allowed to run on the devices they own, and it threatens
        independent and open-source distribution such as F-Droid, which cannot comply and may cease
        to exist.

        I respectfully ask that you:

        1. Investigate whether this program is anti-competitive and constitutes an abuse of a
           gatekeeper position.
        2. Press Google to exempt open-source and non-commercial distribution, and to keep
           sideloading free of centralized registration.
        3. Protect the right of users to run software of their choosing on hardware they purchased.

        Please acknowledge this message in writing and let me know what action your office takes.

        I am also copying the Keep Android Open campaign at ${KeepAndroidOpen.VICTORY_EMAIL}.

        Sincerely,
        [Your name]
        [Your city and country]
    """.trimIndent()
}
