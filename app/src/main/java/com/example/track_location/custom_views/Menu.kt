package com.example.track_location.custom_views

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import com.example.track_location.Base.ParentActivity
import com.example.track_location.Main.MainActivity
import com.example.track_location.Main.fragments.ProfileFragment
import com.example.track_location.Main.fragments.UsersFragment
import com.example.track_location.R
import com.example.track_location.databinding.MenuBinding
import com.example.track_location.utils.MyUserData
import com.general.utils.AppFont
import com.general.utils.KeyBoardUtil

class Menu : LinearLayout {

    lateinit var binding: MenuBinding

    constructor(context: Context?) : super(context) {
        initView()
        doWork()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
        doWork()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
        doWork()
    }

    private fun initView() {
        binding = MenuBinding.inflate(
            LayoutInflater.from(
                context
            ), this, true
        )
    }

    private fun doWork() {
        AppFont.showAppFont(
            context, arrayOf<View>(
                binding.txtMapUsers,
                binding.txtUsers,
                binding.txtProfile,
                binding.txtLanguage
            ), false
        )
        binding.lMapUsers.setOnClickListener(OnClickListener {
            KeyBoardUtil.hideSoftKeyboard(context as Activity)
            (context as ParentActivity).closeDrawer()
            (context as MainActivity).openMainFragment()
        })
        binding.lUsers.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                KeyBoardUtil.hideSoftKeyboard(context as Activity)
                (context as ParentActivity).closeDrawer()
                (context as MainActivity).openFragment(UsersFragment(), R.id.navUsersFrg)
            }
        })
        binding.lProfile.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                KeyBoardUtil.hideSoftKeyboard(context as Activity)
                (context as ParentActivity).closeDrawer()
                if (MyUserData.isUserLoggedIn) {
                    (context as MainActivity).openFragment(ProfileFragment(), R.id.navProfileFrg)
                 } else {
                    (context as MainActivity).openLoginFragment()
                }
            }
        })
        binding.lLanguage.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                KeyBoardUtil.hideSoftKeyboard(context as Activity)
                (context as ParentActivity).closeDrawer()
                (context as MainActivity).chooseLanguage(true)
            }
        })
    }
}