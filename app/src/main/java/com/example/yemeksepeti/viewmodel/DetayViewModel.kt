package com.example.yemeksepeti.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.yemeksepeti.entitiy.YemekG
import com.example.yemeksepeti.entitiy.Yemekler
import com.example.yemeksepeti.reporsitory.YemeklerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetayViewModel @Inject constructor(var krepo: YemeklerRepository): ViewModel() {

    suspend fun ekleYemek(yeniYemek: YemekG) {
        try {
            Log.d("DetayViewModel", "Yemek ekleniyor: ${yeniYemek.yemek_adi}, Adet: ${yeniYemek.yemek_siparis_adet}")
            krepo.ekleYemek(yemek = yeniYemek)
            Log.d("DetayViewModel", "Yemek başarıyla eklendi")
        } catch (e: Exception) {
            Log.e("DetayViewModel", "Yemek ekleme hatası: ${e.message}")
            throw e
        }
    }
}