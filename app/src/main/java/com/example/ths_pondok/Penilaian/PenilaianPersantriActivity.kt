package com.example.ths_pondok.Penilaian

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ths_pondok.Adapter.AdapterPenilaian
import com.example.ths_pondok.R
import com.example.ths_pondok.UrlClass
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_penilaian_perkelas.*
import org.json.JSONArray

class PenilaianPersantriActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var urlClass: UrlClass
    var kelas = ""

    val daftarPenilaian = mutableListOf<HashMap<String,String>>()
    lateinit var penilaianAdapter: AdapterPenilaian

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penilaian_perkelas)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()

        penilaianAdapter = AdapterPenilaian(daftarPenilaian)
        rvPenilaian.layoutManager = LinearLayoutManager(this)
        rvPenilaian.adapter = penilaianAdapter

        supportActionBar?.setTitle("Penilaian Santri")

        btnCari.setOnClickListener {
            showDataPenilaian(etCari.text.toString().trim())
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        showDataPenilaian("")
    }

    private fun showDataPenilaian(nilai: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlPenilaian,
            Response.Listener { response ->
                daftarPenilaian.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var frm = HashMap<String,String>()
                        frm.put("id_nilai",jsonObject.getString("id_nilai"))
                        frm.put("nama_santri",jsonObject.getString("nama_santri"))
                        frm.put("nilai_persurat",jsonObject.getString("nilai_persurat"))
                        frm.put("nilai_salah",jsonObject.getString("nilai_salah"))


                        daftarPenilaian.add(frm)
                    }
                    penilaianAdapter.notifyDataSetChanged()
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

                hm.put("mode","show_data_penilaian")
                hm.put("nis", intent.getStringExtra("nis").toString())
                hm.put("nilai_persurat", nilai)

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.menu_riwayatPenilaian -> {
                val fragment = fragmentManager?.findFragmentById(R.id.frameLayout)
                fragment?.let { it1 -> fragmentManager?.beginTransaction()?.remove(it1)?.commit() }
                frameLayoutPenilaian.visibility = View.GONE
                supportActionBar?.setTitle("Penilaian Kelas "+kelas)
            }
            R.id.menu_formPenilaian -> {
                frameLayoutPenilaian.visibility = View.VISIBLE
                frameLayoutPenilaian.setBackgroundColor(Color.argb(255,255,255,255))
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutPenilaian, PenilaianFormFragment()).commit()
                supportActionBar?.setTitle("Form Penilaian Kelas "+kelas)
            }
        }
        return true
    }
}