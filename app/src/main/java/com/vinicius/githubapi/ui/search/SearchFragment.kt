package com.vinicius.githubapi.ui.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.arch.toolkit.delegate.viewProvider
import com.vinicius.githubapi.R
import com.vinicius.githubapi.ui.adapter.LoaderAdapter
import com.vinicius.githubapi.ui.adapter.UserAdapter
import com.vinicius.githubapi.ui.widget.SearchInput
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(R.layout.search_fragment) {

    private val searchViewModel: SearchViewModel by viewModel()
    private val searchInput: SearchInput by viewProvider(R.id.search_users)
    private val users: RecyclerView by viewProvider(R.id.users)
    private val adapter = UserAdapter {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(it.url)
        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupLoadUser()
    }

    private fun setupLoadUser() {
        searchInput.onClickSearch {
            loadUser(it)
        }
    }

    private fun setupRecyclerView() {
        users.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )

        adapter.addOnPagesUpdatedListener {

        }
    }

    private fun loadUser(user: String) {
        users.adapter = adapter.withLoadStateFooter(
            LoaderAdapter(R.layout.user_item_loading) {
                loadUser(user)
            }
        )
        lifecycleScope.launch {
            searchViewModel.searchUser(user).distinctUntilChanged().apply {
                collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        users.adapter = null
        super.onDestroyView()
    }

}
