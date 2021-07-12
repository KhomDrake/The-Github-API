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
import com.vinicius.githubapi.data.ui.User

class UserDiffUtil: DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}

class UserAdapter(
    private val onClickUserHome: (User) -> Unit
) : PagingDataAdapter<User, RecyclerView.ViewHolder>(UserDiffUtil()) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? UserViewHolder)?.run {
            val user = getItem(position) ?: return
            bind(user)
            userHome.setOnClickListener {
                onClickUserHome.invoke(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.user_item, parent, false
        )
        return UserViewHolder(view)
    }

}

class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val avatarUrl: AppCompatImageView by viewProvider(R.id.avatar_url)
    private val login: AppCompatTextView by viewProvider(R.id.login)
    val userHome: AppCompatTextView by viewProvider(R.id.user_home)

    fun bind(user: User) {
        avatarUrl.load(user.avatarUrl) {
            crossfade(1000)
        }
        login.text = user.login
    }
}
