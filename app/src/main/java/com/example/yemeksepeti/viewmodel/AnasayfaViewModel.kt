package com.example.yemeksepeti.viewmodel

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
class AnasayfaViewModel @Inject constructor(var krepo: YemeklerRepository): ViewModel() {

    var yemeklerListesi = MutableLiveData<List<YemekG>>()

    init {
        yemeklerYukle()
    }

    fun yemeklerYukle() {
        viewModelScope.launch {
            try {
                yemeklerListesi.value = krepo.yemeklerYukle()
            } catch (e: Exception) {
                yemeklerListesi.value = emptyList()
            }
        }
    }
}