package org.panta.misskeynest.interfaces

interface IItemFilter<in I: Any, out O : ID>{
    fun filter(items: List<I>): List<O>
}