package com.vinicius.githubapi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.arch.toolkit.delegate.viewProvider
import com.vinicius.githubapi.R
import com.vinicius.githubapi.data.ui.Issue
import com.vinicius.githubapi.extension.openLink
import com.vinicius.githubapi.extension.toDateUS

class IssuesDiffUtil: DiffUtil.ItemCallback<Issue>() {
    override fun areItemsTheSame(oldItem: Issue, newItem: Issue): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Issue, newItem: Issue): Boolean {
        return oldItem == newItem
    }

}

class IssuesAdapter : PagingDataAdapter<Issue, RecyclerView.ViewHolder>(IssuesDiffUtil()) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is IssueViewHolder -> {
                val issue = getItem(position) ?: return
                holder.bind(issue)
            }
            else -> Unit
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.issue_item, parent, false)
        return IssueViewHolder(view)
    }
}

class IssueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: AppCompatTextView by viewProvider(R.id.title)
    private val author: AppCompatTextView by viewProvider(R.id.author)
    private val state: AppCompatTextView by viewProvider(R.id.state)
    private val access: AppCompatTextView by viewProvider(R.id.access)
    private val date: AppCompatTextView by viewProvider(R.id.date)

    fun bind(issue: Issue) {
        val context = itemView.context
        title.text = context.getString(R.string.issues_title, issue.title)
        author.text = context.getString(R.string.issues_author, issue.user.login)
        state.text = context.getString(R.string.issues_state, issue.state)
        issue.createdAt?.let {
            date.text = context.getString(R.string.issues_date, issue.createdAt.toDateUS())
        }

        date.isVisible = issue.createdAt.isNullOrEmpty().not()

        access.setOnClickListener {
            context.openLink(issue.htmlUrl)
        }
    }
}