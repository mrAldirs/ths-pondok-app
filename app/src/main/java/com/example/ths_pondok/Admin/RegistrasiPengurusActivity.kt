package com.example.ths_pondok.Admin

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ths_pondok.Admin.Model.Pengurus
import com.example.ths_pondok.Admin.ViewModel.PengurusViewModel
import com.example.ths_pondok.R
import kotlinx.android.synthetic.main.activity_registrasi_pengurus.*

class RegistrasiPengurusActivity : AppCompatActivity() {
    private lateinit var pVM : PengurusViewModel

    val jenkel = arrayOf("-- Pilih Jenis Kelamin --","Laki-laki","Perempuan")
    lateinit var adpJenkel : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi_pengurus)
        supportActionBar?.setTitle("Registrasi Pengurus")

        adpJenkel = ArrayAdapter(baseContext, android.R.layout.simple_list_item_1, jenkel)
        insJenisKelamin.adapter = adpJenkel

        pVM = ViewModelProvider(this).get(PengurusViewModel::class.java)

        btnSimpan.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Informasi!!")
                .setIcon(android.R.drawable.stat_sys_warning)
                .setMessage("Apakah Anda ingin menambah pengurus baru?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    insert()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                    null
                })
                .show()
        }
    }

    private fun insert() {
        val data = Pengurus(
            regisNama.text.toString(),
            insJenisKelamin.selectedItem.toString(),
            regisAlamat.text.toString(),
            regisHp.text.toString(),
            regisUsername.text.toString(),
            regisPassword.text.toString()
        )
        pVM.insert(data).observe(this, Observer { result ->
            Toast.makeText(this, "Berhasil menambahkan akun pengurus!", Toast.LENGTH_SHORT).show()
            onBackPressed()
        })
    }
}