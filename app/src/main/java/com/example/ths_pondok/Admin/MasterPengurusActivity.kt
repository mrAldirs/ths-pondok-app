package com.example.ths_pondok.Admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ths_pondok.Adapter.AdapterPengurus
import com.example.ths_pondok.Admin.ViewModel.PengurusViewModel
import com.example.ths_pondok.R
import kotlinx.android.synthetic.main.activity_master_pengurus.*

class MasterPengurusActivity : AppCompatActivity() {
    private lateinit var pVM : PengurusViewModel
    private lateinit var adapter: AdapterPengurus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master_pengurus)
        supportActionBar?.setTitle("Master Pengurus")

        pVM = ViewModelProvider(this).get(PengurusViewModel::class.java)

        adapter = AdapterPengurus(ArrayList(), supportFragmentManager)
        rvPengurus.layoutManager = LinearLayoutManager(baseContext)
        rvPengurus.adapter = adapter

        btnTambahPengurus.setOnClickListener {
            startActivity(Intent(baseContext, RegistrasiPengurusActivity::class.java))
        }

        btnCari.setOnClickListener {
            loadData(etCari.text.toString().trim())
        }
    }

    override fun onStart() {
        super.onStart()
        loadData("")
    }

    fun loadData(nama: String) {
        pVM.load_data(nama).observe(this, Observer { list ->
            adapter.setData(list)
        })
    }
}