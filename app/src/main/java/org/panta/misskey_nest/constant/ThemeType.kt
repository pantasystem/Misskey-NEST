package org.panta.misskey_nest.constant

enum class ThemeType{
    STANDARD, BLACK;

    companion object{
        fun getThemeTypeFromInt(type: Int): ThemeType{
            return when(type){
                STANDARD.ordinal -> STANDARD
                BLACK.ordinal -> BLACK
                else -> STANDARD
            }
        }
    }
}