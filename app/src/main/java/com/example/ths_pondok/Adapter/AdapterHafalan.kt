package com.example.ths_pondok.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ths_pondok.CatatanHafalan.HafalanDetailActivity
import com.example.ths_pondok.R

class AdapterHafalan(val daftarCatatan: List<HashMap<String,String>>)
    : RecyclerView.Adapter<AdapterHafalan.HolderDataCatatan>(){
    class HolderDataCatatan(v: View) : RecyclerView.ViewHolder(v) {
        val nama = v.findViewById<TextView>(R.id.adpNamaHafalan)
        val nis = v.findViewById<TextView>(R.id.adpNisHafalan)
        val pelajaran = v.findViewById<TextView>(R.id.adpPelajaranHafalan)
        val tanggal = v.findViewById<TextView>(R.id.adpTglHafalan)
        val card = v.findViewById<LinearLayout>(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataCatatan {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_hafalan, parent, false)
        return HolderDataCatatan(v)
    }

    override fun getItemCount(): Int {
        return daftarCatatan.size
    }

    override fun onBindViewHolder(holder: HolderDataCatatan, position: Int) {
        val data = daftarCatatan.get(position)
        holder.nama.setText(data.get("nama_santri"))
        holder.nis.setText("NIS : "+data.get("nis"))
        holder.pelajaran.setText("Pelajaran : "+data.get("pelajaran_baru"))
        holder.tanggal.setText(data.get("tanggal_hafalan"))

        holder.card.setOnClickListener {
            val intent = Intent(it.context, HafalanDetailActivity::class.java)
            intent.putExtra("kode", data.get("id_hafalan"))
            it.context.startActivity(intent)
        }
    }
}