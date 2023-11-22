package com.example.ths_pondok.CatatanHafalan

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ths_pondok.R
import com.example.ths_pondok.UrlClass
import kotlinx.android.synthetic.main.activity_hafalan_form.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class HafalanInsertActivity : AppCompatActivity() {

    lateinit var urlClass: UrlClass

    var tahun = 0
    var bulan = 0
    var hari = 0

    val sugroSp = arrayOf("-- Pilih Murojaah Sughro --","Al-Fatihah","An-Nas","Al-Falaq","Al-Ikhlas","Al-Lahab","An-Nasr","Al-Kafirun")
    val kubroSp = arrayOf("-- Pilih Murojaah Kubro --","Al-Baqarah","Ali'Imron","Al-Nisa'","Al-Maidah","Al-An'Am","Al-A'raaf")
    lateinit var adpSugro : ArrayAdapter<String>
    lateinit var adpKubro : ArrayAdapter<String>
    var pilihSugro = ""
    var pilihKubro = ""

    lateinit var namaAdapter: ArrayAdapter<String>
    val daftarNama = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hafalan_form)
        supportActionBar?.setTitle("Form Catatan Hafalan")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        urlClass = UrlClass()

        val kalender : Calendar = Calendar.getInstance()

        tahun = kalender.get(Calendar.YEAR)
        bulan = kalender.get(Calendar.MONTH) + 1
        hari = kalender.get(Calendar.DAY_OF_MONTH)

        adpSugro = ArrayAdapter(this, android.R.layout.simple_list_item_1, sugroSp)
        adpKubro = ArrayAdapter(this, android.R.layout.simple_list_item_1, kubroSp)
        spSughroHafalan.adapter = adpSugro
        spSughroHafalan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent : AdapterView<*>?) {
                pilihSugro = ""
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedValue = parent.getItemAtPosition(position).toString()
                if (selectedValue.equals("-- Pilih Murojaah Sughro --")) {
                    pilihSugro = ""
                } else {
                    pilihSugro = selectedValue
                }
            }
        }
        spKubroHafalan.adapter = adpKubro
        spKubroHafalan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent : AdapterView<*>?) {
                pilihKubro = ""
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedValue = parent.getItemAtPosition(position).toString()
                if (selectedValue.equals("-- Pilih Murojaah Kubro --")) {
                    pilihKubro = ""
                } else {
                    pilihKubro = selectedValue
                }
            }
        }

        namaAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,daftarNama)
        actvNamaSantriHafalan.setAdapter(namaAdapter)
        actvNamaSantriHafalan.threshold = 1
        actvNamaSantriHafalan.setOnItemClickListener { parent, view, position, id ->
            form.visibility = View.VISIBLE
            actvNamaSantriHafalan.isEnabled = false
            info.visibility = View.GONE
            Toast.makeText(this, "Berhasil Memilih Nama!", Toast.LENGTH_SHORT).show()
        }

        btnTgl.setOnClickListener {
            showDialog(10)
        }

        btnKirimHafalan.setOnClickListener {
            insertHafalan("insert")
            onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun insertHafalan(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlHafalan,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(this, "Berhasil Menambahkan Catatan Hafalan!", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(this, "Gagal Menambahkan Catatan Hafalan!", Toast.LENGTH_LONG)
                        .show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                when(mode){
                    "insert"->{
                        hm.put("mode","insert")
                        hm.put("nama_santri", actvNamaSantriHafalan.text.toString())
                        hm.put("tanggal_hafalan", etTglHafalan.text.toString())
                        hm.put("pelajaran_baru", etPelajaranHafalan.text.toString())
                        hm.put("murojaah_sughro", pilihSugro)
                        hm.put("murojaah_kubro", pilihKubro)
                        hm.put("persiapan", etPersiapanHafalan.text.toString())
                        hm.put("catatan", etCatatanHafalan.text.toString())
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    var ubahTanggalDialog = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        etTglHafalan.setText("$year-${month+1}-$dayOfMonth")
        tahun = year
        bulan = month+1
        hari = dayOfMonth
    }

    override fun onCreateDialog(id: Int): Dialog {
        when(id){
            10 -> return DatePickerDialog(this, ubahTanggalDialog, tahun, bulan-1, hari)
        }
        return super.onCreateDialog(id)
    }

    override fun onStart() {
        super.onStart()
        getNama("get_nama")
        form.visibility = View.GONE
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
                    "get_nama" -> {
                        hm.put("mode", "get_nama")
                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}