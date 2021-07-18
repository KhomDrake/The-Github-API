package com.vinicius.githubapi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.arch.toolkit.delegate.viewProvider
import com.vinicius.githubapi.R
import com.vinicius.githubapi.data.ui.CommitBody
import com.vinicius.githubapi.extension.openLink
import com.vinicius.githubapi.extension.toDateUS

class CommitDiffUtil: DiffUtil.ItemCallback<CommitBody>() {
    override fun areItemsTheSame(oldItem: CommitBody, newItem: CommitBody): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CommitBody, newItem: CommitBody): Boolean {
        return oldItem == newItem
    }
}

class CommitsAdapter : PagingDataAdapter<CommitBody, RecyclerView.ViewHolder>(CommitDiffUtil()) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is CommitViewHolder -> {
                val commitBody = getItem(position) ?: return
                holder.bind(commitBody)
            }
            else -> Unit
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.commit_item, parent, false)
        return CommitViewHolder(view)
    }

}

class CommitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val author: AppCompatTextView by viewProvider(R.id.author)
    private val message: AppCompatTextView by viewProvider(R.id.message)
    private val access: AppCompatTextView by viewProvider(R.id.access)
    private val date: AppCompatTextView by viewProvider(R.id.date)

    fun bind(commitBody: CommitBody) {
        author.text = itemView.context.getString(R.string.commits_author, commitBody.author?.login.toString())
        message.text = itemView.context.getString(R.string.commits_description, commitBody.commit?.message.toString())
        access.setOnClickListener {
            itemView.context.openLink(commitBody.htmlUrl)
        }
        date.text = itemView.context.getString(
            R.string.commits_date, commitBody.commit?.author?.date?.toDateUS().toString()
        )
    }
}