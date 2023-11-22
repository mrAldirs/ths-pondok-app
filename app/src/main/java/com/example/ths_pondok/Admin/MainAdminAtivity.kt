package com.example.ths_pondok.Admin

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ths_pondok.LoginActivity
import com.example.ths_pondok.R
import com.example.ths_pondok.UrlClass
import kotlinx.android.synthetic.main.activity_main_admin.*

class MainAdminAtivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_main_admin)
        supportActionBar?.setTitle("Home Admin")

        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        urlClass = UrlClass()

        btnMasterPengurus.setOnClickListener {
            startActivity(Intent(baseContext, MasterPengurusActivity::class.java))
        }

        btnLogoutAdmin.setOnClickListener {
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
        }
    }
}