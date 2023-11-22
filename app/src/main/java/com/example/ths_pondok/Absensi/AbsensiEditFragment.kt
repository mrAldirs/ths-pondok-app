package com.example.ths_pondok.Absensi

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ths_pondok.R
import com.example.ths_pondok.UrlClass
import kotlinx.android.synthetic.main.activity_absensi_insert.*
import kotlinx.android.synthetic.main.fragment_absensi_edit.view.*
import org.json.JSONObject
import java.util.HashMap

class AbsensiEditFragment : DialogFragment() {
    lateinit var v: View
    var kode = ""
    var tgl = ""
    var kls = ""

    lateinit var urlClass: UrlClass

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_absensi_edit, container, false)

        v.txNamaSantriAbsen.setText(arguments?.getString("nama").toString())
        kode = arguments?.getString("kode").toString()
        tgl = arguments?.getString("tanggal").toString()
        kls = arguments?.getString("kelas").toString()

        urlClass = UrlClass()

        v.btnEditHadir.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setTitle("Warning!")
                .setIcon(R.drawable.info)
                .setMessage("Apakah anda ingin mengedit absensi kehadiran?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    editAbsensi("edit_absensi_hadir")
                    dismiss()
                    requireActivity().recreate()
                    Toast.makeText(v.context, "Berhasil mengedir Absensi!", Toast.LENGTH_SHORT).show()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
        }

        v.btnEditTidak.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setTitle("Warning!")
                .setIcon(R.drawable.info)
                .setMessage("Apakah anda ingin mengedit absensi kehadiran?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    editAbsensi("edit_absensi_alpha")
                    dismiss()
                    requireActivity().recreate()
                    Toast.makeText(v.context, "Berhasil mengedir Absensi!", Toast.LENGTH_SHORT).show()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
        }

        v.btnEditIzin.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setTitle("Warning!")
                .setIcon(R.drawable.info)
                .setMessage("Apakah anda ingin mengedit absensi kehadiran?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    editAbsensi("edit_absensi_izin")
                    dismiss()
                    requireActivity().recreate()
                    Toast.makeText(v.context, "Berhasil mengedir Absensi!", Toast.LENGTH_SHORT).show()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
        }

        v.btnEditSakit.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setTitle("Warning!")
                .setIcon(R.drawable.info)
                .setMessage("Apakah anda ingin mengedit absensi kehadiran?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    editAbsensi("edit_absensi_sakit")
                    dismiss()
                    requireActivity()
                    Toast.makeText(v.context, "Berhasil mengedir Absensi!", Toast.LENGTH_SHORT).show()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
        }

        return v
    }

    private fun editAbsensi(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlAbsensi,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

//                if (respon.equals("1")) {
//                    Toast.makeText(this.context, "Berhasil Mengedit Absensi!", Toast.LENGTH_LONG)
//                        .show()
//                } else {
//                    Toast.makeText(this.context, "Gagal Mengedit Absensi!", Toast.LENGTH_LONG)
//                        .show()
//                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                when(mode){
                    "edit_absensi_hadir"->{
                        hm.put("mode","edit")
                        hm.put("id_absensi", kode)
                        hm.put("status_hadir", "Hadir")
                    }
                    "edit_absensi_izin"->{
                        hm.put("mode","edit")
                        hm.put("id_absensi", kode)
                        hm.put("status_hadir", "Izin")
                    }
                    "edit_absensi_sakit"->{
                        hm.put("mode","edit")
                        hm.put("id_absensi", kode)
                        hm.put("status_hadir", "Sakit")
                    }
                    "edit_absensi_alpha"->{
                        hm.put("mode","edit")
                        hm.put("id_absensi", kode)
                        hm.put("status_hadir", "Alpha")
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}