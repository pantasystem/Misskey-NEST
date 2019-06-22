package org.panta.misskeynest.constant

enum class TimelineTypeEnum{
    GLOBAL, HOME, LOCAL, SOCIAL, DESCRIPTION, USER;
    companion object {
        fun toEnum(st: String): TimelineTypeEnum{
            return when(st.toUpperCase()){
                GLOBAL.name -> GLOBAL
                HOME.name -> HOME
                LOCAL.name -> LOCAL
                SOCIAL.name -> SOCIAL
                USER.name -> USER
                else -> HOME
            }
        }
    }
}

