package com.hom.pharmacy

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hom.pharmacy.databinding.RowPharmViewBinding

class PharmacyAdapter(val _context: Context, val filterData: List<Feature>) :
    RecyclerView.Adapter<PharmacyAdapter.PharmViewHolder>() {
    class PharmViewHolder(var view: RowPharmViewBinding) : ViewHolder(view.root) {
        val pharmName = view.rowPharmName
        val pharmAdult = view.rowPharmAdult
        val pharmChild = view.rowPharmChild
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmViewHolder {
        val v = RowPharmViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PharmViewHolder(v)
    }

    override fun getItemCount(): Int {
        return filterData.size
    }

    override fun onBindViewHolder(holder: PharmViewHolder, position: Int) {
        val data = filterData.get(position)
        holder.pharmName.setText(data.properties.name)
        holder.pharmAdult.setText(data.properties.mask_adult.toString())
        holder.pharmChild.setText(data.properties.mask_child.toString())
        holder.itemView.setOnClickListener {
            PharmacyViewModel.PharmInfoData.pharmFilterData = data
            Intent(_context, PharmacyActivity::class.java)
                .also { _context.startActivity(it) }
        }
    }


}




