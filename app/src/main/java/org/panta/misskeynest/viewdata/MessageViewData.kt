package org.panta.misskeynest.viewdata

import org.panta.misskeynest.entity.MessageProperty
import org.panta.misskeynest.interfaces.ID

data class MessageViewData(override val id: String, override val isIgnore: Boolean, private val message: MessageProperty) : ID