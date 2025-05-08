package com.example.yemeksepeti.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yemeksepeti.databinding.SepetCardTasarimBinding
import com.example.yemeksepeti.entitiy.YemekG

class SepetAdapter(
    private var yemekler: List<YemekG>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<SepetAdapter.SepetViewHolder>() {

    inner class SepetViewHolder(private val binding: SepetCardTasarimBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(yemek: YemekG) {
            binding.apply {
                textViewSepetCardYeAd.text = yemek.yemek_adi
                textViewSepetCardGfiyat.text = "${yemek.yemek_fiyat} TL"
                textViewCardSepetAdet.text = yemek.yemek_siparis_adet.toString()
                textViewCardSepetTfiyat.text = "${yemek.yemek_fiyat.toDouble() * yemek.yemek_siparis_adet} TL"
                
                // Resim yükleme kodu
                val url = "http://kasimadalan.pe.hu/yemekler/resimler/${yemek.yemek_resim_adi}"
                Log.d("SepetAdapter", "Resim yükleniyor: $url")
                
                Glide.with(itemView.context)
                    .load(url)
                    .override(300, 300)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(imageView3)
                
                imageButton.setOnClickListener {
                    onDeleteClick(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SepetViewHolder {
        val binding = SepetCardTasarimBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SepetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SepetViewHolder, position: Int) {
        val yemek = yemekler[position]
        Log.d("SepetAdapter", "Yemek bağlanıyor: ${yemek.yemek_adi}, Resim: ${yemek.yemek_resim_adi}")
        holder.bind(yemek)
    }

    override fun getItemCount(): Int = yemekler.size

    fun updateList(newList: List<YemekG>) {
        Log.d("SepetAdapter", "Liste güncelleniyor. Yeni liste boyutu: ${newList.size}")
        Log.d("SepetAdapter", "Yeni liste: ${newList.map { "${it.yemek_adi} (${it.yemek_resim_adi})" }}")
        yemekler = newList
        notifyDataSetChanged()
    }
} 