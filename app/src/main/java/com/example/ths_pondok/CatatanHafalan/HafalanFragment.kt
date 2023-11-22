package com.example.ths_pondok.CatatanHafalan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ths_pondok.R
import kotlinx.android.synthetic.main.fragment_hafalan.view.*

class HafalanFragment : Fragment() {
    lateinit var v : View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_hafalan, container, false)

        v.btnKelasA.setOnClickListener {
            val intent = Intent(v.context, HafalanPerkelasActivity::class.java)
            intent.putExtra("kelas", "A")
            startActivity(intent)
        }

        v.btnKelasB.setOnClickListener {
            val intent = Intent(v.context, HafalanPerkelasActivity::class.java)
            intent.putExtra("kelas", "B")
            startActivity(intent)
        }

        v.btnKelasC.setOnClickListener {
            val intent = Intent(v.context, HafalanPerkelasActivity::class.java)
            intent.putExtra("kelas", "C")
            startActivity(intent)
        }

        v.btnKelasD.setOnClickListener {
            val intent = Intent(v.context, HafalanPerkelasActivity::class.java)
            intent.putExtra("kelas", "D")
            startActivity(intent)
        }

        v.btnTambahCatatan.setOnClickListener {
            startActivity(Intent(it.context, HafalanInsertActivity::class.java))
        }

        return v
    }
}