package com.vinicius.githubapi.ui.detail.information

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.arch.toolkit.delegate.viewProvider
import com.vinicius.githubapi.R
import com.vinicius.githubapi.ui.adapter.IssuesAdapter
import com.vinicius.githubapi.ui.adapter.LoaderAdapter
import com.vinicius.githubapi.ui.detail.REPOSITORY_FULL_NAME_ARGS
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class IssuesFragment : Fragment(R.layout.issues_fragment) {

    private val issues: RecyclerView by viewProvider(R.id.issues)
    private val viewModel: IssuesViewModel by viewModel()
    private val adapter = IssuesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        arguments?.let {
            val repositoryFullName: String = it.getString(REPOSITORY_FULL_NAME_ARGS) ?: ""
            viewModel.repo = repositoryFullName
        }
        loadIssues()
    }

    private fun setupRecyclerView() {
        issues.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        issues.adapter = adapter.withLoadStateFooter(
            LoaderAdapter(R.layout.issue_item_loading) {
                loadIssues()
            }
        )
    }

    private fun loadIssues() {
        lifecycleScope.launch {
            viewModel.issuesPaging().distinctUntilChanged().apply {
                collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        issues.adapter = null
        super.onDestroyView()
    }
}