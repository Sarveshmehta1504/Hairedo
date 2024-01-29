package com.example.hairedo

data class DataItem(val viewType: Int) {

    var recyclerItemList: List<RecyclerItem>? = null
    constructor(viewType: Int, recyclerItemList: List<RecyclerItem>): this(viewType){
        this.recyclerItemList = recyclerItemList
    }

}

data class RecyclerItem(val image: Int, val shopName: String, val salonType: String, val visit: String, val time: String)