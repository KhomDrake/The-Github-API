package com.vinicius.githubapi.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import br.com.arch.toolkit.delegate.viewProvider
import com.vinicius.githubapi.R

class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val errorText: AppCompatTextView by viewProvider(R.id.error_text)
    private val errorTitle: AppCompatTextView by viewProvider(R.id.error_title)
    private val errorButton: AppCompatButton by viewProvider(R.id.error_button)

    init {
        View.inflate(context, R.layout.error_view, this)
        setupAttributes(context, attrs)
    }

    fun setOnButtonErrorListener(l: OnClickListener?) {
        errorButton.setOnClickListener(l)
    }

    private fun setupAttributes(context: Context, set: AttributeSet?) {
        val attrs = context.obtainStyledAttributes(set, R.styleable.ErrorView)

        if(attrs.hasValue(R.styleable.ErrorView_errorTitle))
            errorTitle.text = attrs.getText(R.styleable.ErrorView_errorTitle)

        if(attrs.hasValue(R.styleable.ErrorView_errorBody))
            errorText.text = attrs.getText(R.styleable.ErrorView_errorBody)

        attrs.recycle()
    }

    fun showButton(showButton: Boolean) {
        errorButton.isVisible = showButton
    }

    fun setTitle(@StringRes text: Int) {
        errorTitle.text = context.getString(text)
    }

    fun setBody(@StringRes text: Int) {
        errorText.text = context.getString(text)
    }

    fun setTitle(text: String) {
        errorTitle.text = text
    }

    fun setBody(text: String) {
        errorText.text = text
    }

    fun default() {
        setTitle(R.string.error_view_title_default)
        setBody(R.string.error_view_body_default)
        showButton(true)
    }
}