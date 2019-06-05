package org.panta.misskey_nest.entity

import java.io.Serializable

data class PersonAuthKey(val i: String)
data class LocalAppTimelineSetting(var excludeNsfw: Boolean? = null, val includeMyReNotes: Boolean = true ,val includeReNotedNotes: Boolean = true, val includeLocalReNotes: Boolean = true): Serializable