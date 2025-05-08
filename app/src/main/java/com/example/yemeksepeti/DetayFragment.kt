package com.example.yemeksepeti

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.yemeksepeti.databinding.FragmentDetayBinding
import com.example.yemeksepeti.entitiy.YemekG
import com.example.yemeksepeti.viewmodel.DetayViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DetayFragment : Fragment() {
    private lateinit var binding: FragmentDetayBinding
    private val viewModel: DetayViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetayBinding.inflate(inflater, container, false)
        val bundle: DetayFragmentArgs by navArgs()
        val gelenYemek = bundle.yemekg
        
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${gelenYemek.yemek_resim_adi}"
        Glide.with(this).load(url).override(300, 300).into(binding.imageViewYemekDetay)
        binding.textViewFiyat.text = "${gelenYemek.yemek_fiyat}Tl"
        binding.textViewYemekAd.text = "${gelenYemek.yemek_adi}"

        var sonuc = 1
        binding.buttonAzalt.setOnClickListener {
            if (sonuc > 0) {
                sonuc--
                binding.textViewMiktar.text = sonuc.toString()
                binding.textViewToplamTutarDetay.text = "${gelenYemek.yemek_fiyat.toDouble() * sonuc}"
            }
        }
        binding.buttonArtma.setOnClickListener {
            sonuc++
            binding.textViewMiktar.text = sonuc.toString()
            binding.textViewToplamTutarDetay.text = "${gelenYemek.yemek_fiyat.toDouble() * sonuc}"
        }
        binding.textViewToplamTutarDetay.text = "${gelenYemek.yemek_fiyat.toDouble() * sonuc}"

        binding.buttonSepeteEkle.setOnClickListener {
            gelenYemek.yemek_siparis_adet = sonuc
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    viewModel.ekleYemek(gelenYemek)
                    // API çağrısı başarılı olduktan sonra sepet sayfasına yönlendir
                    val geciss = DetayFragmentDirections.detaydanSepete(yemekg = gelenYemek, adet = sonuc)
                    Navigation.findNavController(it).navigate(geciss)
                } catch (e: Exception) {
                    // Hata durumunda kullanıcıya bilgi ver
                    android.widget.Toast.makeText(requireContext(), "Sepete eklenirken bir hata oluştu", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }
}