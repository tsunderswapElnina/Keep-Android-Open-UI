package org.keepandroidopen.app.data

/**
 * Non-localized contact data. Human-readable text lives in string resources so it
 * can be translated; URLs and e-mail addresses are language-independent.
 */
data class ContactSeed(
    val url: String? = null,
    val email: String? = null,
)

object Content {

    val usContactSeeds = listOf(
        ContactSeed(url = "https://www.justice.gov/atr/webform/submit-your-antitrust-report-online"),
        ContactSeed(url = "https://www.ftc.gov/advice-guidance/competition-guidance/antitrust-complaint-intake"),
        ContactSeed(url = "https://www.house.gov/representatives/find-your-representative"),
        ContactSeed(url = "https://www.senate.gov/senators/senators-contact.htm"),
    )

    val worldContactSeeds = listOf(
        ContactSeed(email = "COMP-GREFFE-ANTITRUST@ec.europa.eu", url = "https://competition-policy.ec.europa.eu/antitrust-and-cartels/contact_en"),
        ContactSeed(url = "https://digital-markets-act.ec.europa.eu/contact-us-eu-citizen_en"),
        ContactSeed(email = "general.enquiries@cma.gov.uk", url = "https://contact-the-cma.service.gov.uk/wizard/classify"),
        ContactSeed(email = "superintendencia@cade.gov.br", url = "https://www.gov.br/cade/pt-br"),
        ContactSeed(email = "cccs_feedback@cccs.gov.sg", url = "https://www.cccs.gov.sg"),
        ContactSeed(email = "saraban@tcct.or.th", url = "https://www.tcct.or.th"),
        ContactSeed(email = "infokom@kppu.go.id", url = "https://eng.kppu.go.id"),
        ContactSeed(email = "international@accc.gov.au", url = "https://www.accc.gov.au/about-us/contact-us-or-report-an-issue"),
        ContactSeed(url = "https://competition-bureau.canada.ca/en/contact-competition-bureau-canada/complaint-form"),
        ContactSeed(email = "atdregistry@cci.gov.in", url = "https://www.cci.gov.in"),
        ContactSeed(email = "intnldiv@jftc.go.jp", url = "https://www.jftc.go.jp"),
        ContactSeed(email = "kftc@korea.kr", url = "https://www.ftc.go.kr"),
        ContactSeed(email = "denuncias@antimonopolio.gob.mx", url = "https://www.antimonopolio.gob.mx"),
        ContactSeed(email = "contact@comcom.govt.nz", url = "https://www.comcom.govt.nz/report-a-concern/"),
        ContactSeed(email = "ftc@ftc.gov.tw", url = "https://www.ftc.gov.tw"),
        ContactSeed(url = "https://www.weko.admin.ch/en/information-about-unlawful-practices"),
    )

    val advocacySeeds = listOf(
        ContactSeed(url = "https://www.eff.org"),
        ContactSeed(url = "https://www.fsf.org"),
        ContactSeed(url = "https://fsfe.org"),
        ContactSeed(url = "https://edri.org"),
        ContactSeed(url = "https://sfconservancy.org"),
    )
}

/** A contact resolved against string resources for display. */
data class Contact(
    val region: String,
    val name: String,
    val detail: String,
    val url: String? = null,
    val email: String? = null,
)
