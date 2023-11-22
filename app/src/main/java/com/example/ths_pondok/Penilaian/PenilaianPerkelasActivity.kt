package com.example.ths_pondok.Penilaian

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ths_pondok.Adapter.AdapterSantriPenilaian
import com.example.ths_pondok.R
import com.example.ths_pondok.UrlClass
import kotlinx.android.synthetic.main.activity_hafalan_perkelas.*
import org.json.JSONArray

class PenilaianPerkelasActivity : AppCompatActivity() {

    val daftarSantri = mutableListOf<HashMap<String,String>>()
    lateinit var santriAdapter : AdapterSantriPenilaian

    lateinit var urlClass: UrlClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hafalan_perkelas)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()

        santriAdapter = AdapterSantriPenilaian(daftarSantri)
        rvHafalan.layoutManager = LinearLayoutManager(this)
        rvHafalan.adapter = santriAdapter

        btnCari.setOnClickListener {
            showDataSantri(etCari.text.toString().trim())
        }

        supportActionBar?.setTitle("Daftar Santri kelas "+intent.getStringExtra("kelas").toString())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        showDataSantri("")
    }

    private fun showDataSantri(nama: String) {
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
                hm.put("mode", "show_santri_perkelas")
                hm.put("kelas", intent.getStringExtra("kelas").toString())
                hm.put("nama_santri", nama)

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}