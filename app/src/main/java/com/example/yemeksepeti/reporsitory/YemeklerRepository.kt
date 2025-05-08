package com.example.yemeksepeti.reporsitory

import com.example.yemeksepeti.datasource.YemeklerDataSource
import com.example.yemeksepeti.entitiy.YemekG
import javax.inject.Inject

class YemeklerRepository @Inject constructor(var yds: YemeklerDataSource) {

    suspend fun yemeklerYukle(): List<YemekG> {
        return yds.yemekleriGetir()
    }

    suspend fun sepetYemekleriGetir(): List<YemekG> {
        return yds.sepetYemekleriGetir()
    }

    suspend fun ekleYemek(yemek: YemekG) {
        yds.ekleYemek(yemek)
    }

    suspend fun sil(position: Int) {
        yds.sil(position)
    }
}