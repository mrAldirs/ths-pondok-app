package com.example.ths_pondok.Absensi

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ths_pondok.R
import com.example.ths_pondok.UrlClass
import kotlinx.android.synthetic.main.activity_absensi_perkelas.*
import org.json.JSONArray
import org.json.JSONObject

class AbsensiPerkelasActivity : AppCompatActivity() {

    val daftarAbsensi = mutableListOf<HashMap<String,String>>()
    lateinit var absensiAdapter: AdapterAbsensi

    lateinit var urlClass: UrlClass
    var kelas = ""
    var tgl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absensi_perkelas)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()

        var paket : Bundle? = intent.extras
        kelas = paket?.getString("kelas").toString()

        absensiAdapter = AdapterAbsensi(daftarAbsensi, this)
        rvAbsensi.layoutManager = LinearLayoutManager(this)
        rvAbsensi.adapter = absensiAdapter

        supportActionBar?.setTitle("Absensi Kelas "+kelas)

        btnTambahAbsensi.setOnClickListener {
            val intent = Intent(this, AbsensiInsertActivity::class.java)
            intent.putExtra("kelas", kelas)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        showDataAbsensi("show_data_absensi")
    }

    private fun showDataAbsensi(mode: String) {
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
                        frm.put("id_absensi",jsonObject.getString("id_absensi"))
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
                kelas = paket?.getString("kelas").toString()
                when(mode){
                    "show_data_absensi" -> {
                        hm.put("mode","show_data_absensi")
                        hm.put("kelas",kelas)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun deleteAbsensi(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlAbsensi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = java.util.HashMap<String, String>()
                var paket : Bundle? = intent.extras
                when(mode){
                    "delete" -> {
                        hm.put("mode","delete")
                        hm.put("tanggal_absensi", tgl)
                        hm.put("kelas", paket?.getString("kelas").toString())
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    class AdapterAbsensi(val daftarAbsensi: List<HashMap<String,String>>, val thisParent : AbsensiPerkelasActivity)
        : RecyclerView.Adapter<AdapterAbsensi.HolderDataAbsensi>(){
        class HolderDataAbsensi(v: View) : RecyclerView.ViewHolder(v) {
            val tanggal = v.findViewById<TextView>(R.id.btnTanggalAbsensi)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataAbsensi {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.row_absensi_kelas, parent, false)
            return HolderDataAbsensi(v)
        }

        override fun getItemCount(): Int {
            return daftarAbsensi.size
        }

        override fun onBindViewHolder(holder: HolderDataAbsensi, position: Int) {
            val data = daftarAbsensi.get(position)
            holder.tanggal.setText(data.get("tanggal_absensi"))

            holder.tanggal.setOnClickListener {
                val intent = Intent(it.context, AbsensiDetailActivity::class.java)
                intent.putExtra("tanggal", data.get("tanggal_absensi"))
                intent.putExtra("kelas", thisParent.kelas)
                it.context.startActivity(intent)
            }

            holder.tanggal.setOnLongClickListener {
                thisParent.tgl = data.get("tanggal_absensi").toString()

                var hapusMenu = PopupMenu(it.context, it)
                hapusMenu.menuInflater.inflate(R.menu.menu_hapus, hapusMenu.menu)
                hapusMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_hapus -> {
                            AlertDialog.Builder(thisParent)
                                .setIcon(android.R.drawable.stat_sys_warning)
                                .setTitle("Peringatan!")
                                .setMessage("Apakah Anda menghapus data ini?")
                                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                                    thisParent.deleteAbsensi("delete")
                                    Toast.makeText(thisParent, "Berhasil menghapus data!", Toast.LENGTH_SHORT).show()
                                    thisParent.recreate()
                                })
                                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                                })
                                .show()
                            true
                        }
                    }
                    false
                }
                hapusMenu.show()
                true
            }
        }
    }
}