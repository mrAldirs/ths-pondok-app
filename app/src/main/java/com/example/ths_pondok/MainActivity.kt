package com.example.ths_pondok

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ths_pondok.Absensi.AbsensiFragment
import com.example.ths_pondok.Adapter.AdapterSantri
import com.example.ths_pondok.CatatanHafalan.HafalanFragment
import com.example.ths_pondok.Penilaian.PenilaianFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
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

    val daftarSantri = mutableListOf<HashMap<String,String>>()
    lateinit var santriAdapter : AdapterSantri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setTitle("Home Pengurus")
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayoutMain)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayoutMain, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        urlClass = UrlClass()
        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        santriAdapter = AdapterSantri(daftarSantri)
        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.adapter = santriAdapter

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    supportActionBar?.setTitle("Home")
                    frameLayout.visibility = View.GONE
                }
                R.id.nav_catatan -> {
                    navView.setCheckedItem(R.id.nav_catatan)
                    supportActionBar?.setTitle("Catatan Hafalan")
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, HafalanFragment()).commit()
                    frameLayout.setBackgroundColor(Color.argb(255,255,255,255))
                    frameLayout.visibility = View.VISIBLE
                }
                R.id.nav_absensi -> {
                    navView.setCheckedItem(R.id.nav_absensi)
                    supportActionBar?.setTitle("Absensi")
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, AbsensiFragment()).commit()
                    frameLayout.setBackgroundColor(Color.argb(255,255,255,255))
                    frameLayout.visibility = View.VISIBLE
                }
                R.id.nav_penilaian -> {
                    navView.setCheckedItem(R.id.nav_penilaian)
                    supportActionBar?.setTitle("Penilaian")
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, PenilaianFragment()).commit()
                    frameLayout.setBackgroundColor(Color.argb(255,255,255,255))
                    frameLayout.visibility = View.VISIBLE
                }
                R.id.nav_logout -> {
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
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        btnAbsensiSaya.setOnClickListener {

        }

        btnAbsensiSantri.setOnClickListener {
            navView.setCheckedItem(R.id.nav_absensi)
            supportActionBar?.setTitle("Absensi")
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, AbsensiFragment()).commit()
            frameLayout.setBackgroundColor(Color.argb(255,255,255,255))
            frameLayout.visibility = View.VISIBLE
        }

        btnCatatanHafalan.setOnClickListener {
            navView.setCheckedItem(R.id.nav_catatan)
            supportActionBar?.setTitle("Catatan Hafalan")
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, HafalanFragment()).commit()
            frameLayout.setBackgroundColor(Color.argb(255,255,255,255))
            frameLayout.visibility = View.VISIBLE
        }

        btnPenilaian.setOnClickListener {
            navView.setCheckedItem(R.id.nav_penilaian)
            supportActionBar?.setTitle("Penilaian")
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, PenilaianFragment()).commit()
            frameLayout.setBackgroundColor(Color.argb(255,255,255,255))
            frameLayout.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayoutMain.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutMain.closeDrawer(GravityCompat.START)
        } else {
            AlertDialog.Builder(this)
                .setTitle("Warning!")
                .setIcon(R.drawable.info)
                .setMessage("Apakah anda ingin keluar Aplikasi?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    onBackPressedDispatcher.onBackPressed()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
        }
    }

    override fun onStart() {
        super.onStart()
        showDataSantri("show_data_santri")
    }

    private fun showDataSantri(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlSantri,
            Response.Listener { response ->
                daftarSantri.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var frm = HashMap<String,String>()
                        frm.put("nama_santri",jsonObject.getString("nama_santri"))
                        frm.put("nis",jsonObject.getString("nis"))
                        frm.put("kelas",jsonObject.getString("kelas"))


                        daftarSantri.add(frm)
                    }
                    santriAdapter.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    this, "Koneksi Eror tidak dapat terhubung ke database! Pastikan Anda memiliki jaringan Internet",
                    Toast.LENGTH_SHORT
                ).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "show_data_santri" -> {
                        hm.put("mode","show_data_santri")
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}