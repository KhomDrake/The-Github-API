package com.vinicius.githubapi.ui.detail.information

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.arch.toolkit.delegate.viewProvider
import com.vinicius.githubapi.R
import com.vinicius.githubapi.ui.adapter.CommitsAdapter
import com.vinicius.githubapi.ui.adapter.LoaderAdapter
import com.vinicius.githubapi.ui.detail.LANGUAGE_REPOSITORY_ARGS
import com.vinicius.githubapi.ui.detail.REPOSITORY_FULL_NAME_ARGS
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommitsFragment : Fragment(R.layout.commits_fragment) {

    private val commits: RecyclerView by viewProvider(R.id.commits)
    private val viewModel: CommitsViewModel by viewModel()
    private val adapter = CommitsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        arguments?.let {
            val repositoryFullName: String = it.getString(REPOSITORY_FULL_NAME_ARGS) ?: ""
            val repositoryLanguage: String = it.getString(LANGUAGE_REPOSITORY_ARGS) ?: ""
            viewModel.language = repositoryLanguage
            viewModel.repo = repositoryFullName
        }

        loadCommits()
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
    }

    private fun loadCommits() {
        lifecycleScope.launch {
            viewModel.commitsPaging().distinctUntilChanged().apply {
                collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        commits.adapter = null
        super.onDestroyView()
    }

}