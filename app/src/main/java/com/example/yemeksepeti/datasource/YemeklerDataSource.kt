package com.example.yemeksepeti.datasource

import android.util.Log
import com.example.yemeksepeti.entitiy.YemekG
import com.example.yemeksepeti.entitiy.YemeklerCevap
import com.example.yemeksepeti.retrofit.YemeklerDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class YemeklerDataSource @Inject constructor(private val ydao: YemeklerDao) {

    private var yemeklerListesi = mutableListOf<YemekG>()

    suspend fun yemekleriGetir(): List<YemekG> = withContext(Dispatchers.IO) {
        try {
            val response = ydao.yemekleriYukle()
            if (response.success == 1) {
                yemeklerListesi = response.yemekler.map { yemek ->
                    YemekG(
                        yemek.sepet_yemek_id.toString(),
                        yemek.yemek_adi,
                        yemek.yemek_resim_adi,
                        yemek.yemek_fiyat.toString(),
                        0
                    )
                }.toMutableList()
            }
            return@withContext yemeklerListesi
        } catch (e: Exception) {
            Log.e("YemeklerDataSource", "Yemekler yüklenirken hata: ${e.message}")
            return@withContext emptyList()
        }
    }

    suspend fun sepetYemekleriGetir(): List<YemekG> = withContext(Dispatchers.IO) {
        try {
            val response = ydao.sepeteyemeklerigetir("enes")
            Log.d("YemeklerDataSource", "API Response: success=${response.success}")
            
            if (response.success == 1) {
                val sepetYemekler = response.sepet_yemekler
                Log.d("YemeklerDataSource", "Sepet yemekleri null mu: ${sepetYemekler == null}")
                Log.d("YemeklerDataSource", "Sepet yemekleri boş mu: ${sepetYemekler?.isEmpty()}")
                
                if (sepetYemekler.isNullOrEmpty()) {
                    Log.d("YemeklerDataSource", "Sepet boş veya null")
                    return@withContext emptyList()
                }

                // Her yemeği detaylı logla
                sepetYemekler.forEachIndexed { index, yemek ->
                    Log.d("YemeklerDataSource", """
                        Yemek #$index:
                        ID: ${yemek.yemek_id}
                        Ad: ${yemek.yemek_adi}
                        Resim: ${yemek.yemek_resim_adi}
                        Fiyat: ${yemek.yemek_fiyat}
                        Adet: ${yemek.yemek_siparis_adet}
                    """.trimIndent())
                }

                // Her yemeği YemekG'ye dönüştür
                val yemekListesi = sepetYemekler.mapNotNull { yemek ->
                    try {
                        YemekG(
                            yemek.yemek_id?.toString() ?: "0",
                            yemek.yemek_adi,
                            yemek.yemek_resim_adi,
                            yemek.yemek_fiyat,
                            yemek.yemek_siparis_adet ?: 1
                        ).also {
                            Log.d("YemeklerDataSource", "Dönüştürülen yemek: ID=${it.yemek_id}, Ad=${it.yemek_adi}, Adet=${it.yemek_siparis_adet}")
                        }
                    } catch (e: Exception) {
                        Log.e("YemeklerDataSource", "Yemek dönüştürme hatası: ${e.message}")
                        null
                    }
                }
                
                Log.d("YemeklerDataSource", "Toplam dönüştürülen yemek sayısı: ${yemekListesi.size}")
                return@withContext yemekListesi
            } else {
                Log.e("YemeklerDataSource", "API başarısız")
                return@withContext emptyList()
            }
        } catch (e: Exception) {
            Log.e("YemeklerDataSource", "Sepet yemekleri alınırken hata: ${e.message}")
            Log.e("YemeklerDataSource", "Hata detayı: ", e)
            return@withContext emptyList()
        }
    }

    suspend fun ekleYemek(yeniYemek: YemekG): Boolean = withContext(Dispatchers.IO) {
        try {
            Log.d("YemeklerDataSource", "Sepete ekleme başladı: ${yeniYemek.yemek_adi}, Resim: ${yeniYemek.yemek_resim_adi}")
            
            // Önce mevcut sepeti kontrol et
            val mevcutSepet = ydao.sepeteyemeklerigetir("enes").sepet_yemekler ?: emptyList()
            val ayniYemek = mevcutSepet.find { it.yemek_adi == yeniYemek.yemek_adi }
            
            if (ayniYemek != null) {
                // Eğer yemek zaten sepette varsa, önce sil
                Log.d("YemeklerDataSource", "Aynı yemek bulundu, siliniyor: ${ayniYemek.yemek_adi}")
                val yemekId = ayniYemek.yemek_id?.toInt() ?: 0
                ydao.sepettenYemekSil(yemekId, "enes")
                
                // Yeni toplam adet hesapla
                val yeniAdet = ayniYemek.yemek_siparis_adet + yeniYemek.yemek_siparis_adet
                Log.d("YemeklerDataSource", "Yeni toplam adet: $yeniAdet")
                
                // Yeni adetle tekrar ekle
                val response = ydao.sepeteEkle(
                    yeniYemek.yemek_adi,
                    yeniYemek.yemek_resim_adi,
                    yeniYemek.yemek_fiyat.toInt(),
                    yeniAdet,
                    "enes"
                )
                
                if (response.success == 1) {
                    Log.d("YemeklerDataSource", "Sepet güncelleme başarılı: ${yeniYemek.yemek_adi}")
                    return@withContext true
                } else {
                    Log.e("YemeklerDataSource", "Sepet güncelleme başarısız")
                    return@withContext false
                }
            } else {
                // Yemek sepette yoksa, yeni ekle
                val response = ydao.sepeteEkle(
                    yeniYemek.yemek_adi,
                    yeniYemek.yemek_resim_adi,
                    yeniYemek.yemek_fiyat.toInt(),
                    yeniYemek.yemek_siparis_adet,
                    "enes"
                )
                
                if (response.success == 1) {
                    Log.d("YemeklerDataSource", "Sepete ekleme başarılı: ${yeniYemek.yemek_adi}")
                    return@withContext true
                } else {
                    Log.e("YemeklerDataSource", "Sepete ekleme başarısız")
                    return@withContext false
                }
            }
        } catch (e: Exception) {
            Log.e("YemeklerDataSource", "Sepete ekleme hatası: ${e.message}")
            Log.e("YemeklerDataSource", "Hata detayı: ", e)
            return@withContext false
        }
    }

    suspend fun sil(position: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            val sepetYemekleri = ydao.sepeteyemeklerigetir("enes").sepet_yemekler ?: emptyList()
            if (position < sepetYemekleri.size) {
                val silinecekYemek = sepetYemekleri[position]
                Log.d("YemeklerDataSource", "Silinecek yemek: ${silinecekYemek.yemek_adi}")
                val yemekId = silinecekYemek.yemek_id?.toInt() ?: 0
                val response = ydao.sepettenYemekSil(yemekId, "enes")
                return@withContext response.success == 1
            }
            return@withContext false
        } catch (e: Exception) {
            Log.e("YemeklerDataSource", "Silme hatası: ${e.message}")
            return@withContext false
        }
    }

    fun getYemekler(): List<YemekG> {
        return yemeklerListesi.toList()
    }
}