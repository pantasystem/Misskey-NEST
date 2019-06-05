package org.panta.misskey_nest.view_data

import org.panta.misskey_nest.entity.NotificationProperty
import org.panta.misskey_nest.interfaces.ID
import java.io.Serializable

data class NotificationViewData(override val id: String,
                                override val isIgnore: Boolean, val notificationProperty: NotificationProperty, val noteViewData: NoteViewData?):Serializable, ID