package com.example.ths_pondok.Admin.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ths_pondok.Admin.Model.Pengurus
import com.example.ths_pondok.UrlClass
import org.json.JSONArray
import org.json.JSONObject

class PengurusRepository(context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val urlClass: UrlClass = UrlClass()

    fun loadData(nama: String): LiveData<List<Pengurus>> {
        val data = MutableLiveData<List<Pengurus>>()
        val request = object : StringRequest(
            Method.POST, urlClass.urlPengurus,
            Response.Listener { response ->
                val dataList = mutableListOf<Pengurus>()
                val jsonArray = JSONArray(response)
                for (x in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(x)
                    val list = Pengurus(
                        jsonObject.getString("nama_pengguna"), "",
                    "", jsonObject.getString("no_telp"),
                        jsonObject.getString("username"),""
                    )
                    dataList.add(list)
                }
                data.value = dataList
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "load_data")
                hm.put("nama", nama)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }

    fun detail(username: String): LiveData<Pengurus> {
        val data = MutableLiveData<Pengurus>()
        val request = object : StringRequest(
            Method.POST, urlClass.urlPengurus,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("nama_pengguna")
                val st2 = jsonObject.getString("jenis_kelamin")
                val st3 = jsonObject.getString("alamat")
                val st4 = jsonObject.getString("no_telp")
                val st5 = jsonObject.getString("username")
                val st6 = jsonObject.getString("password")

                val dataList = Pengurus(st1,st2,st3,st4,st5,st6)
                data.value = dataList
            },
            Response.ErrorListener { error ->
                // Error handling
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "detail")
                hm.put("username", username)

                return hm
            }
        }
        requestQueue.add(request)
        return data
    }

    fun insert(pengurus: Pengurus): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val request = object : StringRequest(
            Method.POST, urlClass.urlPengurus,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                result.value = respon == "1"
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                hm.put("mode", "insert")
                hm.put("username", pengurus.username)
                hm.put("password", pengurus.password)
                hm.put("nama_pengguna", pengurus.nama_pengguna)
                hm.put("jenis_kelamin", pengurus.jenis_kelamin)
                hm.put("alamat", pengurus.alamat)
                hm.put("no_telp", pengurus.no_telp)

                return hm
            }
        }

        requestQueue.add(request)
        return result
    }

    fun edit(userLama: String, pengurus: Pengurus): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val request = object : StringRequest(
            Method.POST, urlClass.urlPengurus,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                result.value = respon == "1"
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                hm.put("mode", "edit")
                hm.put("userLama", userLama)
                hm.put("username", pengurus.username)
                hm.put("password", pengurus.password)
                hm.put("nama_pengguna", pengurus.nama_pengguna)
                hm.put("jenis_kelamin", pengurus.jenis_kelamin)
                hm.put("alamat", pengurus.alamat)
                hm.put("no_telp", pengurus.no_telp)

                return hm
            }
        }

        requestQueue.add(request)
        return result
    }

    fun delete(username: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val request = object : StringRequest(
            Method.POST, urlClass.urlPengurus,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                result.value = respon == "1"
            },
            Response.ErrorListener { error ->

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "delete")
                hm.put("username", username)
                return hm
            }
        }
        requestQueue.add(request)
        return result
    }
}