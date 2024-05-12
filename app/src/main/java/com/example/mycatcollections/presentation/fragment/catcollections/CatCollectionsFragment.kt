package com.example.mycatcollections.presentation.fragment.catcollections

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mycatcollections.data.model.Cat
import com.example.mycatcollections.databinding.FragmentCatCollectionsBinding
import com.example.mycatcollections.extension.SpacesItemDecoration
import com.example.mycatcollections.presentation.fragment.catcollections.adapter.CatCollectionsAdapter
import com.example.mycatcollections.presentation.fragment.catcollections.adapter.CatCollectionsAdapterListener
import com.example.mycatcollections.presentation.viewmodel.CatCollectionsViewModel
import com.google.android.material.snackbar.Snackbar

class CatCollectionsFragment : Fragment(), CatCollectionsAdapterListener {
    private val viewModel: CatCollectionsViewModel by viewModels<CatCollectionsViewModel> {
        CatCollectionsViewModel.provideFactory(this, requireActivity().applicationContext)
    }
    private val catCollectionsAdapter = CatCollectionsAdapter(this)
    private lateinit var binding: FragmentCatCollectionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentCatCollectionsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData(view.context)
        refresh()
        binding.swipeRefresh.setOnRefreshListener { refresh() }
    }

    override fun onClickCard(id: String) {

    }

    private fun setData(context: Context) {
        binding.catList.layoutManager = GridLayoutManager(
            context,
            2,
        )
        binding.catList.adapter = catCollectionsAdapter
        binding.catList.itemAnimator = DefaultItemAnimator()
        binding.catList.addItemDecoration(SpacesItemDecoration(2,5,false))
    }

    private fun refresh() {
        viewModel.getCatCollections().observe(this) { cats ->
            catCollectionsAdapter.submitList(cats)
        }

        viewModel.getError().observe(this) { error ->
            Snackbar.make(
                binding.root,
                error.message.toString(),
                Snackbar.LENGTH_SHORT
            ).show()
        }

        binding.swipeRefresh.isRefreshing = false
    }
}