package com.example.ths_pondok.Walisantri

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ths_pondok.Adapter.AdapterHafalan
import com.example.ths_pondok.R
import com.example.ths_pondok.UrlClass
import kotlinx.android.synthetic.main.activity_hafalan_perkelas.btnCari
import kotlinx.android.synthetic.main.activity_hafalan_perkelas.input
import kotlinx.android.synthetic.main.activity_hafalan_perkelas.rvHafalan
import org.json.JSONArray

class HafalanWalisantriActivity : AppCompatActivity() {
    val daftarHafalan = mutableListOf<HashMap<String,String>>()
    lateinit var hafalanAdapter: AdapterHafalan

    lateinit var urlClass: UrlClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hafalan_perkelas)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Catatan Hafalan")

        urlClass = UrlClass()

        hafalanAdapter = AdapterHafalan(daftarHafalan)
        rvHafalan.layoutManager = LinearLayoutManager(this)
        rvHafalan.adapter = hafalanAdapter

        input.visibility = View.GONE
        btnCari.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        showDataHafalan("show_data_hafalan")
    }

    private fun showDataHafalan(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlWalisantri,
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
                var paket : Bundle? = intent.extras
                when(mode){
                    "show_data_hafalan" -> {
                        hm.put("mode","show_data_hafalan")
                        hm.put("nis", paket?.getString("nis").toString())
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}