package com.example.yemeksepeti.entitiy

import java.io.Serializable

data class YemekG(var yemek_id: String,var yemek_adi: String,var yemek_resim_adi:String,var yemek_fiyat:String, var yemek_siparis_adet: Int = 1 ) :
    Serializable{
}