package com.example.ths_pondok.Admin

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ths_pondok.Admin.ViewModel.PengurusViewModel
import com.example.ths_pondok.R
import kotlinx.android.synthetic.main.fragment_detail_pengurus.view.*

class FragmentDetailPengurus : DialogFragment() {
    private lateinit var pVM: PengurusViewModel
    lateinit var v: View
    lateinit var parent: MasterPengurusActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_detail_pengurus, container, false)
        parent = activity as MasterPengurusActivity

        pVM = ViewModelProvider(this).get(PengurusViewModel::class.java)

        v.btnHapusPengurus.setOnClickListener {
            AlertDialog.Builder(v.context)
                .setTitle("Peringatan!!")
                .setIcon(android.R.drawable.stat_sys_warning)
                .setMessage("Apakah Anda ingin menghapus data ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    delete(arguments?.get("kode").toString())
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                    null
                })
                .show()
        }

        v.btnEditPengurus.setOnClickListener {
            dismiss()
            val intent = Intent(v.context, EditPengurusActivity::class.java)
            intent.putExtra("kode", arguments?.get("kode").toString())
            v.context.startActivity(intent)
        }

        return v
    }

    private fun delete(kd: String) {
        pVM.delete(kd).observe(this, Observer { result ->
            Toast.makeText(v.context, "Berhasil menghapus data!", Toast.LENGTH_SHORT).show()
            dismiss()
            parent.loadData("")
        })
    }

    override fun onStart() {
        super.onStart()
        detail(arguments?.get("kode").toString())
    }

    private fun detail(id: String) {
        pVM.detail(id).observe(this, Observer { pengurus ->
            v.detailNama.setText(pengurus.nama_pengguna)
            v.detailJenkel.setText(pengurus.jenis_kelamin)
            v.detailAlamat.setText(pengurus.alamat)
            v.detailNoTelp.setText(pengurus.no_telp)
            v.detailUsername.setText(pengurus.username)
            v.detailPassword.setText(pengurus.password)
        })
    }
}