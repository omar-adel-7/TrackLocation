package com.example.track_location.Main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.example.track_location.Base.MyBaseFragment
import com.example.track_location.Main.MainActivity
import com.example.track_location.Main.ViewModels.vm_fragments.ProfileFrgVM
import com.example.track_location.R
import com.example.track_location.databinding.FrgProfileBinding
import com.example.track_location.utils.AppUtil.logOut
import com.example.track_location.utils.MyUserData.myUserDetails
import com.example.track_location.utils.MyUserData.saveUserName
import com.general.base_act_frg.myapp.BaseParentActivity
import com.general.utils.AppFont
import com.general.utils.BaseUtil
import com.general.utils.CustomAlertDialog
import com.general.utils.KeyBoardUtil
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class ProfileFragment : MyBaseFragment() {
    lateinit var frgVM: ProfileFrgVM
    lateinit var mAwesomeValidation: AwesomeValidation
     private var _binding: FrgProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrgProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initVmWork() {
        frgVM = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(getContainerActivity().application)
        )
            .get(ProfileFrgVM::class.java)
    }

    override fun setWork() {
        customAlertDialog = CustomAlertDialog(getContainerActivity())
        AppFont.showAppFont(
            getContainerActivity(),
            arrayOf(binding.etUserName, binding.btnUpdate, binding.btnLogOut)
        )
        (getContainerActivity() as BaseParentActivity).updateTitle(getString(R.string.menu_profile))
        work()
    }

    override fun onResume() {
        super.onResume()
        (getContainerActivity() as BaseParentActivity).updateTitle(getString(R.string.menu_profile))
        work()
    }

    private fun work() {
        addValidations()
        binding.btnUpdate.setOnClickListener {
            KeyBoardUtil.hideSoftKeyboard(getContainerActivity())
            if (mAwesomeValidation.validate()) {
                validationSucceeded()
            }
        }
        binding.btnLogOut.setOnClickListener { logOut(getContainerActivity()) }
        frgVM.myUser = myUserDetails
        displayUi()
        (getContainerActivity() as MainActivity).observeMyUserDetails()
            .observe(viewLifecycleOwner, { user ->
                frgVM.myUser = user
                displayUi()
            })
    }

    private fun displayUi() {
        binding.etUserName.setText(frgVM.myUser?.name)
    }

    private fun addValidations() {
        mAwesomeValidation = AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT)
        mAwesomeValidation.addValidation(
            binding.tiUserName,
            RegexTemplate.NOT_EMPTY,
            getString(R.string.err_name)
        )
    }

    private fun validationSucceeded() {
        frgVM.updateUserName(
            getContainerActivity(),
            binding.etUserName.text.toString()
        ).observe(viewLifecycleOwner, { responseBody ->
            if (responseBody != null) {
                try {
                    val jsonObj = JSONObject(responseBody.string())
                    val name = jsonObj.getString("name")
                    saveUserName(name)
                    frgVM.myUser = myUserDetails
                    customAlertDialog.alertDialog(getString(R.string.success_process))
                } catch (e: JSONException) {
                    BaseUtil.logMessage("TAG", e.toString())
                    customAlertDialog.alertDialog(getString(R.string.err_in_update_user))
                } catch (e: IOException) {
                    BaseUtil.logMessage("TAG", e.toString())
                    customAlertDialog.alertDialog(getString(R.string.err_in_update_user))
                }
            } else {
                customAlertDialog.alertDialog(getString(R.string.err_in_update_user))
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}