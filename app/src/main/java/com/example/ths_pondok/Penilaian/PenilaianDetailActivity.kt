package com.example.ths_pondok.Penilaian

import android.content.Context
import android.content.DialogInterface
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
import kotlinx.android.synthetic.main.activity_penilaian_detail.*
import kotlinx.android.synthetic.main.fragment_penilaian_form.view.etJumlahAyat
import kotlinx.android.synthetic.main.fragment_penilaian_form.view.etJumlahSalah
import kotlinx.android.synthetic.main.fragment_penilaian_form.view.etNilaiPersurat
import kotlinx.android.synthetic.main.fragment_penilaian_form.view.etNilaiSalah
import org.json.JSONObject
import java.util.HashMap

class PenilaianDetailActivity : AppCompatActivity() {

    lateinit var urlClass: UrlClass
    var kode = ""
    lateinit var preferences: SharedPreferences
    val PREF_NAME = "akun"
    val LEVEL = "level"
    val DEF_LEVEL = ""
    var lvl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penilaian_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        lvl = preferences.getString(LEVEL, DEF_LEVEL).toString()
        if (lvl.equals("Wali")) {
            btnHapusPenilaianDetail.visibility = View.GONE
            btnEditPenilaianDetail.visibility = View.GONE
        } else {
            btnHapusPenilaianDetail.visibility = View.VISIBLE
            btnEditPenilaianDetail.visibility = View.VISIBLE
        }

        var paket : Bundle? = intent.extras
        kode = paket?.getString("kode").toString()
        detailIdPenilaian.setText("Kode : "+paket?.getString("kode").toString())

        urlClass = UrlClass()

        btnEditPenilaianDetail.setOnClickListener {
            AlertDialog.Builder(this)
            .setTitle("Warning!")
            .setIcon(R.drawable.info)
            .setMessage("Apakah anda ingin mengedit penilaian ini?")
            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                detailPenilaianAyat.isEnabled = true
                detailPenilaianSalah.isEnabled = true
                detailPenilaianKeterangan.isEnabled = true
                btnEditPenilaianDetail.visibility = View.GONE
                btnHapusPenilaianDetail.visibility = View.GONE
                btnSimpanEditPenilaian.visibility = View.VISIBLE
                btnDetailJumlah.visibility = View.VISIBLE
                detailPenilaianAyat.requestFocus()
            })
            .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

            })
            .show()
        }

        btnDetailJumlah.setOnClickListener {
            val hasil1 = (detailPenilaianAyat.text.toString().toInt() - detailPenilaianSalah.text.toString().toInt()) * 100 / detailPenilaianAyat.text.toString().toInt()
            detailPenilaianNilaiPersurat.setText(hasil1.toString())
            val nilaiSalah = 100 - detailPenilaianNilaiPersurat.text.toString().toInt()
            detailPenilaianNilaiSalah.setText(nilaiSalah.toString())
        }

        btnSimpanEditPenilaian.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Warning!")
                .setIcon(R.drawable.info)
                .setMessage("Apakah anda ingin menyimpan perubahan ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    crudPenilaian("edit_penilaian")
                    Toast.makeText(this, "Berhasil Mengedit Penilaian!", Toast.LENGTH_LONG)
                        .show()
                    recreate()
                    detailPenilaianAyat.isEnabled = false
                    detailPenilaianSalah.isEnabled = false
                    detailPenilaianKeterangan.isEnabled = false
                    btnEditPenilaianDetail.visibility = View.VISIBLE
                    btnHapusPenilaianDetail.visibility = View.VISIBLE
                    btnSimpanEditPenilaian.visibility = View.GONE
                    btnDetailJumlah.visibility = View.GONE
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
        }

        btnHapusPenilaianDetail.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Warning!")
                .setIcon(R.drawable.info)
                .setMessage("Apakah anda ingin menghapus penilaian ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    crudPenilaian("delete_penilaian")
                    Toast.makeText(this, "Berhasil Menghapus Penilaian!", Toast.LENGTH_LONG)
                        .show()
                    onBackPressed()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
        }
    }

    fun crudPenilaian(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlPenilaian,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

//                if (respon.equals("1")) {
//                    Toast.makeText(this, "Berhasil Mengedit Penilaian!", Toast.LENGTH_LONG)
//                        .show()
//                } else {
//                    Toast.makeText(this, "Gagal Mengedit Penilaian!", Toast.LENGTH_LONG)
//                        .show()
//                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                when(mode){
                    "edit_penilaian"->{
                        hm.put("mode", "edit")
                        hm.put("id_nilai", kode)
                        hm.put("jumlah_ayat", detailPenilaianAyat.text.toString())
                        hm.put("jumlah_salah", detailPenilaianSalah.text.toString())
                        hm.put("nilai_salah", detailPenilaianNilaiSalah.text.toString())
                        hm.put("nilai_persurat", detailPenilaianNilaiPersurat.text.toString())
                        hm.put("keterangan", detailPenilaianKeterangan.text.toString())
                    }
                    "delete_penilaian" -> {
                        hm.put("mode", "delete")
                        hm.put("id_nilai", kode)
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
        showDetailPenilaian("show_detail_penilaian")
    }

    private fun showDetailPenilaian(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlPenilaian,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val namaSantri = jsonObject.getString("nama_santri")
                val nis = jsonObject.getString("nis")
                val kelas = jsonObject.getString("kelas")
                val jAyat = jsonObject.getString("jumlah_ayat")
                val jSalah = jsonObject.getString("jumlah_salah")
                val nMaks = jsonObject.getString("nilai_maks")
                val nSalah = jsonObject.getString("nilai_salah")
                val nSurat = jsonObject.getString("nilai_persurat")
                val ket = jsonObject.getString("keterangan")

                supportActionBar?.setTitle("Detail Nilai "+namaSantri)
                detailPenilaianNama.setText(namaSantri)
                detailPenilaianNis.setText(nis)
                detailPenilaianKelas.setText(kelas)
                detailPenilaianAyat.setText(jAyat)
                detailPenilaianSalah.setText(jSalah)
                detailPenilaianNilaiMaks.setText(nMaks)
                detailPenilaianNilaiSalah.setText(nSalah)
                detailPenilaianNilaiPersurat.setText(nSurat)
                detailPenilaianKeterangan.setText(ket)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = java.util.HashMap<String, String>()
                var paket : Bundle? = intent.extras
                kode = paket?.getString("kode").toString()
                when(mode) {
                    "show_detail_penilaian" -> {
                        hm.put("mode", "show_detail_penilaian")
                        hm.put("id_nilai", kode)
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}