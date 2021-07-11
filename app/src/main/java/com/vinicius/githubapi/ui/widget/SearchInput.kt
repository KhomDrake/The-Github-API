package com.vinicius.githubapi.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import br.com.arch.toolkit.delegate.viewProvider
import com.vinicius.githubapi.R

class SearchInput : ConstraintLayout {

    private val input: AppCompatEditText by viewProvider(R.id.search_text)
    private val search: AppCompatImageView by viewProvider(R.id.search_icon)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.search_input, this, true)
        background = ContextCompat.getDrawable(context, R.drawable.background_rounded_white_big)
        search.isVisible = false

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.SearchInput)

        input.hint = attributes.getString(R.styleable.SearchInput_android_hint)

        val minToShow = attributes.getInteger(R.styleable.SearchInput_minToSearch, 4)
        input.doOnTextChanged { text, _, _, _ ->
            search.isVisible = (text?.length ?: 0) >= minToShow
        }

        attributes.recycle()
    }

    fun onClickSearch(onClick: (String) -> Unit) {
        search.setOnClickListener {
            onClick.invoke(input.text.toString())
            input.setText("")
        }
    }
}
