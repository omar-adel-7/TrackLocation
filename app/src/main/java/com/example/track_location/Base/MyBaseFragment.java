package com.example.track_location.Base;

import com.general.base_act_frg.myapp.BaseFragment;
import com.general.utils.CustomAlertDialog;

public abstract class MyBaseFragment
        extends BaseFragment {


    public CustomAlertDialog customAlertDialog;

    @Override
    public void onResume() {
        super.onResume();
        onCustomResume();
    }

    public void onCustomResume() {
         ((ParentActivity) getContainerActivity()).enableMenuFunction();
     }


 }