package com.example.track_location.Main.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.example.track_location.Base.MyBaseFragment
import com.example.track_location.Main.MainActivity
import com.example.track_location.Main.ViewModels.vm_fragments.LoginFrgVM
import com.example.track_location.R
import com.example.track_location.databinding.FrgLoginBinding
import com.example.track_location.utils.MyUserData.saveUserId
import com.general.base_act_frg.myapp.BaseParentActivity
import com.general.utils.AppFont
import com.general.utils.CustomAlertDialog
import com.general.utils.KeyBoardUtil
import com.general.utils.MyConstants

class LoginFragment : MyBaseFragment() {
    lateinit  var frgVM: LoginFrgVM
    lateinit var mAwesomeValidation: AwesomeValidation
    private var _binding: FrgLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FrgLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initVmWork() {
        frgVM = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(getContainerActivity().application)
        )
            .get(LoginFrgVM::class.java)
    }

    override fun setWork() {
        customAlertDialog = CustomAlertDialog(getContainerActivity())
        AppFont.showAppFont(
            getContainerActivity(), arrayOf(
                binding.etUserEmail, binding.etPassword,
                binding.btnLogin, binding.btnRegister
            )
        )
        (getContainerActivity() as BaseParentActivity).updateTitle(getString(R.string.frg_login))
        work()
    }

    override fun onResume() {
        super.onResume()
        (getContainerActivity() as BaseParentActivity).updateTitle(getString(R.string.frg_login))
    }

    private fun work() {
        addValidations()
        binding.btnLogin.setOnClickListener {
            KeyBoardUtil.hideSoftKeyboard(getContainerActivity())
            if (mAwesomeValidation.validate()) {
                validationSucceeded()
            }
        }
        binding.btnRegister.setOnClickListener {
            (getContainerActivity() as MainActivity).openFragment(
                RegisterFragment(),
                R.id.navRegisterFrg
            )
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
    }

    private fun validationSucceeded() {
        frgVM.login(
            getContainerActivity(), binding.etUserEmail.text.toString(),
            binding.etPassword.text.toString()
        ).observe(viewLifecycleOwner, { authResponse ->
            if (authResponse != null) {
                saveUserId(authResponse.localId)
                (getContainerActivity() as MainActivity).observeMyUserDetails().observe(
                    viewLifecycleOwner,
                    { (getContainerActivity() as MainActivity).loginCallBack() })
            } else {
                customAlertDialog.alertDialog(getString(R.string.error_login))
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}