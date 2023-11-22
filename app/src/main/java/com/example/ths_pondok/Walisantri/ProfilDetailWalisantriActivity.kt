package com.example.ths_pondok.Walisantri

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ths_pondok.R
import com.example.ths_pondok.UrlClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profil_detail_walisantri.*
import org.json.JSONObject

class ProfilDetailWalisantriActivity : AppCompatActivity() {

    lateinit var urlClass: UrlClass
    lateinit var preferences: SharedPreferences
    val PREF_NAME = "akun"
    val USERNAME = "username"
    val DEF_USERNAME = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_detail_walisantri)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        urlClass = UrlClass()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        showProfil("show_profil")
    }

    private fun showProfil(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlWalisantri,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val sts = jsonObject.getString("status_santri")
                val img = jsonObject.getString("img")
                val nm = jsonObject.getString("nama_santri")
                val nis = jsonObject.getString("nis")
                val kls = jsonObject.getString("kelas")
                val ttl = jsonObject.getString("ttl")
                val jk = jsonObject.getString("jenis_kelamin")
                val usia = jsonObject.getString("usia")
                val alm = jsonObject.getString("alamat")
                val ayah = jsonObject.getString("nama_ayah")
                val ibu = jsonObject.getString("nama_ibu")
                val wal = jsonObject.getString("nama_wali")
                val noTp = jsonObject.getString("no_telepon")
                val krjA = jsonObject.getString("pekerjaan_ayah")
                val krjI = jsonObject.getString("pekerjaan_ibu")

                supportActionBar?.setTitle("Detail Profil "+nm)

                detailStatus.setText("Status Santri "+sts)
                Picasso.get().load(img).into(detailFotoProfil)
                detailNamaSantri.setText(nm)
                detailNisSantri.setText(nis)
                detailKelasSantri.setText(kls)
                detailTtl.setText(ttl)
                detailJenisKelamin.setText(jk)
                detailUsia.setText(usia)
                detailAlamat.setText(alm)
                detailNamaAyah.setText(ayah)
                detailNamaIbu.setText(ibu)
                detailNamaWali.setText(wal)
                detailNoTelepon.setText(noTp)
                detailPekerjaanAyah.setText(krjA)
                detailPekerjaanIbu.setText(krjI)
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
}