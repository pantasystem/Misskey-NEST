package org.panta.misskeynest.repository

interface IItemRepository<E> {
    fun getItemsUseSinceId( sinceId: String ): List<E>?

    fun getItemsUseUntilId( untilId: String ): List<E>?

    fun getItems(): List<E>?


}