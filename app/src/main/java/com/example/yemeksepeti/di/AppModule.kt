package com.example.yemeksepeti.di

import com.example.yemeksepeti.datasource.YemeklerDataSource
import com.example.yemeksepeti.reporsitory.YemeklerRepository
import com.example.yemeksepeti.retrofit.ApiUtils
import com.example.yemeksepeti.retrofit.YemeklerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

   @Provides
   @Singleton
fun provideYemeklerDataSource(kdao: YemeklerDao): YemeklerDataSource {
    return YemeklerDataSource(kdao)
}
   @Provides
   @Singleton
   fun provideYemeklerRepostry(kds: YemeklerDataSource): YemeklerRepository{
       return YemeklerRepository(kds)
   }

    @Provides
    @Singleton
    fun provideYemeklerDao(): YemeklerDao {
        return ApiUtils.getyemeklerDao()
    }

}