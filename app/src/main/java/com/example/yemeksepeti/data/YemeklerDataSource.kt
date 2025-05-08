package com.example.yemeksepeti.data

import android.util.Log
import com.example.yemeksepeti.entitiy.YemekG
import com.example.yemeksepeti.entitiy.YemeklerCevap
import com.example.yemeksepeti.entitiy.Sepetceva
import com.example.yemeksepeti.retrofit.YemeklerDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class YemeklerDataSource @Inject constructor(var ydao: YemeklerDao) {
    suspend fun yemekleriGetir(): List<YemekG> {
        val response = ydao.yemekleriYukle()
        val yemeklerListesi = response.yemekler.map { yemek ->
            YemekG(
                yemek_id = yemek.sepet_yemek_id.toString(),
                yemek_adi = yemek.yemek_adi,
                yemek_resim_adi = yemek.yemek_resim_adi,
                yemek_fiyat = yemek.yemek_fiyat.toString(),
                yemek_siparis_adet = 0
            )
        }
        return yemeklerListesi
    }

    suspend fun sepetYemekleriGetir(): List<YemekG> {
        val response = ydao.sepeteyemeklerigetir("enes")
        val sepetYemekler = response.sepet_yemekler ?: return emptyList()
        return sepetYemekler
    }

    suspend fun ekleYemek(yemek: YemekG) {
        try {
            Log.d("YemeklerDataSource", "Yeni yemek ekleniyor: ${yemek.yemek_adi}, Adet: ${yemek.yemek_siparis_adet}")
            
            val mevcutSepet = ydao.sepeteyemeklerigetir("enes").sepet_yemekler ?: emptyList()
            Log.d("YemeklerDataSource", "Mevcut sepet: ${mevcutSepet.map { "${it.yemek_adi} (${it.yemek_siparis_adet})" }}")
            
            val ayniYemek = mevcutSepet.find { it.yemek_adi == yemek.yemek_adi }
            
            if (ayniYemek != null) {
                Log.d("YemeklerDataSource", "Aynı yemek bulundu. Siliniyor: ${ayniYemek.yemek_adi}")
                ydao.sepettenYemekSil(ayniYemek.yemek_id.toInt(), "enes")
                
                val toplamAdet = ayniYemek.yemek_siparis_adet + yemek.yemek_siparis_adet
                Log.d("YemeklerDataSource", "Yeni toplam adet: $toplamAdet")
                
                val response = ydao.sepeteEkle(
                    yemek.yemek_adi,
                    yemek.yemek_resim_adi,
                    yemek.yemek_fiyat.toInt(),
                    toplamAdet,
                    "enes"
                )
                Log.d("YemeklerDataSource", "Güncelleme yanıtı: $response")
            } else {
                Log.d("YemeklerDataSource", "Yeni yemek ekleniyor: ${yemek.yemek_adi}, Adet: ${yemek.yemek_siparis_adet}")
                val response = ydao.sepeteEkle(
                    yemek.yemek_adi,
                    yemek.yemek_resim_adi,
                    yemek.yemek_fiyat.toInt(),
                    yemek.yemek_siparis_adet,
                    "enes"
                )
                Log.d("YemeklerDataSource", "Ekleme yanıtı: $response")
            }
        } catch (e: Exception) {
            Log.e("YemeklerDataSource", "Yemek ekleme hatası: ${e.message}")
            throw e
        }
    }

    suspend fun sil(position: Int) {
        try {
            val sepetYemekleri = ydao.sepeteyemeklerigetir("enes").sepet_yemekler ?: emptyList()
            if (position < sepetYemekleri.size) {
                val silinecekYemek = sepetYemekleri[position]
                Log.d("YemeklerDataSource", "Silinecek yemek: ${silinecekYemek.yemek_adi}")
                val response = ydao.sepettenYemekSil(silinecekYemek.yemek_id.toInt(), "enes")
                Log.d("YemeklerDataSource", "Silme yanıtı: $response")
            }
        } catch (e: Exception) {
            Log.e("YemeklerDataSource", "Silme hatası: ${e.message}")
            throw e
        }
    }
} 