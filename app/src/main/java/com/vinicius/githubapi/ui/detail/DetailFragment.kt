package com.vinicius.githubapi.ui.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.arch.toolkit.delegate.viewProvider
import coil.load
import com.google.android.material.tabs.TabLayout
import com.vinicius.githubapi.R
import com.vinicius.githubapi.data.ui.Repository
import com.vinicius.githubapi.ui.detail.information.CommitsFragment
import com.vinicius.githubapi.ui.detail.information.IssuesFragment
import com.vinicius.githubapi.ui.detail.information.LicenseFragment
import com.vinicius.githubapi.ui.detail.information.LinksFragment

enum class RepositoryInformation {
    COMMITS,
    ISSUES,
    LICENSE,
    LINKS
}

const val LICENSE_ARGS = "LICENSE_ARGS"
const val LINKS_REPOSITORY_ARGS = "LINKS_REPOSITORY_ARGS"
const val LINKS_USER_ARGS = "LINKS_USER_ARGS"
const val REPOSITORY_FULL_NAME_ARGS = "REPOSITORY_FULL_NAME_ARGS"
const val LANGUAGE_REPOSITORY_ARGS = "LANGUAGE_REPOSITORY_ARGS"

class DetailFragment : Fragment(R.layout.detail_fragment) {

    private val name: AppCompatTextView by viewProvider(R.id.name)
    private val fullName: AppCompatTextView by viewProvider(R.id.full_name)
    private val description: AppCompatTextView by viewProvider(R.id.description)
    private val image: AppCompatImageView by viewProvider(R.id.image)
    private val score: AppCompatTextView by viewProvider(R.id.score)
    private val watchers: AppCompatTextView by viewProvider(R.id.watchers)
    private val openIssues: AppCompatTextView by viewProvider(R.id.open_issues)
    private val stars: AppCompatTextView by viewProvider(R.id.stars)
    private val tabLayout: TabLayout by viewProvider(R.id.tab_information)
    private val args: DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateInformationRepository(args.repository)
        setupTabLayout(args.repository)
    }

    private fun setupTabLayout(repository: Repository) {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val fragment: Fragment? = when(tab.position) {
                    RepositoryInformation.COMMITS.ordinal -> {
                        CommitsFragment().apply {
                            arguments = Bundle().apply {
                                putString(REPOSITORY_FULL_NAME_ARGS, repository.fullName)
                                putString(LANGUAGE_REPOSITORY_ARGS, repository.language)
                            }
                        }
                    }
                    RepositoryInformation.ISSUES.ordinal -> {
                        IssuesFragment().apply {
                            arguments = Bundle().apply {
                                putString(REPOSITORY_FULL_NAME_ARGS, repository.fullName)
                            }
                        }
                    }
                    RepositoryInformation.LICENSE.ordinal -> {
                        LicenseFragment().apply {
                            arguments = Bundle().apply {
                                putParcelable(LICENSE_ARGS, repository.license)
                            }
                        }
                    }
                    RepositoryInformation.LINKS.ordinal -> {
                        LinksFragment().apply {
                            arguments = Bundle().apply {
                                putString(LINKS_REPOSITORY_ARGS, repository.url)
                                putString(LINKS_USER_ARGS, repository.owner.url)
                            }
                        }
                    }
                    else -> null
                }

                fragment?.let {
                    childFragmentManager.beginTransaction().apply {
                        replace(R.id.container, it)
                    }.commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

            override fun onTabReselected(tab: TabLayout.Tab?) = Unit

        })

        val fragment = CommitsFragment().apply {
            arguments = Bundle().apply {
                putString(REPOSITORY_FULL_NAME_ARGS, repository.fullName)
                putString(LANGUAGE_REPOSITORY_ARGS, repository.language)
            }
        }

        childFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
        }.commit()

    }

    private fun updateInformationRepository(repository: Repository) {
        name.text = repository.name
        fullName.text = repository.fullName
        description.text = repository.description
        score.text = getString(R.string.detail_score, repository.score.toString())
        watchers.text = getString(R.string.detail_watcher, repository.watchers.toString())
        openIssues.text = getString(R.string.detail_open_issues, repository.openIssues.toString())
        stars.text = getString(R.string.detail_stars, repository.stars)

        image.load(repository.owner.avatarUrl) {
            crossfade(true)
        }
    }

}