package com.general.ui.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.general.databinding.CustomTitleBinding
import com.general.utils.AppFont.showAppFont
import com.general.utils.ITitleListener

class CustomTitle : LinearLayout {
    lateinit var listenerMain: ITitleListener
    lateinit var binding: CustomTitleBinding

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init(context)
    }

    private fun init(context: Context) {
        binding = CustomTitleBinding.inflate(LayoutInflater.from(context), this, true)
        updateTitleFont()
    }

    fun enableBtnsClick(titleListener: ITitleListener) {
        listenerMain = titleListener
        binding.customtitleBtnMenu.setOnClickListener { listenerMain.onMenuPressed() }
        binding.customtitleBtnBack.setOnClickListener { listenerMain.onBackPressed() }
    }

    fun updateTitleFont() {
        showAppFont(
            context, arrayOf(binding.customtitleTxtTitle), false
        )
    }

    fun updateTitle(title: String?) {
        binding.customtitleLTitle.visibility = VISIBLE
        binding.customtitleTxtTitle.text = title
        binding.customtitleTxtTitle.isSelected = true
    }

    fun showTitle() {
        binding.customtitleLTitle.visibility = VISIBLE
    }

    fun showBack() {
        binding.customtitleLBack.visibility = VISIBLE
    }

    fun removeBack() {
        binding.customtitleLBack.visibility = GONE
    }

    fun showMenu() {
        binding.customtitleLMenu.visibility = VISIBLE
    }

    fun removeMenu() {
        binding.customtitleLMenu.visibility = GONE
    }
}