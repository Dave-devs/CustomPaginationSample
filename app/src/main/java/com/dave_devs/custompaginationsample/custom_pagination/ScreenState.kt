package com.dave_devs.custompaginationsample.custom_pagination

data class ScreenState(
    val items: List<ItemList> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0
)
