package com.example.ths_pondok.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ths_pondok.R

class AdapterSantri(val daftarSantri: List<HashMap<String,String>>)
    : RecyclerView.Adapter<AdapterSantri.HolderDataSantri>(){
    class HolderDataSantri(v: View) : RecyclerView.ViewHolder(v) {
        val nama = v.findViewById<TextView>(R.id.adpNamaMain)
        val nis = v.findViewById<TextView>(R.id.adpNisMain)
        val kelas = v.findViewById<TextView>(R.id.adpKelasMain)
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
    }
}