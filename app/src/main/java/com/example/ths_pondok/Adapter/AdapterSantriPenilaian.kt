package com.example.ths_pondok.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ths_pondok.CatatanHafalan.HafalanDetailActivity
import com.example.ths_pondok.CatatanHafalan.HafalanPersantriActivity
import com.example.ths_pondok.Penilaian.PenilaianPersantriActivity
import com.example.ths_pondok.R

class AdapterSantriPenilaian(val daftarSantri: List<HashMap<String,String>>)
    : RecyclerView.Adapter<AdapterSantriPenilaian.HolderDataSantri>(){
    class HolderDataSantri(v: View) : RecyclerView.ViewHolder(v) {
        val nama = v.findViewById<TextView>(R.id.adpNamaMain)
        val nis = v.findViewById<TextView>(R.id.adpNisMain)
        val kelas = v.findViewById<TextView>(R.id.adpKelasMain)
        val cd = v.findViewById<CardView>(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataSantri {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_santri, parent, false)
        return HolderDataSantri(v)
    }

    override fun getItemCount(): Int {
        return daftarSantri.size
    }

    override fun onBindViewHolder(holder: HolderDataSantri, position: Int) {
        val data = daftarSantri.get(position)
        holder.nama.setText(data.get("nama_santri"))
        holder.nis.setText("NIS "+data.get("nis"))
        holder.kelas.setText("Kelas : "+data.get("kelas"))

        holder.cd.setOnClickListener {
            val intent = Intent(it.context, PenilaianPersantriActivity::class.java)
            intent.putExtra("nis", data.get("nis").toString())
            it.context.startActivity(intent)
        }
    }
}