package com.example.ths_pondok.Penilaian

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ths_pondok.R
import kotlinx.android.synthetic.main.fragment_penilaian.view.*

class PenilaianFragment : Fragment() {
    lateinit var v : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_penilaian, container, false)

        v.btnPenilaianKelasA.setOnClickListener {
            val intent = Intent(v.context, PenilaianPerkelasActivity::class.java)
            intent.putExtra("kelas", "A")
            startActivity(intent)
        }

        v.btnPenilaianKelasB.setOnClickListener {
            val intent = Intent(v.context, PenilaianPerkelasActivity::class.java)
            intent.putExtra("kelas", "B")
            startActivity(intent)
        }

        v.btnPenilaianKelasC.setOnClickListener {
            val intent = Intent(v.context, PenilaianPerkelasActivity::class.java)
            intent.putExtra("kelas", "C")
            startActivity(intent)
        }

        v.btnPenilaianKelasD.setOnClickListener {
            val intent = Intent(v.context, PenilaianPerkelasActivity::class.java)
            intent.putExtra("kelas", "D")
            startActivity(intent)
        }

        return v
    }
}