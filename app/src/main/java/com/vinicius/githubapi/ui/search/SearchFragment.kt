package com.vinicius.githubapi.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.vinicius.githubapi.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(R.layout.search_fragment) {

    private val searchViewModel: SearchViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
