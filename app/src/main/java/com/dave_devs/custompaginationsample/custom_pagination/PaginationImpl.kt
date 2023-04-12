package com.dave_devs.custompaginationsample.custom_pagination

class PaginationImpl<Key, Item>(
    private val initialKey: Key, //Initial key(page) to start with which is 0
    /*
    Lambda callback that is call when the laos]ding value is updated.
    Either if we are loading the new items or if we finish loading the item
    so we could show progress indicator in our UI.
    * */
    private inline val onLoadUpdated: (Boolean) -> Unit,
    /*This defines how we get the next load of items given the key*/
    private inline val onRequest: suspend (nextKey: Key) -> Result<List<Item>>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: List<Item>, nextKey: Key) -> Unit
): Paginator<Key, Item> {
    //Member variable to get the currentPage from the InitialKey above in class constructor
    private var currentKey: Key = initialKey
    /* To know the current state of request to the Api/Database
    to get data it will be false as long as we don't make request.
    It is there to prevent the issue of loading two pages at once.
    */
    private var isMakingRequest: Boolean = false
    //Function to load next page of items/data.
    override suspend fun loadNextItems() {
        //Check if we are currently making request and return(ignore) if we are.
       if (isMakingRequest) {
           return
       }
        //if making request is true, we put a loading status up to true/CircularProgressIndicator.
        isMakingRequest = true
        onLoadUpdated(true)
        //Then we can get the result and provide currentKey as the next key/page/Items we want to load
        val result = onRequest(currentKey)
        //After we get the data we make isMakingRequest to False
        isMakingRequest = false
        //If everything went well, we get the result(items) else the OrElse from getOrElse will return a Throwable
        val items = result.getOrElse {
            onError(it)
            //We are no more loading, we set it to false and get out of the load next item function
            onLoadUpdated(false)
            return
        }
        //If we didn't get an error the we have a list of item to pass
        currentKey = getNextKey(items)
        onSuccess(items, currentKey)
        onLoadUpdated(false)
    }

    override fun reset() {
       currentKey = initialKey
    }
}