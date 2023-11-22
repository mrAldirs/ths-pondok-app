package com.example.ths_pondok.Walisantri

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ths_pondok.LoginActivity
import com.example.ths_pondok.R
import com.example.ths_pondok.UrlClass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main_wali.*
import org.json.JSONObject

class MainWaliActivity : AppCompatActivity() {

    lateinit var urlClass: UrlClass
    lateinit var preferences: SharedPreferences
    val PREF_NAME = "akun"
    val NAMA = "nama"
    val DEF_NAMA = ""
    val USERNAME = "username"
    val DEF_USERNAME = ""
    val PASSWORD = "password"
    val DEF_PASSWORD = ""
    val LEVEL = "level"
    val DEF_LEVEL = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_wali)
        supportActionBar?.setTitle("Home Wali Santri")

        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        urlClass = UrlClass()

        btnDetailProfil.setOnClickListener {
            startActivity(Intent(this, ProfilDetailWalisantriActivity::class.java))
        }

        btnMainUbahProfil.setOnClickListener {
            startActivity(Intent(this, ProfilEditWalisantriActivity::class.java))
        }

        btnMainHafalan.setOnClickListener {
            val intent = Intent(this, HafalanWalisantriActivity::class.java)
            intent.putExtra("nis", mainNisSantri.text.toString())
            startActivity(intent)
        }

        btnMainPenilaian.setOnClickListener {
            val intent = Intent(this, PenilaianWalisantriActivity::class.java)
            intent.putExtra("nis", mainNisSantri.text.toString())
            startActivity(intent)
        }

        btnMainAbsensi.setOnClickListener {
            val intent = Intent(this, AbsensiWalisantriActivity::class.java)
            intent.putExtra("nis", mainNisSantri.text.toString())
            intent.putExtra("nama", mainNamaSantri.text.toString())
            startActivity(intent)
        }

        btnLogoutWali.setOnClickListener {
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.stat_sys_warning)
                .setTitle("Logout")
                .setMessage("Apakah Anda ingin keluar aplikasi?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    val prefEditor = preferences.edit()
                    prefEditor.putString(NAMA,null)
                    prefEditor.putString(USERNAME,null)
                    prefEditor.putString(PASSWORD,null)
                    prefEditor.putString(LEVEL,null)
                    prefEditor.commit()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                })
                .show()
            true
        }
    }

    override fun onStart() {
        super.onStart()
        showProfil("show_profil")
    }

    fun showProfil(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlWalisantri,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val nm = jsonObject.getString("nama_santri")
                val nis = jsonObject.getString("nis")
                val kls = jsonObject.getString("kelas")
                val wal = jsonObject.getString("nama_wali")
                val img = jsonObject.getString("img")

                mainNamaSantri.setText(nm)
                mainNisSantri.setText(nis)
                mainKelasSantri.setText(kls)
                mainNamaWali.setText(wal)
                Picasso.get().load(img).into(mainFotoProfil)
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