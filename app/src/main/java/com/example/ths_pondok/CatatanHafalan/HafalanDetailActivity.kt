package com.example.ths_pondok.CatatanHafalan

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ths_pondok.R
import com.example.ths_pondok.UrlClass
import kotlinx.android.synthetic.main.activity_hafalan_detail.*
import org.json.JSONObject
import java.util.HashMap

class HafalanDetailActivity : AppCompatActivity() {

    lateinit var urlClass: UrlClass
    var kode = ""
    lateinit var preferences: SharedPreferences
    val PREF_NAME = "akun"
    val LEVEL = "level"
    val DEF_LEVEL = ""
    var lvl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hafalan_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var paket : Bundle? = intent.extras
        kode = paket?.getString("kode").toString()

        urlClass = UrlClass()
        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        lvl = preferences.getString(LEVEL, DEF_LEVEL).toString()
        if (lvl.equals("Wali")) {
            btnHapusCatatanDetail.visibility = View.GONE
            btnEditCatatanDetail.visibility = View.GONE
        } else {
            btnHapusCatatanDetail.visibility = View.VISIBLE
            btnEditCatatanDetail.visibility = View.VISIBLE
        }

        btnHapusCatatanDetail.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hapus Hafalan!")
                .setIcon(R.drawable.info)
                .setMessage("Apakah anda yakin ingin menghapus?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    hapusHafalan("delete")
                    Toast.makeText(this, "Berhasil Menghapus Order Bakso!", Toast.LENGTH_LONG)
                        .show()
//                    startActivity(Intent(this, HafalanPerkelasActivity::class.java))
                    onBackPressed()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
        }

        btnEditCatatanDetail.setOnClickListener {
            val intent = Intent(it.context, HafalanEditActivity::class.java)
            intent.putExtra("kode", kode)
            it.context.startActivity(intent)
        }
    }

    private fun hapusHafalan(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlHafalan,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                if (respon.equals("1")) {
                    Toast.makeText(this, "Berhasil Menghapus Catatan Hafalan!", Toast.LENGTH_LONG)
                } else {
                    Toast.makeText(this, "Gagal Menghapus Catatan Hafalan!", Toast.LENGTH_LONG)
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                when(mode){
                    "delete" -> {
                        hm.put("mode","delete")
                        hm.put("id_hafalan", kode)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        showDetailHafalan("show_detail_hafalan")
    }

    private fun showDetailHafalan(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlHafalan,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val namaSantri = jsonObject.getString("nama_santri")
                val nis = jsonObject.getString("nis")
                val kelas = jsonObject.getString("kelas")
                val pelajaran_baru = jsonObject.getString("pelajaran_baru")
                val murojaahSughro = jsonObject.getString("murojaah_sughro")
                val murojaahKubro = jsonObject.getString("murojaah_kubro")
                val persiapan = jsonObject.getString("persiapan")
                val catatan = jsonObject.getString("catatan")
                val tgl = jsonObject.getString("tanggal_hafalan")

                supportActionBar?.setTitle("Detail Hafalan "+namaSantri)

                tvTanggalHafalanDetail.setText(tgl)
                tvDetailNamaHafalan.setText(namaSantri)
                tvDetailNisHafalan.setText(nis)
                tvDetailKelasHafalan.setText(kelas)
                tvDetailPelajaranHafalan.setText(pelajaran_baru)
                tvDetailSughroHafalan.setText(murojaahSughro)
                tvDetailKubroHafalan.setText(murojaahKubro)
                tvDetailPersiapanHafalan.setText(persiapan)
                tvDetailCatatanHafalan.setText(catatan)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = java.util.HashMap<String, String>()
                var paket : Bundle? = intent.extras
                kode = paket?.getString("kode").toString()
                when(mode) {
                    "show_detail_hafalan" -> {
                        hm.put("mode", "show_detail_hafalan")
                        hm.put("id_hafalan", kode)
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}