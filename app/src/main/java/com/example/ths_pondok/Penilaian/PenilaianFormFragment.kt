package com.example.ths_pondok.Penilaian

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ths_pondok.R
import com.example.ths_pondok.UrlClass
import kotlinx.android.synthetic.main.fragment_penilaian_form.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class PenilaianFormFragment : Fragment() {

    lateinit var thisParent : PenilaianPersantriActivity
    lateinit var v : View

    lateinit var urlClass: UrlClass

    lateinit var namaAdapter: ArrayAdapter<String>
    val daftarNama = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as PenilaianPersantriActivity
        v = inflater.inflate(R.layout.fragment_penilaian_form, container, false)

        v.etNilaiMaks.setText("100")

        urlClass = UrlClass()

        namaAdapter = ArrayAdapter(v.context, android.R.layout.simple_list_item_1,daftarNama)
        v.actvNamaSantriPenilaian.setAdapter(namaAdapter)
        v.actvNamaSantriPenilaian.threshold = 1
        v.actvNamaSantriPenilaian.setOnItemClickListener { parent, view, position, id ->
            v.actvNamaSantriPenilaian.isEnabled = false
        }

        v.btnJumlahMaks.setOnClickListener {
            val hasil1 = (v.etJumlahAyat.text.toString().toInt() - v.etJumlahSalah.text.toString().toInt()) * 100 / v.etJumlahAyat.text.toString().toInt()
            v.etNilaiPersurat.setText(hasil1.toString())
            val nilaiSalah = 100 - v.etNilaiPersurat.text.toString().toInt()
            v.etNilaiSalah.setText(nilaiSalah.toString())
        }

        v.btnSimpanPenilaian.setOnClickListener {
            insertPenilaian("insert")
            requireActivity().recreate()
            Toast.makeText(v.context, "Berhasil Menambahkan Penilaian Santri!", Toast.LENGTH_LONG)
            .show()
        }

        return v
    }

    private fun insertPenilaian(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlPenilaian,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

//                if (respon.equals("1")) {
//                    Toast.makeText(this.context, "Berhasil Menambahkan Penilaian Santri!", Toast.LENGTH_LONG)
//                        .show()
//                } else {
//                    Toast.makeText(this.context, "Gagal Menambahkan Penilaian Santri!", Toast.LENGTH_LONG)
//                        .show()
//                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this.context,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                when(mode){
                    "insert"->{
                        hm.put("mode","insert")
                        hm.put("nama_santri", v.actvNamaSantriPenilaian.text.toString())
                        hm.put("jumlah_ayat", v.etJumlahAyat.text.toString())
                        hm.put("jumlah_salah", v.etJumlahSalah.text.toString())
                        hm.put("nilai_salah", v.etNilaiSalah.text.toString())
                        hm.put("nilai_persurat", v.etNilaiPersurat.text.toString())
                        hm.put("keterangan", v.etKeterangan.text.toString())
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    override fun onStart() {
        super.onStart()
        getNama("get_nama_perkelas")
    }

    private fun getNama(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlSantri,
            Response.Listener { response ->
                daftarNama.clear()
                val jsonArray = JSONArray(response)
                for (x in 0..(jsonArray.length()-1)){
                    val jsonObject = jsonArray.getJSONObject(x)
                    daftarNama.add(jsonObject.getString("nama_santri"))
                }
                namaAdapter.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->

            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode) {
                    "get_nama_perkelas" -> {
                        hm.put("mode", "get_nama_perkelas")
                        hm.put("kelas", thisParent.kelas)
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }
}