package com.dave_devs.custompaginationsample.custom_pagination

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class Repository {
    private val remoteDataSource = (1..200).map { count -> 
        ItemList(
            title = "Item $count",
            description = "Description $count"
        )
    }
    
    suspend fun getItems(page: Int, pageSize: Int): Result<List<ItemList>> {
        delay(2.seconds)
        //starting number of pagination = to whatever page & pageSize is.
        val startingIndex = page + pageSize
        //Return if starting index is less or equals remoteDatasource size
        return if(startingIndex <= remoteDataSource.size) {
            Result.success(
                remoteDataSource.slice(startingIndex until startingIndex + pageSize)
            )
        } else Result.success(emptyList())
    }
}
