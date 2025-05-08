package com.example.yemeksepeti.retrofit

import com.example.yemeksepeti.entitiy.Crudcevap
import com.example.yemeksepeti.entitiy.Sepetceva
import com.example.yemeksepeti.entitiy.YemekG
import com.example.yemeksepeti.entitiy.YemeklerCevap
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface YemeklerDao {
//http://kasimadalan.pe.hu/yemekler/tumYemekleriGetir.php
//http://kasimadalan.pe.hu/  -> base url
 //yemekler/tumYemekleriGetir.php    -- web servis

    @GET("yemekler/tumYemekleriGetir.php")
    suspend fun yemekleriYukle(): YemeklerCevap

    @POST("yemekler/sepeteYemekEkle.php")
    @FormUrlEncoded
    suspend fun sepeteEkle(
        @Field("yemek_adi") yemek_adi: String,
        @Field("yemek_resim_adi") yemek_resim_adi: String,
        @Field("yemek_fiyat") yemek_fiyat: Int,
        @Field("yemek_siparis_adet") yemek_siparis_adet: Int = 1,
        @Field("kullanici_adi") kullanici_adi: String
    ): Crudcevap

    @POST("yemekler/sepettekiYemekleriGetir.php")
    @FormUrlEncoded
    suspend fun sepeteyemeklerigetir(@Field("kullanici_adi") kullanici_adi: String): Sepetceva

    @POST("yemekler/sepettenYemekSil.php")
    @FormUrlEncoded
    suspend fun sepettenYemekSil(
        @Field("sepet_yemek_id") sepet_yemek_id: Int,
        @Field("kullanici_adi") kullanici_adi: String
    ): Crudcevap
}