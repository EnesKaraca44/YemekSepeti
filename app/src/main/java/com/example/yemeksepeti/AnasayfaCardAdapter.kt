package com.example.yemeksepeti

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yemeksepeti.databinding.AnasayfaCardTasarimBinding
import com.example.yemeksepeti.databinding.FragmentAnasayfaBinding
import com.example.yemeksepeti.entitiy.YemekG
import com.example.yemeksepeti.entitiy.Yemekler
import com.example.yemeksepeti.viewmodel.AnasayfaViewModel

class AnasayfaCardAdapter(var Mcontext: Context, var yemeklerListesi: List<YemekG>, var viewModel: AnasayfaViewModel): RecyclerView.Adapter<AnasayfaCardAdapter.AnasayfaCardTasarımTutucu> () {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnasayfaCardTasarımTutucu {
val binding=AnasayfaCardTasarimBinding.inflate(LayoutInflater.from(Mcontext),parent,false)
        return AnasayfaCardTasarımTutucu(binding)
    }

    override fun onBindViewHolder(
        holder: AnasayfaCardTasarımTutucu,
        position: Int
    ) {
val yemekler=yemeklerListesi.get(position)
        val t=holder.tasarim

        t.textViewCardFiyat.text="${yemekler.yemek_fiyat}TL"
        Log.d("AnasayfaCardAdapter", "Yemek Fiyatı (pozisyon $position): ${yemekler.yemek_fiyat}")

       // t.imageViewAnasyfaCard.setImageResource(Mcontext.resources.getIdentifier(yemekler.yemek_resim_adi,"drawable",Mcontext.packageName))
        val url="http://kasimadalan.pe.hu/yemekler/resimler/${yemekler.yemek_resim_adi}"
        Glide.with(Mcontext).load(url).override(300,300).into(t.imageViewAnasyfaCard)
        t.CardViewAnasayfa.setOnClickListener {
val gecis= AnasayfaFragmentDirections.anadanDetaya(yemekg = yemekler)
            Navigation.findNavController(it).navigate(gecis)
        }
    }
    override fun getItemCount(): Int {
        return yemeklerListesi.size
    }
    inner class AnasayfaCardTasarımTutucu(var tasarim: AnasayfaCardTasarimBinding): RecyclerView.ViewHolder(tasarim.root)

}
