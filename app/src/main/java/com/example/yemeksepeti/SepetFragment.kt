package com.example.yemeksepeti

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yemeksepeti.adapter.SepetAdapter
import com.example.yemeksepeti.databinding.FragmentSepetBinding
import com.example.yemeksepeti.entitiy.YemekG
import com.example.yemeksepeti.viewmodel.SepetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SepetFragment : Fragment() {
    private lateinit var binding: FragmentSepetBinding
    private val viewModel: SepetViewModel by viewModels()
    private lateinit var adapter: SepetAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSepetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        viewModel.sepetYemekleriniYukle()
    }

    private fun setupRecyclerView() {
        adapter = SepetAdapter(emptyList(), { position ->
            viewModel.sil(position)
        })
        binding.rvSepet.adapter = adapter
        binding.rvSepet.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        viewModel.sepetYemeklerListesi.observe(viewLifecycleOwner) { yemekler ->
            if (yemekler.isNotEmpty()) {
                adapter.updateList(yemekler)
                var toplamTutar = 0
                yemekler.forEach { yemek ->
                    toplamTutar += yemek.yemek_fiyat.toInt() * yemek.yemek_siparis_adet
                }
                binding.textViewToplamTutar.text = "Toplam Tutar: $toplamTutar TL"
            } else {
                adapter.updateList(emptyList())
                binding.textViewToplamTutar.text = "Sepetiniz Boş"
            }
        }
    }
}