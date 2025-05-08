package com.example.yemeksepeti.retrofit

class ApiUtils { //interface erişmemizi sağlayan class
    companion object{
        val BASE_URL="http://kasimadalan.pe.hu/"

        fun getyemeklerDao(): YemeklerDao{
            return RetrofitClient.getClient(BASE_URL).create(YemeklerDao::class.java)
        }
    }
}