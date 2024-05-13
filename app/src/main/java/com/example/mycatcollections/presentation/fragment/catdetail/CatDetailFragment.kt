package com.example.mycatcollections.presentation.fragment.catdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import coil.load
import com.example.mycatcollections.databinding.FragmentCatDetailBinding
import com.example.mycatcollections.presentation.viewmodel.CatDetailViewModel
import com.google.android.material.snackbar.Snackbar

class CatDetailFragment : Fragment() {
    private val viewModel: CatDetailViewModel by viewModels<CatDetailViewModel> {
        CatDetailViewModel.provideFactory(this, requireActivity().applicationContext)
    }
    private lateinit var binding: FragmentCatDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCatDetailBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
    }

    private fun setView() {
        viewModel.getCatDetail(arguments?.getString("id")!!).observe(this) { cat ->
            val weight: String = "${cat.weight} m"

            binding.catImage.load(cat.imgUrl)
            binding.catTitle.text = cat.name
            binding.catDesc.text = cat.description
            binding.catOrigin.text = cat.origin
            binding.catTemperament.text = cat.temperament
            binding.catWeight.text = weight
            binding.catLifespan.text = cat.lifeSpan
            binding.learnMoreBtn.setOnClickListener { navigateToGoogle(cat.name) }
        }

        viewModel.getError().observe(this) { error ->
            Snackbar.make(
                binding.root,
                error.message.toString(),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun navigateToGoogle(name: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.google.com/search?q=${name}")
        startActivity(intent)
    }
}