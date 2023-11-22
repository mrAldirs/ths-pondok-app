package com.example.ths_pondok.Absensi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ths_pondok.R
import kotlinx.android.synthetic.main.fragment_absensi.view.*

class AbsensiFragment : Fragment() {
    lateinit var v : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_absensi, container, false)

        v.btnAbsenKelasA.setOnClickListener {
            val intent = Intent(v.context, AbsensiPerkelasActivity::class.java)
            intent.putExtra("kelas", "A")
            startActivity(intent)
        }

        v.btnAbsenKelasB.setOnClickListener {
            val intent = Intent(v.context, AbsensiPerkelasActivity::class.java)
            intent.putExtra("kelas", "B")
            startActivity(intent)
        }

        v.btnAbsenKelasC.setOnClickListener {
            val intent = Intent(v.context, AbsensiPerkelasActivity::class.java)
            intent.putExtra("kelas", "C")
            startActivity(intent)
        }

        v.btnAbsenKelasD.setOnClickListener {
            val intent = Intent(v.context, AbsensiPerkelasActivity::class.java)
            intent.putExtra("kelas", "D")
            startActivity(intent)
        }

        return v
    }
}