package com.example.track_location.Main.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.example.track_location.Base.MyBaseFragment
import com.example.track_location.Base.ParentActivity
import com.example.track_location.Main.ViewModels.vm_fragments.RegisterFrgVM
import com.example.track_location.R
import com.example.track_location.databinding.FrgRegisterBinding
import com.general.base_act_frg.myapp.BaseParentActivity
import com.general.utils.AppFont
import com.general.utils.CustomAlertDialog
import com.general.utils.KeyBoardUtil
import com.general.utils.MyConstants

class RegisterFragment : MyBaseFragment() {
    lateinit var frgVM: RegisterFrgVM
    lateinit var mAwesomeValidation: AwesomeValidation
     private var _binding: FrgRegisterBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrgRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initVmWork() {
        frgVM = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(getContainerActivity().application)
        )
            .get(RegisterFrgVM::class.java)
    }

    override fun setWork() {
        customAlertDialog = CustomAlertDialog(getContainerActivity())
        AppFont.showAppFont(
            getContainerActivity(), arrayOf(
                binding.etUserEmail, binding.etPassword,
                binding.etConfirmPassword, binding.etUserName, binding.btnNewMembership
            )
        )
        (getContainerActivity() as BaseParentActivity).updateTitle(getString(R.string.frg_register))
        work()
    }

    override fun onCustomResume() {
        (getContainerActivity() as ParentActivity).disableMenuFunction()
        (getContainerActivity() as BaseParentActivity).showBack()
        (getContainerActivity() as BaseParentActivity).updateTitle(getString(R.string.frg_register))
    }

    private fun work() {
        addValidations()
        binding.btnNewMembership.setOnClickListener {
            KeyBoardUtil.hideSoftKeyboard(getContainerActivity())
            if (mAwesomeValidation.validate()) {
                validationSucceeded()
            }
        }
    }

    private fun addValidations() {
        mAwesomeValidation = AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT)
        mAwesomeValidation.addValidation(
            binding.tiUserEmail,
            Patterns.EMAIL_ADDRESS,
            getString(R.string.err_email)
        )
        mAwesomeValidation.addValidation(
            binding.tiPassword,
            MyConstants.PASSWORD_PATTERN,
            getString(R.string.password_min_length_error)
        )
        mAwesomeValidation.addValidation(
            binding.tiConfirmPassword,
            binding.tiPassword,
            getString(R.string.password_mismatch)
        )
        mAwesomeValidation.addValidation(
            binding.tiUserName,
            RegexTemplate.NOT_EMPTY,
            getString(R.string.err_name)
        )
    }

    private fun validationSucceeded() {
        frgVM.register(
            getContainerActivity(), binding.etUserEmail.text.toString(),
            binding.etPassword.text.toString()
        ).observe(viewLifecycleOwner, { authResponse ->
            if (authResponse != null) {
                frgVM.createUser(
                    getContainerActivity(), authResponse.localId, authResponse.email,
                    binding.etUserName.text.toString()
                ).observe(
                    viewLifecycleOwner, { user ->
                        if (user != null) {
                            getContainerActivity().onBackPressed()
                        }
                    })
            } else {
                customAlertDialog.alertDialog(getString(R.string.err_in_register))
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}