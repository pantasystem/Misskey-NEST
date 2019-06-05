package org.panta.misskey_nest.entity

import java.io.Serializable

data class ConnectionProperty(val domain: String, val i: String, val userPrimaryId: String): Serializable