package com.vinicius.githubapi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.arch.toolkit.delegate.viewProvider
import coil.load
import com.vinicius.githubapi.R
import com.vinicius.githubapi.data.ui.Repository

class RepositoryDiffUtil: DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem.id == newItem.id && oldItem.fullName == newItem.fullName
    }
}

class RepositoryAdapter(
    private val onClickRepo: (Repository) -> Unit
) : PagingDataAdapter<Repository, RecyclerView.ViewHolder>(RepositoryDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        return RepositoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.repository_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is RepositoryViewHolder -> {
                val repo = getItem(position) ?: return
                holder.bind(repo)
                holder.itemView.setOnClickListener {
                    onClickRepo.invoke(repo)
                }
            }
            else -> Unit
        }
    }
}

class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val logo: AppCompatImageView by viewProvider(R.id.logo)
    private val name: AppCompatTextView by viewProvider(R.id.name)
    private val author: AppCompatTextView by viewProvider(R.id.author)
    private val stars: AppCompatTextView by viewProvider(R.id.stars)

    fun bind(repository: Repository) {
        logo.load(repository.owner.avatarUrl) {
            crossfade(1000)
        }
        name.text = repository.name
        author.text = itemView.context.getString(R.string.repo_author, repository.owner.login)
        stars.text = itemView.context.getString(R.string.repo_stars, repository.stars)
    }
}