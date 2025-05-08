package com.example.yemeksepeti.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yemeksepeti.entitiy.YemekG
import com.example.yemeksepeti.entitiy.Yemekler
import com.example.yemeksepeti.reporsitory.YemeklerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SepetViewModel @Inject constructor(var krepo: YemeklerRepository): ViewModel() {

    var sepetYemeklerListesi = MutableLiveData<List<YemekG>>()

    init {
        sepetYemekleriniYukle()
    }

    fun sepetYemekleriniYukle() {
        viewModelScope.launch {
            try {
                sepetYemeklerListesi.value = krepo.sepetYemekleriGetir()
            } catch (e: Exception) {
                sepetYemeklerListesi.value = emptyList()
            }
        }
    }

    fun sil(position: Int) {
        viewModelScope.launch {
            try {
                krepo.sil(position)
                sepetYemekleriniYukle()
            } catch (e: Exception) {
                // Hata durumunda yapılacak işlemler
            }
        }
    }
}

