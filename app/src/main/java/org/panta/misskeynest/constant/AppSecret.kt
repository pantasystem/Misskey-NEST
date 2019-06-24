package org.panta.misskeynest.constant

internal fun getAppSecretKey(): String{
    return ApplicationSecretConstant.getAppSecretKey()
}


data class DomainAndAppSecret(internal val domain: String, internal val appSecret: String, internal val instanceName: String)
fun getInstanceInfoList(): List<DomainAndAppSecret>{
    return listOf(DomainAndAppSecret("https://misskey.io", getAppSecretKey(), instanceName = "misskey.io")

    )
}

