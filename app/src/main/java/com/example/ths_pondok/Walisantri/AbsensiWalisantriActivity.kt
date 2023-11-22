package com.example.ths_pondok.Walisantri

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ths_pondok.Adapter.AdapterAbsensiWalisantri
import com.example.ths_pondok.R
import com.example.ths_pondok.UrlClass
import kotlinx.android.synthetic.main.activity_absensi_walisantri.absNama
import kotlinx.android.synthetic.main.activity_absensi_walisantri.rvAbsensi
import org.json.JSONArray

class AbsensiWalisantriActivity : AppCompatActivity() {

    val daftarAbsensi = mutableListOf<HashMap<String,String>>()
    lateinit var absensiAdapter: AdapterAbsensiWalisantri

    lateinit var urlClass: UrlClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absensi_walisantri)
        supportActionBar?.setTitle("Absensi Santri Anda")

        urlClass = UrlClass()

        var paket : Bundle? = intent.extras
        absNama.setText(paket?.getString("nama").toString())

        absensiAdapter = AdapterAbsensiWalisantri(daftarAbsensi)
        rvAbsensi.layoutManager = LinearLayoutManager(this)
        rvAbsensi.adapter = absensiAdapter

    }

    override fun onStart() {
        super.onStart()
        loadData("")
    }

    private fun loadData(tgl: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlAbsensi,
            Response.Listener { response ->
                daftarAbsensi.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var frm = HashMap<String,String>()
                        frm.put("tanggal_absensi",jsonObject.getString("tanggal_absensi"))
                        frm.put("status_hadir",jsonObject.getString("status_hadir"))

                        daftarAbsensi.add(frm)
                    }
                    absensiAdapter.notifyDataSetChanged()
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
                hm.put("mode","load_absen_wali")
                hm.put("tgl", tgl)
                hm.put("nis", paket?.getString("nis").toString())

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}