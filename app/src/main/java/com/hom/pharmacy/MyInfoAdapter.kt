package com.hom.pharmacy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.hom.pharmacy.databinding.RowInfoViewBinding

class MyInfoAdapter(val _context: Context) :
    GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker): View? {
        val binding = RowInfoViewBinding.inflate(
            _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        )
            val mask = marker.snippet.toString().split(",")
            binding.rowInfoName.setText(marker.title)
            binding.rowInfoAdult.setText(mask.get(0))
            binding.rowInfoChild.setText(mask.get(1))
            return binding.root
    }

    override fun getInfoWindow(marker: Marker): View? {

        return null
    }


}
