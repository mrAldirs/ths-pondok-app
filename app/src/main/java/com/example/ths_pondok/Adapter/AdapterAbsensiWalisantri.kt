package com.example.ths_pondok.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ths_pondok.R

class AdapterAbsensiWalisantri(val list: List<HashMap<String,String>>) :
    RecyclerView.Adapter<AdapterAbsensiWalisantri.HolderDataAbsen>(){
    class HolderDataAbsen (v: View) : RecyclerView.ViewHolder(v) {
        val tgl = v.findViewById<TextView>(R.id.adpTanggalAbsen)
        val ket = v.findViewById<TextView>(R.id.adpKeteranganAbsen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataAbsen {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_absens_wali, parent, false)
        return HolderDataAbsen(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HolderDataAbsen, position: Int) {
        val data = list.get(position)
        holder.tgl.setText(data.get("tanggal_absensi"))
        holder.ket.setText(data.get("status_hadir"))

        if (data.get("status_hadir").toString().equals("Alpha")) {
            holder.ket.setTextColor(Color.RED)
        } else if (data.get("status_hadir").toString().equals("Sakit")) {
            holder.ket.setTextColor(Color.GREEN)
        } else if (data.get("status_hadir").toString().equals("Izin")) {
            holder.ket.setTextColor(Color.BLUE)
        } else {
            holder.ket.setTextColor(Color.BLACK)
        }
    }
}