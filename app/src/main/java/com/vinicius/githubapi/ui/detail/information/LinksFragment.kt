package com.vinicius.githubapi.ui.detail.information

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import br.com.arch.toolkit.delegate.viewProvider
import com.vinicius.githubapi.R
import com.vinicius.githubapi.extension.openLink
import com.vinicius.githubapi.ui.detail.LINKS_REPOSITORY_ARGS
import com.vinicius.githubapi.ui.detail.LINKS_USER_ARGS

class LinksFragment : Fragment(R.layout.links_fragment) {

    private val repositoryLink: AppCompatTextView by viewProvider(R.id.repository_link)
    private val ownerLink: AppCompatTextView by viewProvider(R.id.owner_link)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val linkOwner = it.getString(LINKS_USER_ARGS)
            val linkRepository = it.getString(LINKS_REPOSITORY_ARGS)

            repositoryLink.text = linkRepository
            ownerLink.text = linkOwner

            repositoryLink.setOnClickListener {
                requireContext().openLink(linkRepository)
            }

            ownerLink.setOnClickListener {
                requireContext().openLink(linkOwner)
            }
        }
    }

}