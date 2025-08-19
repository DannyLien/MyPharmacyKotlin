package com.hom.pharmacy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class PharmacyViewModel : ViewModel() {
    companion object {
        val pharmUrl_5K = "http://delexons.ddns.net:81/pharmacies/info.json"
        val pharmUrl_132 = "http://delexons.ddns.net:81/pharmacies/info_132.json"
    }

    private var pharmInfo: PharmacyInfo? = null
    var getPharmInfo = MutableLiveData<PharmacyInfo>()
    fun upDataPharmacy() {
        CoroutineScope(Dispatchers.IO).launch {
            val json = URL(pharmUrl_132).readText()
            pharmInfo = Gson().fromJson(json, PharmacyInfo::class.java)
            getPharmInfo.postValue(pharmInfo!!)
        }
    }

}



