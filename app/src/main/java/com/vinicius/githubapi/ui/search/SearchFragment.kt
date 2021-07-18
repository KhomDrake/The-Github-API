package com.vinicius.githubapi.ui.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.arch.toolkit.delegate.viewProvider
import br.com.arch.toolkit.statemachine.ViewStateMachine
import br.com.arch.toolkit.statemachine.setup
import br.com.arch.toolkit.statemachine.state
import com.facebook.shimmer.ShimmerFrameLayout
import com.vinicius.githubapi.R
import com.vinicius.githubapi.extension.openLink
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
    private val emptyStateText: AppCompatTextView by viewProvider(R.id.empty_text)
    private val shimmer: ShimmerFrameLayout by viewProvider(R.id.shimmer_search)
    private val users: RecyclerView by viewProvider(R.id.users)
    private val adapter = UserAdapter {
        requireContext().openLink(it.url)
    }

    private val viewStateMachine = ViewStateMachine()
    private val stateData = 0
    private val stateLoading = 1
    private val stateEmpty = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupLoadUser()
        setupViewStateMachine()
    }

    private fun setupViewStateMachine() {
        viewStateMachine.setup {
            state(stateData) {
                visibles(users)
                gones(shimmer, emptyStateText)
            }

            state(stateLoading) {
                visibles(shimmer)
                gones(users, emptyStateText)
            }

            state(stateEmpty) {
                visibles(emptyStateText)
                gones(users, shimmer)
            }
        }
    }

    private fun setupLoadUser() {
        if(viewStateMachine.isStarted) viewStateMachine.changeState(stateLoading)

        searchInput.onClickSearch {
            loadUser(it)
        }
    }

    private fun setupRecyclerView() {
        users.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )

        adapter.addOnPagesUpdatedListener {
            if(!viewStateMachine.isStarted) return@addOnPagesUpdatedListener

            if(adapter.itemCount == 0) {
                viewStateMachine.changeState(stateEmpty)
            } else {
                viewStateMachine.changeState(stateData)
            }
        }
    }

    private fun loadUser(user: String) {
        if(users.adapter == null)
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
        viewStateMachine.shutdown()
        users.adapter = null
        super.onDestroyView()
    }
}
