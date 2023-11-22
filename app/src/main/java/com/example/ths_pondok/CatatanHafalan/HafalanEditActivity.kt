package com.example.ths_pondok.CatatanHafalan

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ths_pondok.R
import com.example.ths_pondok.UrlClass
import kotlinx.android.synthetic.main.activity_hafalan_form.*
import org.json.JSONObject
import java.util.*

class HafalanEditActivity : AppCompatActivity() {

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

    var kode = ""
    var ms = ""
    var mk = ""

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

        actvNamaSantriHafalan.isEnabled = false

        adpSugro = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sugroSp)
        adpKubro = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, kubroSp)
        spSughroHafalan.adapter = adpSugro
        spKubroHafalan.adapter = adpKubro
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

        var paket : Bundle? = intent.extras
        kode = paket?.getString("kode").toString()

        btnTgl.setOnClickListener {
            showDialog(10)
        }

        info.visibility = View.GONE
        btnKirimHafalan.setText("Edit Catatan")

        btnKirimHafalan.setOnClickListener {
            editHafalan("edit")
            onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        showDetailHafalan("show_detail_hafalan")
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

    private fun showDetailHafalan(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlHafalan,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val namaSantri = jsonObject.getString("nama_santri")
                val pelajaran_baru = jsonObject.getString("pelajaran_baru")
                val murojaahSughro = jsonObject.getString("murojaah_sughro")
                val murojaahKubro = jsonObject.getString("murojaah_kubro")
                val persiapan = jsonObject.getString("persiapan")
                val catatan = jsonObject.getString("catatan")
                val tgl = jsonObject.getString("tanggal_hafalan")

                ms = murojaahSughro
                mk = murojaahKubro

                val spinnerPositionS = adpSugro.getPosition(ms)
                spSughroHafalan.setSelection(spinnerPositionS)

                val spinnerPositionK = adpKubro.getPosition(mk)
                spKubroHafalan.setSelection(spinnerPositionK)

                etTglHafalan.setText(tgl)
                actvNamaSantriHafalan.setText(namaSantri)
                etPelajaranHafalan.setText(pelajaran_baru)
                etPersiapanHafalan.setText(persiapan)
                etCatatanHafalan.setText(catatan)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = java.util.HashMap<String, String>()
                var paket : Bundle? = intent.extras
                kode = paket?.getString("kode").toString()
                when(mode) {
                    "show_detail_hafalan" -> {
                        hm.put("mode", "show_detail_hafalan")
                        hm.put("id_hafalan", kode)
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun editHafalan(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.urlHafalan,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")

                if (respon.equals("1")) {
                    Toast.makeText(this, "Berhasil Mengedit Catatan Hafalan!", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(this, "Gagal Mengedit Catatan Hafalan!", Toast.LENGTH_LONG)
                        .show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                var paket : Bundle? = intent.extras
                when(mode){
                    "edit"->{
                        hm.put("mode","edit")
                        hm.put("id_hafalan", paket?.getString("kode").toString())
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
}