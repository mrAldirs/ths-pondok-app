package com.example.ths_pondok.Admin.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.ths_pondok.Admin.Model.Pengurus
import com.example.ths_pondok.Admin.Repository.PengurusRepository

class PengurusViewModel : AndroidViewModel {

    private val pengurusRepository: PengurusRepository

    constructor(application: Application) : super(application) {
        pengurusRepository = PengurusRepository(application)
    }

    fun load_data(nama: String) : LiveData<List<Pengurus>> {
        return pengurusRepository.loadData(nama)
    }

    fun insert(pengurus: Pengurus) : LiveData<Boolean> {
        return pengurusRepository.insert(pengurus)
    }

    fun detail(username: String) : LiveData<Pengurus> {
        return pengurusRepository.detail(username)
    }

    fun delete(username: String) : LiveData<Boolean> {
        return pengurusRepository.delete(username)
    }

    fun edit(username: String, pengurus: Pengurus): LiveData<Boolean> {
        return pengurusRepository.edit(username, pengurus)
    }
}