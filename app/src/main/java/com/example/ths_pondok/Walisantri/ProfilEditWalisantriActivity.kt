package com.example.ths_pondok.Walisantri

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apk_pn.Helper.MediaHelper
import com.example.ths_pondok.R
import com.example.ths_pondok.UrlClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profil_edit_walisantri.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ProfilEditWalisantriActivity : AppCompatActivity() {

    lateinit var urlClass: UrlClass
    lateinit var preferences: SharedPreferences
    val PREF_NAME = "akun"
    val USERNAME = "username"
    val DEF_USERNAME = ""

    lateinit var mediaHealper: MediaHelper
    var imStr = ""

    var idWali = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_edit_walisantri)
        supportActionBar?.setTitle("Edit Profil")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        urlClass = UrlClass()

        btnEditFoto.setOnClickListener {
            etFotoProfil.visibility = View.GONE
            etFotoProfilUpdate.visibility = View.VISIBLE
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,mediaHealper.RcGallery())
        }

        etTanggalLahir.setOnClickListener {
            showDatePickerDialog()
        }

        btnSimpanEditProfil.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Warning!")
                .setIcon(R.drawable.info)
                .setMessage("Apakah anda ingin menyimpan perubahan ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    editProfil("edit")
                    onBackPressed()
                    Toast.makeText(this, "Berhasil Mengedit Profil Santri!", Toast.LENGTH_LONG)
                        .show()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        showProfil("show_profil")
        etNamaSantri.requestFocus()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == mediaHealper.RcGallery()){
                imStr = mediaHealper.getBitmapToString(data!!.data,etFotoProfilUpdate)
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val date = "$year-${month + 1}-$dayOfMonth"
                etTanggalLahir.setText(date)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun showProfil(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlWalisantri,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val idWal = jsonObject.getString("id_walisantri")
                val img = jsonObject.getString("img")
                val nm = jsonObject.getString("nama_santri")
                val nis = jsonObject.getString("nis")
                val kls = jsonObject.getString("kelas")
                val tmp = jsonObject.getString("tempat")
                val ttl = jsonObject.getString("tanggal_lahir")
                val jk = jsonObject.getString("jenis_kelamin")
                val usia = jsonObject.getString("usia")
                val alm = jsonObject.getString("alamat")
                val ayah = jsonObject.getString("nama_ayah")
                val ibu = jsonObject.getString("nama_ibu")
                val wal = jsonObject.getString("nama_wali")
                val noTp = jsonObject.getString("no_telepon")
                val krjA = jsonObject.getString("pekerjaan_ayah")
                val krjI = jsonObject.getString("pekerjaan_ibu")

                idWali = idWal
                Picasso.get().load(img).into(etFotoProfil)
                etNamaSantri.setText(nm)
                etNisSantri.setText(nis)
                etKelasSantri.setText(kls)
                etTempat.setText(tmp)
                etTanggalLahir.setText(ttl)
                etJenisKelamin.setText(jk)
                etUsia.setText(usia)
                etAlamat.setText(alm)
                etNamaAyah.setText(ayah)
                etNamaIbu.setText(ibu)
                etNamaWali.setText(wal)
                etNoTelepon.setText(noTp)
                etPekerjaanAyah.setText(krjA)
                etPekerjaanIbu.setText(krjI)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Tidak dapat terhubung ke server!", Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "show_profil" -> {
                        hm.put("mode","show_profil")
                        hm.put("username",preferences.getString(USERNAME, DEF_USERNAME).toString())
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun editProfil(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlWalisantri,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = java.util.HashMap<String, String>()
                when(mode) {
                    "edit" -> {
                        hm.put("mode", "edit")
                        val nmFile ="IMG_"+ SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
                            Date()
                        )+".jpg"
                        hm.put("id_walisantri", idWali)
                        hm.put("nis", etNisSantri.text.toString())
                        hm.put("nama_santri", etNamaSantri.text.toString())
                        hm.put("tempat", etTempat.text.toString())
                        hm.put("tanggal_lahir", etTanggalLahir.text.toString())
                        hm.put("jenis_kelamin", etJenisKelamin.text.toString())
                        hm.put("usia", etUsia.text.toString())
                        hm.put("alamat", etAlamat.text.toString())
                        hm.put("nama_ayah", etNamaAyah.text.toString())
                        hm.put("nama_ibu", etNamaIbu.text.toString())
                        hm.put("nama_wali", etNamaWali.text.toString())
                        hm.put("no_telepon", etNoTelepon.text.toString())
                        hm.put("pekerjaan_ayah", etPekerjaanAyah.text.toString())
                        hm.put("pekerjaan_ibu", etPekerjaanIbu.text.toString())
                        hm.put("image",imStr)
                        hm.put("file",nmFile)
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}