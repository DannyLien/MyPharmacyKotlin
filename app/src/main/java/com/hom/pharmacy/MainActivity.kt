package com.hom.pharmacy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hom.pharmacy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
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
        Log.d(TAG, "onCreate: mask-")
        val vm = ViewModelProvider(this).get(PharmacyViewModel::class.java)
        vm.upDataPharmacy()
        vm.getPharmInfo.observe(this){
            pharmInfo = it
            Log.d(TAG, "onCreate: mask-pharmInfo- ${it}")
        }

    }


}






