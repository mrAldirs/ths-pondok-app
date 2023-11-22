package com.example.ths_pondok.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ths_pondok.Penilaian.PenilaianDetailActivity
import com.example.ths_pondok.R

class AdapterPenilaian(val daftarPenilaian: List<HashMap<String, String>>)
    : RecyclerView.Adapter<AdapterPenilaian.HolderDataPenilaian>(){
    class HolderDataPenilaian(v: View) : RecyclerView.ViewHolder(v) {
        val nama = v.findViewById<TextView>(R.id.adpNamaPenilaian)
        val nilaiSurat = v.findViewById<TextView>(R.id.adpNilaiPersurat)
        val salahSurat = v.findViewById<TextView>(R.id.adpNilaiSalah)
        val cardd = v.findViewById<LinearLayout>(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataPenilaian {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_penilaian, parent, false)
        return HolderDataPenilaian(v)
    }

    override fun getItemCount(): Int {
        return daftarPenilaian.size
    }

    override fun onBindViewHolder(holder: HolderDataPenilaian, position: Int) {
        val data = daftarPenilaian.get(position)
        holder.nama.setText(data.get("nama_santri"))
        holder.nilaiSurat.setText("Nilai Total Persurat : "+data.get("nilai_persurat"))
        holder.salahSurat.setText("Nilai Total Salah : "+data.get("nilai_salah"))

        holder.cardd.setOnClickListener {
            val intent = Intent(it.context, PenilaianDetailActivity::class.java)
            intent.putExtra("kode", data.get("id_nilai"))
            it.context.startActivity(intent)
        }
    }
}