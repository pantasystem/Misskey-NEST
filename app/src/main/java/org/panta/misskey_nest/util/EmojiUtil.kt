package org.panta.misskey_nest.util

import org.panta.misskey_nest.entity.EmojiProperty

fun EmojiProperty.getExtension(): String?{
    val meta = this.type
    return when {
        meta == null -> this.url?.split(".")?.lastOrNull()
            ?:this.uri?.split(".")?.lastOrNull()
        meta.endsWith("xml+svg") -> "svg"
        else -> "png"
    }
}

fun EmojiProperty.createFileName(): String{
    val name = this.name
    return "$name.${getExtension()}"
}

