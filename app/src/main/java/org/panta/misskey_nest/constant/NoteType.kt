package org.panta.misskey_nest.constant

enum class NoteType {
    CREATE, RE_NOTE, REPLY;
    companion object {
        fun getEnumFromInt(number: Int): NoteType{
            return when(number){
                CREATE.ordinal -> CREATE
                RE_NOTE.ordinal -> RE_NOTE
                REPLY.ordinal -> REPLY
                else -> CREATE
            }
        }
    }
}