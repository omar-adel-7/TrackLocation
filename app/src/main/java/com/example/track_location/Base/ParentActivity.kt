package com.example.track_location.Base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.track_location.R
import com.example.track_location.databinding.ActParentBinding
import com.general.base_act_frg.myapp.BaseParentActivity
import com.general.utils.ITitleListener
import com.general.utils.KeyBoardUtil

open class ParentActivity : BaseParentActivity() {

    private lateinit var binding: ActParentBinding

    override fun getProgressBarView(): ViewGroup {
        return findViewById(R.id.view_progress_bar)
    }


    fun getDrawerLayout(): DrawerLayout {
        return binding.drawerLayout
    }

    override fun getTopBarParent(): LinearLayout {
        return binding.parentviewLnrTopBarParent
    }


    fun onMenuPress() {
        if (!getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            KeyBoardUtil.hideSoftKeyboard(this)
            getDrawerLayout().openDrawer(GravityCompat.START)
        } else
            closeDrawer()
    }


    fun openDrawer() {
        if (!getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            KeyBoardUtil.hideSoftKeyboard(this)
            getDrawerLayout().openDrawer(GravityCompat.START)
        }
    }

    fun closeDrawer() {
        if (getDrawerLayout().isDrawerOpen(GravityCompat.START))
            getDrawerLayout().closeDrawer(GravityCompat.START)
    }

    fun enableDrawerSwipe() {
        getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED)
    }


    fun disableDrawerSwipe() {
        getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    fun enableMenuFunction() {
        enableDrawerSwipe()
        showMenu()
        removeBack()
    }

    fun disableMenuFunction() {
        disableDrawerSwipe()
        removeMenu()
    }

    fun showDefaultCustomTitle() {
        enableMenuFunction()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActParentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDrawerLayout().addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            override fun onDrawerClosed(drawerView: View) {
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })
        enableMenuFunction()
        showTopBar()
        customTitle?.enableBtnsClick(object : ITitleListener {

            override fun onMenuPressed() {
                onMenuPress()
            }

            override fun onBackPressed() {
                this@ParentActivity.onBackPressed()
            }
        })
    }

    override fun onBackPressed() {
        closeDrawer()
        KeyBoardUtil.hideSoftKeyboard(this)

    }

}