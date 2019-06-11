package org.panta.misskeynest.constant

internal fun getAppSecretKey(): String{
    return ApplicationConstant.getAppSecretKey()
}

/*internal fun getMiChanWorkAppSecretKey(): String{
    return ApplicationConstant.getMiChanWorkSecretKey()
}*/


data class DomainAndAppSecret(internal val domain: String, internal val appSecret: String, internal val instanceName: String)
fun getInstanceInfoList(): List<DomainAndAppSecret>{
    return listOf(DomainAndAppSecret("https://misskey.io", getAppSecretKey(), instanceName = "misskey.io")//,//v11
        //DomainAndAppSecret("https://mi-chan.work", getMiChanWorkAppSecretKey(), instanceName = "みーすきー / Mi-sskey")//, /*v11*/
        /*DomainAndAppSecret("https://misskey.m544.net",ApplicationConstant.getM544AppSecretKey() , instanceName = "めいすきー"), /*v10*/
        DomainAndAppSecret("https://twista.283.cloud",ApplicationConstant.getTwistaAppSecretKey() ,instanceName = "twista"), /*v10*/
        DomainAndAppSecret("https://xn--6r8h.tk/", ApplicationConstant.getTkAppSecretKey(),instanceName = "\uD83D\uDC9B.tk"), /*v10*/
        DomainAndAppSecret("https://misskey.dev", ApplicationConstant.getDevMisskeyAppSecretKey() , instanceName = "misskey.dev") /*v10*/*/

    )
}

