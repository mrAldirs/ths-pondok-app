package com.example.ths_pondok

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ths_pondok.Admin.MainAdminAtivity
import com.example.ths_pondok.Walisantri.MainWaliActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var preferences: SharedPreferences
    lateinit var urlClass: UrlClass

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
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        urlClass = UrlClass()

        btnLogin.setOnClickListener {
            if (etEmail.text.toString().equals("")){
                Toast.makeText(this,"Username tidak boleh kosong!", Toast.LENGTH_LONG).show()
            }else if(etPassword.text.toString().equals("")){
                Toast.makeText(this,"Password tidak boleh kosong!", Toast.LENGTH_LONG).show()
            }else{
                validationAccount("login")
            }
        }
    }

    private fun validationAccount(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.validasi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val level = jsonObject.getString("level")
                if(level.equals("Pengurus")){
                    preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    val nama = jsonObject.getString("nama_pengguna")
                    val username = jsonObject.getString("username")
                    val password = jsonObject.getString("password")
                    val prefEditor = preferences.edit()
                    prefEditor.putString(NAMA,nama)
                    prefEditor.putString(USERNAME, username)
                    prefEditor.putString(PASSWORD, password)
                    prefEditor.putString(LEVEL, level)
                    prefEditor.commit()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else if (level.equals("Wali")) {
                    preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    val nama = jsonObject.getString("nama_pengguna")
                    val username = jsonObject.getString("username")
                    val password = jsonObject.getString("password")
                    val prefEditor = preferences.edit()
                    prefEditor.putString(NAMA,nama)
                    prefEditor.putString(USERNAME, username)
                    prefEditor.putString(PASSWORD, password)
                    prefEditor.putString(LEVEL, level)
                    prefEditor.commit()

                    val intent = Intent(this, MainWaliActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else if (level.equals("Admin")) {
                    preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    val nama = jsonObject.getString("nama_pengguna")
                    val username = jsonObject.getString("username")
                    val password = jsonObject.getString("password")
                    val prefEditor = preferences.edit()
                    prefEditor.putString(NAMA,nama)
                    prefEditor.putString(USERNAME, username)
                    prefEditor.putString(PASSWORD, password)
                    prefEditor.putString(LEVEL, level)
                    prefEditor.commit()

                    val intent = Intent(this, MainAdminAtivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else{
                    AlertDialog.Builder(this)
                        .setTitle("Peringatan!")
                        .setMessage("Username atau Katasandi Anda salah!")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->

                        })
                        .show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Koneksi Eror tidak dapat terhubung ke database! Pastikan Anda memiliki jaringan Internet", Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("username",etEmail.text.toString())
                hm.put("password",etPassword.text.toString())
                when(mode) {
                    "login" -> {
                        hm.put("mode", "login")
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}