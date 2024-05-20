package com.example.jokesplash

import android.util.Log
import android.view.animation.AnimationUtils
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val repository = Repository()

    val jokes = repository.jokes

    fun getJokes(limit: Int){
        viewModelScope.launch {
            try {
                repository.getJokes(limit)
            } catch (e:Exception) {
                Log.e("MainViewModel","$e")
            }
        }
    }

}