package com.hom.pharmacy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.hom.pharmacy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var progBar: ProgressBar
    private lateinit var vm: PharmacyViewModel
    private lateinit var recy: RecyclerView
    private lateinit var spTown: Spinner
    private lateinit var spCity: Spinner
    private var pharmInfo: PharmacyInfo? = null
    private val TAG: String? = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        Log.d(TAG, "onCreate: mask-")
        findViews()
        vm = ViewModelProvider(this).get(PharmacyViewModel::class.java)
        vm.upDataPharmacy()
        progBar.visibility = View.VISIBLE
        vm.getPharmInfo.observe(this) {
            pharmInfo = it
//            Log.d(TAG, "onCreate: mask-pharmInfo- ${it}")
        }
        vm.getAllCity.observe(this) {
            setSPCity(it)
//            Log.d(TAG, "onCreate: mask-getAllCity- ${it}")
        }
        vm.getAllTown.observe(this) {
            progBar.visibility = View.GONE
            setSPTown(it)
//            Log.d(TAG, "onCreate: mask-getAllTown- ${it}")
        }

    }

    private fun findViews() {
        progBar = binding.progBar
        spCity = binding.spCity
        spTown = binding.spTown
        recy = binding.recyclerPharm
    }

    var currentCity = ""
    private fun setSPCity(cityName: List<String>) {
        val cityAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityName)
                .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        spCity.adapter = cityAdapter
        spCity.prompt = " Select City "
        spCity.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                currentCity = spCity.selectedItem.toString()
                vm.vmUpDataTown(currentCity)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    var currentTown = ""
    private fun setSPTown(townName: List<String>) {
        val townAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, townName)
                .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        spTown.adapter = townAdapter
        spTown.prompt = " Select Town "
        spTown.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                currentTown = spTown.selectedItem.toString()
//                setRecyPharm()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }


}






