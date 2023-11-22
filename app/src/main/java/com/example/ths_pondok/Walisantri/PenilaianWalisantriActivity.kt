package com.example.ths_pondok.Walisantri

import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_penilaian_perkelas.*
import org.json.JSONArray

class PenilaianWalisantriActivity : AppCompatActivity() {
    lateinit var urlClass: UrlClass

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

        supportActionBar?.setTitle("Raport Penilaian Santri")

        input.visibility = View.GONE
        btnCari.visibility = View.GONE

        bottomNavigationView.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        showDataPenilaian("show_data_penilaian")
    }

    private fun showDataPenilaian(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlWalisantri,
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
                var paket : Bundle? = intent.extras
                when(mode){
                    "show_data_penilaian" -> {
                        hm.put("mode","show_data_penilaian")
                        hm.put("nis",paket?.getString("nis").toString())
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}