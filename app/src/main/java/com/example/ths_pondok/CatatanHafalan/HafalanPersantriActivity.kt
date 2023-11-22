package com.example.ths_pondok.CatatanHafalan

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ths_pondok.Adapter.AdapterHafalan
import com.example.ths_pondok.R
import com.example.ths_pondok.UrlClass
import kotlinx.android.synthetic.main.activity_hafalan_persantri.*
import org.json.JSONArray

class HafalanPersantriActivity : AppCompatActivity() {
    lateinit var urlClass: UrlClass

    val daftarHafalan = mutableListOf<HashMap<String,String>>()
    lateinit var hafalanAdapter: AdapterHafalan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hafalan_persantri)
        supportActionBar?.setTitle("Hafalan Santri")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()

        hafalanAdapter = AdapterHafalan(daftarHafalan)
        rvHafalan.layoutManager = LinearLayoutManager(this)
        rvHafalan.adapter = hafalanAdapter

        btnCari.setOnClickListener {
            showDataHafalan(etCari.text.toString().trim())
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        showDataHafalan("")
    }

    private fun showDataHafalan(pelajaran: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlHafalan,
            Response.Listener { response ->
                daftarHafalan.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var frm = HashMap<String,String>()
                        frm.put("id_hafalan",jsonObject.getString("id_hafalan"))
                        frm.put("nama_santri",jsonObject.getString("nama_santri"))
                        frm.put("nis",jsonObject.getString("nis"))
                        frm.put("tanggal_hafalan",jsonObject.getString("tanggal_hafalan"))
                        frm.put("pelajaran_baru",jsonObject.getString("pelajaran_baru"))


                        daftarHafalan.add(frm)
                    }
                    hafalanAdapter.notifyDataSetChanged()
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
                hm.put("mode","show_data_hafalan")
                hm.put("nis", intent.getStringExtra("nis").toString())
                hm.put("pelajaran_baru", pelajaran)

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}