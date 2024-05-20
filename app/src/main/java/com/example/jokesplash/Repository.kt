package com.example.jokesplash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.jokesplash.model.JokesClass
import com.example.jokesplash.remote.JokesApi

class Repository {


    private var _jokes = MutableLiveData<List<JokesClass>>()
    val jokes : LiveData<List<JokesClass>>
    get() = _jokes


    suspend fun getJokes(limit: Int){
        val response = JokesApi.retrofitService.getJokes(limit)

        _jokes.value = response
        Log.e("MainViewModelTest","${jokes.value}")

    }


}