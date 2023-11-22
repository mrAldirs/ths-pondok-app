package com.example.ths_pondok.Absensi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_absensi_detail.*
import org.json.JSONArray

class AbsensiDetailActivity : AppCompatActivity() {
    val daftarAbsensi = mutableListOf<HashMap<String,String>>()
    lateinit var absensiAdapter: AdapterAbsensi

    lateinit var urlClass: UrlClass
    var kelas = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absensi_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()

        var paket : Bundle? = intent.extras
        kelas = paket?.getString("kelas").toString()
        tvTanggalAbsensiDetail.setText(paket?.getString("tanggal").toString())

        absensiAdapter = AdapterAbsensi(daftarAbsensi, this)
        rvAbsensiDetail.layoutManager = LinearLayoutManager(this)
        rvAbsensiDetail.adapter = absensiAdapter

        btnCariAbsen.setOnClickListener {
            showDataAbsensi("show_detail_absensi", etCariAbsen.text.toString().trim())
        }

        supportActionBar?.setTitle("Kelas "+kelas+" Tanggal "+tvTanggalAbsensiDetail.text.toString())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        showDataAbsensi("show_detail_absensi", "")
    }

    private fun showDataAbsensi(mode: String, nama_santri: String) {
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
                        frm.put("nama_santri",jsonObject.getString("nama_santri"))
                        frm.put("status_hadir",jsonObject.getString("status_hadir"))
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
                hm.put("nama_santri", nama_santri)
                when(mode){
                    "show_detail_absensi" -> {
                        hm.put("mode","show_detail_absensi")
                        hm.put("kelas",kelas)
                        hm.put("tanggal_absensi",tvTanggalAbsensiDetail.text.toString())
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    class AdapterAbsensi(val daftarAbsensi: List<HashMap<String,String>>, val thisParent : AbsensiDetailActivity)
        : RecyclerView.Adapter<AdapterAbsensi.HolderDataAbsensi>(){
        class HolderDataAbsensi(v: View) : RecyclerView.ViewHolder(v) {
            val nama = v.findViewById<TextView>(R.id.adpNamaAbsensi)
            val nis = v.findViewById<TextView>(R.id.adpNisAbsensi)
            val card = v.findViewById<CardView>(R.id.card)
            val izinStatus = v.findViewById<CircleImageView>(R.id.absensiIzin)
            val sakitStatus = v.findViewById<CircleImageView>(R.id.absensiSakit)
            val hadirStatus = v.findViewById<CircleImageView>(R.id.absensiHadir)
            val tidakStatus = v.findViewById<CircleImageView>(R.id.absensiAlpha)
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

            val status = data.get("status_hadir")
            if (status.toString().equals("Hadir")) {
                Picasso.get().load(android.R.drawable.checkbox_on_background).into(holder.hadirStatus)
            } else if (status.toString().equals("Izin")) {
                Picasso.get().load(android.R.drawable.checkbox_on_background).into(holder.izinStatus)
            } else if (status.toString().equals("Sakit")) {
                Picasso.get().load(android.R.drawable.checkbox_on_background).into(holder.sakitStatus)
            } else {
                Picasso.get().load(android.R.drawable.checkbox_on_background).into(holder.tidakStatus)
            }

            holder.card.setOnClickListener {
                var editMenu = PopupMenu(it.context, it)
                editMenu.menuInflater.inflate(R.menu.menu_edit, editMenu.menu)
                editMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_edit -> {
                            var dialog = AbsensiEditFragment()
                            val bundle = Bundle()

                            bundle.putString("kode", data.get("id_absensi"))
                            bundle.putString("nama", data.get("nama_santri"))
                            bundle.putString("kelas", thisParent.kelas)
                            bundle.putString("tanggal", thisParent.tvTanggalAbsensiDetail.text.toString())
                            dialog.show(thisParent.supportFragmentManager, "DialogEdit")
                            dialog.arguments = bundle
                        }
                    }
                    false
                }
                editMenu.show()
            }
        }
    }
}