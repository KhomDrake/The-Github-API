package com.vinicius.githubapi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatButton
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.arch.toolkit.delegate.viewProvider
import com.vinicius.githubapi.R

class LoaderAdapter(
    @LayoutRes
    private val loadingLayout: Int,
    private val retry: () -> Unit
) : LoadStateAdapter<LoaderViewHolder>() {
    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        when(holder) {
            is ErrorViewHolder -> {
                holder.bind(retry)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoaderViewHolder {
        return when(loadState) {
            is LoadState.Error -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.pagination_error, parent, false
                )
                ErrorViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    loadingLayout, parent, false
                )
                LoadingViewHolder(view)
            }
        }
    }

}

abstract class LoaderViewHolder(view: View): RecyclerView.ViewHolder(view)

class ErrorViewHolder(view: View): LoaderViewHolder(view) {

    private val errorButton: AppCompatButton by viewProvider(R.id.try_again)

    fun bind(retry: () -> Unit) {
        errorButton.setOnClickListener { retry.invoke() }
    }

}

class LoadingViewHolder(view: View): LoaderViewHolder(view)