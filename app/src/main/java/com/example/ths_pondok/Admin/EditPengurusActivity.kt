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

class EditPengurusActivity : AppCompatActivity() {
    private lateinit var pVM: PengurusViewModel
    val jenkel = arrayOf("-- Pilih Jenis Kelamin --","Laki-laki","Perempuan")
    lateinit var adpJenkel : ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi_pengurus)
        supportActionBar?.setTitle("Edit Pengurus")

        pVM = ViewModelProvider(this).get(PengurusViewModel::class.java)

        adpJenkel = ArrayAdapter(baseContext, android.R.layout.simple_list_item_1, jenkel)
        insJenisKelamin.adapter = adpJenkel

        var paket : Bundle? = intent.extras
        pVM.detail(paket?.getString("kode").toString()).observe(this, Observer { pengurus ->
            regisNama.setText(pengurus.nama_pengguna)
            insJenisKelamin.setSelection(adpJenkel.getPosition(pengurus.jenis_kelamin))
            regisAlamat.setText(pengurus.alamat)
            regisHp.setText(pengurus.no_telp)
            regisUsername.setText(pengurus.username)
            regisPassword.setText(pengurus.password)
        })

        btnSimpan.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Informasi!!")
                .setIcon(android.R.drawable.stat_sys_warning)
                .setMessage("Apakah Anda ingin mengubah profil dari pengurus?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    edit(paket?.getString("kode").toString())
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                    null
                })
                .show()
        }
    }

    fun edit(id:String) {
        val data = Pengurus(
            regisNama.text.toString(),
            insJenisKelamin.selectedItem.toString(),
            regisAlamat.text.toString(),
            regisHp.text.toString(),
            regisUsername.text.toString(),
            regisPassword.text.toString()
        )
        pVM.edit(id, data).observe(this, Observer {
            Toast.makeText(this, "Berhasil mengubah data!", Toast.LENGTH_SHORT).show()
            onBackPressed()
        })
    }
}