package com.vinicius.githubapi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.arch.toolkit.delegate.viewProvider
import com.vinicius.githubapi.R

class RepositoryLoaderAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<RepositoryLoaderViewHolder>() {
    override fun onBindViewHolder(holder: RepositoryLoaderViewHolder, loadState: LoadState) {
        when(holder) {
            is RepositoryErrorViewHolder -> {
                holder.bind(retry)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): RepositoryLoaderViewHolder {
        return when(loadState) {
            is LoadState.Error -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.pagination_error, parent, false
                )
                RepositoryErrorViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.repository_item_loading, parent, false
                )
                RepositoryLoadingViewHolder(view)
            }
        }
    }

}

abstract class RepositoryLoaderViewHolder(view: View): RecyclerView.ViewHolder(view)

class RepositoryErrorViewHolder(view: View): RepositoryLoaderViewHolder(view) {

    private val errorButton: AppCompatButton by viewProvider(R.id.try_again)

    fun bind(retry: () -> Unit) {
        errorButton.setOnClickListener { retry.invoke() }
    }

}

class RepositoryLoadingViewHolder(view: View): RepositoryLoaderViewHolder(view)