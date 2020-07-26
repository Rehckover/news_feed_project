package com.example.vjetgrouptestapp.base.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class CombinedLiveData<T, K>(source1: LiveData<T>, source2:(T)-> LiveData<K>) : MediatorLiveData<K>() {

    private var data1: T? = null
    private var data2: LiveData<K>? = null

    init {
        super.addSource(source1) { source1Value ->
            data1 = source1Value
            if(data2 != null) removeSource(data2!!)
            data2 = source2(source1Value)
            super.addSource(data2!!){
                value = it
            }
        }
    }
}