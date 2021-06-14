package com.general.base_act_frg.myapp

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    lateinit var myActivity: AppCompatActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity = context as AppCompatActivity
    }

    fun getContainerActivity(): AppCompatActivity {
        return myActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVmWork()
        setWork()
    }

    abstract fun initVmWork()
    abstract fun setWork()


}