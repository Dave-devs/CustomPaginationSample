package com.dave_devs.custompaginationsample.custom_pagination

interface Paginator<Key, Item> {

    suspend fun loadNextItems()
    fun reset()
}