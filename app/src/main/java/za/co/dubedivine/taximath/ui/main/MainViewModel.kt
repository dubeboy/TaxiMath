package za.co.dubedivine.taximath.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import za.co.dubedivine.taximath.model.Money

class MainViewModel : ViewModel() {

    /*you pronounce mony just like the nigerians say money ;) */
    private lateinit var mony: MutableLiveData<ArrayList<Money>>


    init {
        if (!::mony.isInitialized) {
            mony = MutableLiveData()
        }
    }


    /* not sure if this belongs here but then we will add a DB later*/
    /*could have created a static variable some where but this should do for now*/
    fun insert(money: Money) {
        mony.value!!.add(money)
    }

    fun getMony(): LiveData<ArrayList<Money>> {
        return mony
    }

}
