package org.panta.misskeynest.viewdata

import org.panta.misskeynest.entity.NotificationProperty
import org.panta.misskeynest.interfaces.ID
import java.io.Serializable

data class NotificationViewData(override val id: String,
                                override val isIgnore: Boolean, val notificationProperty: NotificationProperty, val noteViewData: NoteViewData?):Serializable, ID