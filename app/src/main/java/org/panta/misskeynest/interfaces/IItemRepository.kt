package org.panta.misskeynest.interfaces

interface IItemRepository<E: ID> {
    fun getItemsUseSinceId( sinceId: String ): List<E>?

    fun getItemsUseUntilId( untilId: String ): List<E>?

    fun getItems(): List<E>?


}