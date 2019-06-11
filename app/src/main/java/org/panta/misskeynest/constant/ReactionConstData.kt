package org.panta.misskeynest.constant

class ReactionConstData{
    companion object{
        const val LIKE = "like"
        const val LOVE = "love"
        const val LAUGH = "laugh"
        const val HMM = "hmm"
        const val SURPRISE = "surprise"
        const val CONGRATS = "congrats"
        const val ANGRY = "angry"
        const val CONFUSED = "confused"
        const val RIP = "rip"
        const val PUDDING = "pudding"

        fun getAllConstReactionList(): List<String>{
            return listOf(
                "like","love","laugh","hmm","surprise","congrats","angry","confused","rip","pudding"
            )
        }
    }
}