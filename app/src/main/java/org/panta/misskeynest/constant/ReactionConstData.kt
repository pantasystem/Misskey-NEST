package org.panta.misskeynest.constant

import org.panta.misskeynest.R

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

        fun getDefaultReactionIconMapping(): Map<String, Int>{
            return hashMapOf(
                ReactionConstData.LIKE to R.drawable.ic_reaction_like,
                ReactionConstData.LOVE to R.drawable.ic_reaction_love,
                ReactionConstData.HMM to R.drawable.ic_reaction_hmm,
                ReactionConstData.SURPRISE to R.drawable.ic_reaction_surprise,
                ReactionConstData.CONGRATS to R.drawable.ic_reaction_congrats,
                ReactionConstData.LAUGH to R.drawable.ic_reaction_laugh,


                ReactionConstData.ANGRY to R.drawable.ic_reaction_angry,
                CONFUSED to R.drawable.ic_reaction_confused,
                RIP to R.drawable.ic_reaction_rip,
                PUDDING to R.drawable.ic_reaction_pudding
            )
        }
    }
}