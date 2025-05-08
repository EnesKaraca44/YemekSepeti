package com.example.yemeksepeti


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.yemeksepeti.databinding.FragmentAnasayfaBinding
import com.example.yemeksepeti.entitiy.Yemekler
import com.example.yemeksepeti.viewmodel.AnasayfaViewModel
import dagger.hilt.android.AndroidEntryPoint
//import com.example.yemeksepeti.viewmodel.AnasayfaViewModel
//import com.example.yemeksepeti.viewmodel.SepetViewModel
import kotlin.getValue


@AndroidEntryPoint
class AnasayfaFragment : Fragment() {
   private lateinit var binding:FragmentAnasayfaBinding
  private lateinit var viewModel: AnasayfaViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAnasayfaBinding.inflate(inflater, container, false)
        binding.toolbarAnasayfa.title="Yemek Sepeti"
    /*    val yemeklerListesi= ArrayList<Yemekler>()
       val f1= Yemekler(1,"hamburger","hamburger",85,4,"enes")
        val f2= Yemekler(2,"pizza","pizza",87,2,"hasan")
        yemeklerListesi.add(f2)
        yemeklerListesi.add(f1)

     */
        viewModel.yemeklerListesi.observe(viewLifecycleOwner) {
            val adapter= AnasayfaCardAdapter(requireContext(),it,viewModel)
            binding.rvAnasayfa.adapter=adapter
        }
        binding.rvAnasayfa.layoutManager = GridLayoutManager(requireContext(), 2)

        return binding.root
    }
   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: AnasayfaViewModel by viewModels()
        viewModel = tempViewModel
    }


    }
