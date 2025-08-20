package com.hom.pharmacy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class PharmacyViewModel : ViewModel() {

    var allTown = mutableListOf<String>()
    var getAllTown = MutableLiveData<List<String>>()
    var allCity = mutableListOf<String>()
    var getAllCity = MutableLiveData<List<String>>()
    private var pharmInfo: PharmacyInfo? = null
    var getPharmInfo = MutableLiveData<PharmacyInfo>()

    companion object {
        val pharmUrl_5K = "http://delexons.ddns.net:81/pharmacies/info.json"
        val pharmUrl_132 = "http://delexons.ddns.net:81/pharmacies/info_132.json"
    }

    object PharmInfoData {
        var pharmInfoGson: PharmacyInfo? = null
    }

    fun upDataPharmacy() {
        CoroutineScope(Dispatchers.IO).launch {
            val json = URL(pharmUrl_132).readText()
            pharmInfo = Gson().fromJson(json, PharmacyInfo::class.java)
            getPharmInfo.postValue(pharmInfo!!)
            PharmInfoData.pharmInfoGson = pharmInfo
            vmUpDataCity()
        }
    }

    fun vmUpDataCity() {
        pharmInfo?.also { pharm ->
            val data = pharm.features.groupBy { it.properties.county }
            data.forEach {
                allCity.add(it.key)
            }
        }
        getAllCity.postValue(allCity)
    }

    fun vmUpDataTown(currentCity: String) {
        pharmInfo?.also { pharm ->
            val data = pharm.features.filter { it.properties.county == currentCity }
            allTown.clear()
            data.forEach {
                allTown.add(it.properties.town)
            }
            allTown = allTown.distinct().toMutableList()
            getAllTown.postValue(allTown)
        }
    }


}



