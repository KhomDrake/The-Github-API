package com.vinicius.githubapi.ui.detail.information

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
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
import com.vinicius.githubapi.ui.adapter.CommitsAdapter
import com.vinicius.githubapi.ui.adapter.LoaderAdapter
import com.vinicius.githubapi.ui.detail.LANGUAGE_REPOSITORY_ARGS
import com.vinicius.githubapi.ui.detail.REPOSITORY_FULL_NAME_ARGS
import com.vinicius.githubapi.ui.handleException
import com.vinicius.githubapi.ui.widget.ErrorView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommitsFragment : Fragment(R.layout.commits_fragment) {

    private val commits: RecyclerView by viewProvider(R.id.commits)
    private val root: ViewGroup by viewProvider(R.id.root)
    private val shimmer: ShimmerFrameLayout by viewProvider(R.id.shimmer_commits)
    private val errorView: ErrorView by viewProvider(R.id.error_view)
    private val viewModel: CommitsViewModel by viewModel()
    private val adapter = CommitsAdapter()
    private val viewStateMachine = ViewStateMachine()
    private val stateLoading = 0
    private val stateData = 1
    private val stateError = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        arguments?.let {
            val repositoryFullName: String = it.getString(REPOSITORY_FULL_NAME_ARGS) ?: ""
            val repositoryLanguage: String = it.getString(LANGUAGE_REPOSITORY_ARGS) ?: ""
            viewModel.language = repositoryLanguage
            viewModel.repo = repositoryFullName
        }
        setupViewStateMachine()
        setupErrorView()
        loadCommits()
    }

    private fun setupErrorView() {
        viewModel.error.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                errorView.handleException(it)
                viewStateMachine.changeState(stateError)
            }
        })

        errorView.setOnButtonErrorListener {
            loadCommits()
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
                visibles(shimmer)
                gones(commits, errorView)
            }

            state(stateData) {
                visibles(commits)
                gones(shimmer, errorView)
            }

            state(stateError) {
                visibles(errorView)
                gones(shimmer, commits)
            }
        }
    }

    private fun setupRecyclerView() {
        commits.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        commits.adapter = adapter.withLoadStateFooter(
            LoaderAdapter(R.layout.commit_item_loading) {
                loadCommits()
            }
        )
        adapter.addOnPagesUpdatedListener {
            if(viewStateMachine.currentStateKey != stateData && viewStateMachine.isStarted)
                viewStateMachine.changeState(stateData)
        }
    }

    private fun loadCommits() {
        lifecycleScope.launch {
            viewStateMachine.changeState(stateLoading)
            viewModel.commitsPaging().distinctUntilChanged().apply {
                collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        commits.adapter = null
        viewStateMachine.shutdown()
        super.onDestroyView()
    }

}