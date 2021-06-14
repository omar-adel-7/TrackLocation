package com.general.base_act_frg.myapp

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.general.base_act_frg.BaseAppCompatActivity
import com.general.ui.custom_views.CustomTitle

open class BaseParentActivity : BaseAppCompatActivity() {

    var customTitle: CustomTitle? = null

    open fun getTopBarParent(): LinearLayout? {
        return null
    }

    open fun getProgressBarView(): ViewGroup? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        super.onCreate(savedInstanceState)
     }


    fun updateTitle(title: String?) {
        customTitle?.updateTitle(title)
    }


    fun removeMenu() {
        customTitle?.removeMenu()
    }

    fun showMenu() {
        customTitle?.showMenu()
    }

    fun showBack() {
        customTitle?.showBack()
    }
    fun removeBack() {
        customTitle?.removeBack()
    }


    open fun hideTopBar() {
        if (customTitle == null) {
            customTitle = CustomTitle(this)
        }
        getTopBarParent()?.removeAllViews()
    }

    open fun showTopBar() {
        if (customTitle == null) {
            customTitle = CustomTitle(this)
        }
        getTopBarParent()?.removeAllViews()
        getTopBarParent()?.addView(customTitle)
    }


    fun getCurrentFragment(): BaseFragment? {
        return supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.primaryNavigationFragment
                as BaseFragment?
    }




}