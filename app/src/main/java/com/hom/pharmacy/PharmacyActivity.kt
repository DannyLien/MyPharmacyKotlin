package com.hom.pharmacy

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hom.pharmacy.databinding.ActivityPharmacyBinding

class PharmacyActivity : AppCompatActivity() {
    private var filterData: Feature? = null
    private lateinit var pharmAddress: TextView
    private lateinit var pharmPhone: TextView
    private lateinit var pharmChild: TextView
    private lateinit var pharmAdult: TextView
    private lateinit var pharmName: TextView
    private lateinit var binding: ActivityPharmacyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPharmacyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        filterData = PharmacyViewModel.PharmInfoData.pharmFilterData

        findViews()

    }

    private fun findViews() {
        pharmName = binding.pharmName
        pharmAdult = binding.pharmAdult
        pharmChild = binding.pharmChild
        pharmPhone = binding.pharmPhone
        pharmAddress = binding.pharmAddress

        filterData?.also {
            pharmName.setText(it.properties.name)
            pharmAdult.setText(it.properties.mask_adult.toString())
            pharmChild.setText(it.properties.mask_child.toString())
            pharmPhone.setText(it.properties.phone)
            pharmAddress.setText(it.properties.address)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_pharm_maps, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val menuId = item.itemId
        when (menuId) {
            R.id.action_back -> {
                finish()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}







