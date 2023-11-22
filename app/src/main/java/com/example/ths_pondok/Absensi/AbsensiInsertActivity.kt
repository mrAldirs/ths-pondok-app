package com.example.ths_pondok.Absensi

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ths_pondok.R
import com.example.ths_pondok.UrlClass
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_absensi_insert.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class AbsensiInsertActivity : AppCompatActivity() {

    val daftarAbsensi = mutableListOf<HashMap<String,String>>()
    lateinit var absensiAdapter: AdapterAbsensi



    lateinit var urlClass: UrlClass

    var tahun = 0
    var bulan = 0
    var hari = 0

    var kelas = ""

    var nisInsert = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absensi_insert)
        supportActionBar?.setTitle("Tambah Absensi Santri")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()

        var paket : Bundle? = intent.extras
        kelas = paket?.getString("kelas").toString()

        val kalender : Calendar = Calendar.getInstance()

        tahun = kalender.get(Calendar.YEAR)
        bulan = kalender.get(Calendar.MONTH) + 1
        hari = kalender.get(Calendar.DAY_OF_MONTH)

        absensiAdapter = AdapterAbsensi(daftarAbsensi, this)
        rvAbsensiInsert.layoutManager = LinearLayoutManager(this)
        rvAbsensiInsert.adapter = absensiAdapter

        btnTglAbsensi.setOnClickListener {
            showDialog(10)
            kolom.visibility = View.VISIBLE
            rvAbsensiInsert.visibility = View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        showDataAbsensi("show_data_insert")
        kolom.visibility = View.GONE
        rvAbsensiInsert.visibility = View.GONE
    }

    private fun insertAbsensi(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlAbsensi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(this, "Berhasil Menambahkan Absensi!", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(this, "Gagal Menambahkan Absensi!", Toast.LENGTH_LONG)
                        .show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                when(mode){
                    "insert_hadir"->{
                        hm.put("mode","insert_hadir")
                        hm.put("nis", nisInsert)
                        hm.put("tanggal_absensi", etTglAbsensi.text.toString())
                    }
                    "insert_sakit"->{
                        hm.put("mode","insert_sakit")
                        hm.put("nis", nisInsert)
                        hm.put("tanggal_absensi", etTglAbsensi.text.toString())
                    }
                    "insert_izin"->{
                        hm.put("mode","insert_izin")
                        hm.put("nis", nisInsert)
                        hm.put("tanggal_absensi", etTglAbsensi.text.toString())
                    }
                    "insert_tidak"->{
                        hm.put("mode","insert_alpha")
                        hm.put("nis", nisInsert)
                        hm.put("tanggal_absensi", etTglAbsensi.text.toString())
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
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
                        frm.put("nama_santri",jsonObject.getString("nama_santri"))
                        frm.put("nis",jsonObject.getString("nis"))


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
                    "show_data_insert" -> {
                        hm.put("mode","show_data_insert")
                        hm.put("kelas",kelas)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    var ubahTanggalDialog = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        etTglAbsensi.setText("$year-${month+1}-$dayOfMonth")
        tahun = year
        bulan = month+1
        hari = dayOfMonth
    }

    override fun onCreateDialog(id: Int): Dialog {
        when(id){
            10 -> return DatePickerDialog(this, ubahTanggalDialog, tahun, bulan-1, hari)
        }
        return super.onCreateDialog(id)
    }

    class AdapterAbsensi(val daftarAbsensi: List<HashMap<String,String>>, val thisParent : AbsensiInsertActivity)
        : RecyclerView.Adapter<AdapterAbsensi.HolderDataAbsensi>(){
        class HolderDataAbsensi(v: View) : RecyclerView.ViewHolder(v) {
            val nama = v.findViewById<TextView>(R.id.adpNamaAbsensi)
            val nis = v.findViewById<TextView>(R.id.adpNisAbsensi)
            val izinStatus = v.findViewById<CircleImageView>(R.id.absensiIzin)
            val sakitStatus = v.findViewById<CircleImageView>(R.id.absensiSakit)
            val hadirStatus = v.findViewById<CircleImageView>(R.id.absensiHadir)
            val alphaStatus = v.findViewById<CircleImageView>(R.id.absensiAlpha)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataAbsensi {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.row_absensi, parent, false)
            return HolderDataAbsensi(v)
        }

        override fun getItemCount(): Int {
            return daftarAbsensi.size
        }

        override fun onBindViewHolder(holder: HolderDataAbsensi, position: Int) {
            val data = daftarAbsensi.get(position)
            holder.nama.setText(data.get("nama_santri"))
            holder.nis.setText(data.get("nis"))

            holder.hadirStatus.setOnClickListener {
                thisParent.nisInsert = data.get("nis").toString()
                thisParent.insertAbsensi("insert_hadir")
                Picasso.get().load(android.R.drawable.checkbox_on_background).into(holder.hadirStatus)
            }

            holder.izinStatus.setOnClickListener {
                thisParent.nisInsert = data.get("nis").toString()
                thisParent.insertAbsensi("insert_izin")
                Picasso.get().load(android.R.drawable.checkbox_on_background).into(holder.izinStatus)
            }

            holder.sakitStatus.setOnClickListener {
                thisParent.nisInsert = data.get("nis").toString()
                thisParent.insertAbsensi("insert_sakit")
                Picasso.get().load(android.R.drawable.checkbox_on_background).into(holder.sakitStatus)
            }

            holder.alphaStatus.setOnClickListener {
                thisParent.nisInsert = data.get("nis").toString()
                thisParent.insertAbsensi("insert_alpha")
                Picasso.get().load(android.R.drawable.checkbox_on_background).into(holder.alphaStatus)
            }
        }
    }
}