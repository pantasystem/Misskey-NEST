package org.panta.misskeynest.usecase

import org.panta.misskeynest.entity.ReactionCountPair
import org.panta.misskeynest.view_data.NoteViewData

class NoteUpdater{

    private val noteAdjustment = NoteAdjustment(false)

    fun addReaction(reaction: String, viewData: NoteViewData, hasMyReaction: Boolean): NoteViewData{
        val note = viewData.toShowNote
        val updatedReactionCounts = updateUpdateReactionCounts(reaction, note.reactionCounts)

        val updatedNote = if(hasMyReaction){
            note.copy(myReaction = reaction, reactionCounts = updatedReactionCounts)
        }else{
            note.copy(reactionCounts = updatedReactionCounts)
        }


        val reactionCountPair = updateUpdateReactionCountPair(reaction,viewData.reactionCountPairList)
        return viewData.copy(toShowNote = updatedNote, reactionCountPairList = reactionCountPair)

    }

    fun removeReaction(reaction: String, viewData: NoteViewData, isMyReaction: Boolean): NoteViewData{
        val note = viewData.toShowNote
        val removedReactionCounts = removeReactionCounts(reaction, note.reactionCounts)

        val removed = if(isMyReaction){
            note.copy(myReaction = null, reactionCounts = removedReactionCounts)
        }else{
            note.copy(reactionCounts = removedReactionCounts)
        }

        val reactionCountPair = removeReactionCountPair(reaction, viewData.reactionCountPairList)
        /*val reactionCountPair = removedReactionCounts.map {
            ReactionCountPair(it.key, it.value.toString())
        }.filter{
            Integer.parseInt(it.reactionCount) > 0
        }*/
        return viewData.copy(toShowNote = removed, reactionCountPairList = reactionCountPair)
    }

    private fun removeReactionCounts(reaction: String, map: Map<String, Int>?): Map<String, Int>{
        return if(map != null && map.isNotEmpty()){
            val hashMap = HashMap<String, Int>(map)
            val hasReaction = hashMap[reaction]
            if(hasReaction != null){
                hashMap[reaction] = hasReaction - 1
            }
            hashMap.filter{
                it.value > 0
            }
        }else{
            emptyMap()
        }
    }
    private fun updateUpdateReactionCounts(reaction: String, map: Map<String, Int>?): Map<String, Int>{
        return if(map != null && map.isNotEmpty()){
            val hashMap = HashMap<String, Int>(map)
            val hasReaction = hashMap[reaction]
            if(hasReaction != null){
                hashMap[reaction] = hasReaction + 1
            }else{
                hashMap[reaction] = 1
            }
            hashMap.filter {
                it.value > 0
            }
        }else{
            hashMapOf(reaction to 1)
        }
    }

    private fun updateUpdateReactionCountPair(reaction: String, pairList: List<ReactionCountPair>): List<ReactionCountPair>{
        val arrayList = ArrayList<ReactionCountPair>(pairList)
        val count = pairList.filter{
            it.reactionType == reaction
        }.count()
        if(count > 0){
            for(n in 0 until arrayList.size){
                if(arrayList[n].reactionType == reaction){
                    val reactionCount = arrayList[n].reactionCount + 1
                    arrayList[n] = arrayList[n].copy(reactionCount = reactionCount)
                }
            }
        }else{
            arrayList.add(ReactionCountPair(reaction, 1))
        }
        return arrayList

    }

    private fun removeReactionCountPair(reaction: String, pairList: List<ReactionCountPair>): List<ReactionCountPair>{
        val list = ArrayList<ReactionCountPair>(pairList)

        val count = list.count{ it.reactionType == reaction }
        return if(count > 0 ){
            list.map{
                if(it.reactionType == reaction){
                    val reactionCount = it.reactionCount - 1
                    it.copy(reactionCount = reactionCount)
                }else{
                    it
                }
            }.filter{
                it.reactionCount > 0
            }
        }else{
            list
        }
    }


}