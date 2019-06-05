package org.panta.misskey_nest.entity

import org.panta.misskey_nest.constant.ReactionConstData


data class ReactionCountPair(val reactionType: String, val reactionCount: Int){

    companion object{
        fun createList(reactionCount: Map<String, Int>): List<ReactionCountPair>{
            /*val array = ArrayList<ReactionCountPair>()
            if(reactionCount.angry != null){
                array.add(ReactionCountPair(ReactionConstData.ANGRY, reactionCount.angry.toString()))
            }
            if(reactionCount.confused != null){
                array.add(ReactionCountPair(ReactionConstData.CONFUSED, reactionCount.confused.toString()))

            }
            if(reactionCount.congrats != null){
                array.add(ReactionCountPair(ReactionConstData.CONGRATS, reactionCount.congrats.toString()))

            }
            if(reactionCount.hmm != null){
                array.add(ReactionCountPair(ReactionConstData.HMM, reactionCount.hmm.toString()))

            }
            if(reactionCount.laugh != null){
                array.add(ReactionCountPair(ReactionConstData.LAUGH, reactionCount.laugh.toString()))

            }
            if(reactionCount.like != null){
                array.add(ReactionCountPair(ReactionConstData.LIKE, reactionCount.like.toString()))

            }
            if(reactionCount.love != null){
                array.add(ReactionCountPair(ReactionConstData.LOVE, reactionCount.love.toString()))

            }
            if(reactionCount.pudding != null){
                array.add(ReactionCountPair(ReactionConstData.PUDDING, reactionCount.pudding.toString()))

            }
            if(reactionCount.rip != null){
                array.add(ReactionCountPair(ReactionConstData.RIP, reactionCount.rip.toString()))

            }
            if(reactionCount.surprise != null){
                array.add(ReactionCountPair(ReactionConstData.SURPRISE, reactionCount.surprise.toString()))

            }
            return array.filter{
                Integer.parseInt(it.reactionCount) > 0
            }*/
            return reactionCount.map{
                ReactionCountPair(it.key, it.value)
            }.filter{
                it.reactionCount > 0
            }
        }
    }
}
