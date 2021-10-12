package com.vinicius.githubapi.ui.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import br.com.arch.toolkit.delegate.viewProvider
import br.com.arch.toolkit.statemachine.ViewStateMachine
import br.com.arch.toolkit.statemachine.config
import br.com.arch.toolkit.statemachine.setup
import br.com.arch.toolkit.statemachine.state
import com.facebook.shimmer.ShimmerFrameLayout
import com.vinicius.githubapi.R
import com.vinicius.githubapi.ui.adapter.RepositoryAdapter
import com.vinicius.githubapi.ui.adapter.LoaderAdapter
import com.vinicius.githubapi.ui.handleException
import com.vinicius.githubapi.ui.widget.ErrorView
import com.vinicius.githubapi.ui.widget.SearchInput
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.home_fragment) {

    private val homeViewModel: HomeViewModel by viewModel()

    private val root: ViewGroup by viewProvider(R.id.root)
    private val errorView: ErrorView by viewProvider(R.id.error_view)
    private val shimmerFrameLayout: ShimmerFrameLayout by viewProvider(R.id.shimmer_home)
    private val search: SearchInput by viewProvider(R.id.search_repo)
    private val selectedRepo: AppCompatTextView by viewProvider(R.id.actual_repo)
    private val repos: RecyclerView by viewProvider(R.id.repos)
    private val adapter = RepositoryAdapter {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeToDetailFragment(
                it
            )
        )
    }

    private val stateLoading = 0
    private val stateData = 1
    private val stateError = 2

    private val viewStateMachine = ViewStateMachine()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateSelectedRepo()
        setupViewStateMachine()
        setupRecyclerView()
        setupSearch()
        setupData()
        loadRepos()
        setupError()

    }

    private fun setupError() {
        homeViewModel.error.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                viewStateMachine.changeState(stateError)
            }
        })

        errorView.setOnButtonErrorListener {
            loadRepos()
        }
    }

    private fun setupViewStateMachine() {
        viewStateMachine.setup {
            config {
                initialState = stateLoading
                setOnChangeState {
                    TransitionManager.beginDelayedTransition(root)
                }
            }

            state(stateLoading) {
                visibles(shimmerFrameLayout)
                gones(selectedRepo, repos, errorView)
            }

            state(stateData) {
                visibles(selectedRepo, repos)
                gones(shimmerFrameLayout, errorView)
            }

            state(stateError) {
                visibles(errorView)
                gones(shimmerFrameLayout, selectedRepo, repos)
            }
        }
    }

    private fun setupSearch() {
        search.onClickSearch {
            homeViewModel.updateSelectedLanguage(it)
            updateSelectedRepo()
            setupData()
            loadRepos()
        }
    }

    private fun setupRecyclerView() {
        repos.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
    }

    private fun updateSelectedRepo() {
        selectedRepo.text = getString(R.string.selected_language, homeViewModel.selectedLanguage)
        selectedRepo.isVisible = true
    }

    private fun loadRepos() {
        lifecycleScope.launch {
            viewStateMachine.changeState(stateLoading)
            homeViewModel.repositoriesPaging().distinctUntilChanged().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun setupData() {
        repos.adapter = adapter.withLoadStateFooter(LoaderAdapter(R.layout.repository_item_loading) {
            loadRepos()
        })
        adapter.addOnPagesUpdatedListener {
            if(viewStateMachine.currentStateKey != stateData && viewStateMachine.isStarted)
                viewStateMachine.changeState(stateData)
        }
    }

    override fun onDestroyView() {
        viewStateMachine.shutdown()
        repos.adapter = null
        super.onDestroyView()
    }
}
