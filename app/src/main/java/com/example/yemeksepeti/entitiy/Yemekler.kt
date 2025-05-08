package com.example.yemeksepeti.entitiy

import java.io.Serializable

data class Yemekler(
    var sepet_yemek_id: Int,
    var yemek_adi: String,
    var yemek_resim_adi: String,
    var yemek_fiyat: Int,
    var yemek_siparis_adet: Int=1,
    var kullanici_adi: String
) : Serializable