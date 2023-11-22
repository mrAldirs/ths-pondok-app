package com.example.ths_pondok.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ths_pondok.Admin.FragmentDetailPengurus
import com.example.ths_pondok.Admin.MasterPengurusActivity
import com.example.ths_pondok.Admin.Model.Pengurus
import com.example.ths_pondok.R

class AdapterPengurus(private var dataList: List<Pengurus>, private val supportFragmentManager: FragmentManager)
    : RecyclerView.Adapter<AdapterPengurus.HolderDataTransaksi>() {
    class HolderDataTransaksi(v: View) : RecyclerView.ViewHolder(v) {
        val nama = v.findViewById<TextView>(R.id.adpNamaPengurus)
        val telp = v.findViewById<TextView>(R.id.adpNoTelpPengurus)
        val card = v.findViewById<CardView>(R.id.cardPengurus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataTransaksi {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_pengurus, parent, false)
        return HolderDataTransaksi(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HolderDataTransaksi, position: Int) {
        val data = dataList.get(position)

        holder.nama.setText(data.nama_pengguna)
        holder.telp.setText(data.no_telp)

        holder.card.setOnClickListener {
            val frag = FragmentDetailPengurus()

            val bundle = Bundle()
            bundle.putString("kode", data.username)
            frag.arguments = bundle

            frag.show(supportFragmentManager, "")
        }
    }

    fun setData(newDataList: List<Pengurus>) {
        dataList = newDataList
        notifyDataSetChanged()
    }
}