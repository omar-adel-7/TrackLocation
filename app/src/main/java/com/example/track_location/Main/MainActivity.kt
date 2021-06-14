package com.example.track_location.Main


import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.track_location.Base.ParentActivity
import com.example.track_location.LocationMap.MapOtherUser.MapOtherUserFragment
import com.example.track_location.LocationMap.MapUsers.MapUsersFragment
import com.example.track_location.Main.ViewModels.vm_activities.MainActVM
import com.example.track_location.Main.fragments.LoginFragment
import com.example.track_location.R
import com.example.track_location.utils.AppUtil
import com.example.track_location.utils.MyAppSettings
import com.example.track_location.utils.MyAppSettings.saveFirstRun
import com.example.track_location.utils.MyUserData
import com.general.DialogOrPopUpList.MyDialogOrPopUpList
import com.general.DialogOrPopUpList.MyListItemClickListener
import com.general.DialogOrPopUpList.MyListModel
import com.general.data.bean.User
import com.general.utils.GeneralUtil
import com.general.utils.LanguageUtil
import com.general.utils.MyConstants
import java.util.*


class MainActivity : ParentActivity() {

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }
    private lateinit var mainActVM: MainActVM

    fun getMainActVM(): MainActVM {
        return mainActVM
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showDefaultCustomTitle()
        updateTitle(getString(R.string.app_name))
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        mainActVM = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(
                this.application
            )
        ).get(MainActVM::class.java)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setNavigationStartDestination()

        if (MyAppSettings.isFirstRun) {
            saveFirstRun(false)
            chooseLanguage(false)
        } else {
            languageChoosed()
        }
    }

    private fun setNavigationStartDestination() {
        val graph = navController.navInflater.inflate(R.navigation.mynavigation)
        if (MyUserData.isUserLoggedIn) {
            graph.startDestination = R.id.navMapUsersFrg
        } else {
            graph.startDestination = R.id.navLoginFrg
        }
        navController.graph = graph
    }


    fun chooseLanguage(fromMenu: Boolean) {
        val myDialogOrPopUpList = MyDialogOrPopUpList(this)
        val languages_codes =
            GeneralUtil.getLanguagesArrayCodes(this)
        val languages_names =
            GeneralUtil.getLanguagesArrayNames(this)
        for (i in languages_codes.indices) {
            if (languages_codes[i] == LanguageUtil.appLanguage) {
                myDialogOrPopUpList.setSelectedItemText(languages_names[i])
                break
            }
        }
        myDialogOrPopUpList.setselectedItemTxtColor(
            ContextCompat.getColor(
                this,
                R.color.colorPrimary
            )
        )
        val myListItemClickListener =
            MyListItemClickListener { model, position ->
                var selectedLanguageText = ""
                if (position != 0) {
                    selectedLanguageText = model.text
                }
                var languageChanged = false
                for (i in languages_names.indices) {
                    if (languages_names[i] == selectedLanguageText) {
                        if (LanguageUtil.appLanguage != languages_codes[i]) {
                            LanguageUtil.saveLanguage(languages_codes[i])
                            LanguageUtil.setAppLocale(this, languages_codes[i])
                            languageChanged = true
                        }
                        break
                    }
                }
                if (languageChanged) {
                    myDialogOrPopUpList.dismiss()
                    AppUtil.restartApplication(this)
                } else {
                    myDialogOrPopUpList.dismiss()
                    if (!fromMenu) {
                        languageChoosed()
                    }
                }
            }
        myDialogOrPopUpList.myListItemClickListener = myListItemClickListener
        val customPopUpWindowModels: ArrayList<MyListModel> = ArrayList()
        customPopUpWindowModels.add(
            MyListModel(
                this.getString(R.string.choose_language), 0
            )
        )
        for (i in languages_names.indices) {
            customPopUpWindowModels.add(
                MyListModel(
                    languages_names[i], 0
                )
            )
        }
        myDialogOrPopUpList.setHasHelp(true)
        myDialogOrPopUpList.setHelpTextColor(Color.BLACK)
        myDialogOrPopUpList.showPopup(null, customPopUpWindowModels, true,
            object : MyDialogOrPopUpList.CancelCallBack {
                override fun onCancelCallBack() {
                    if (!fromMenu) {
                        languageChoosed()
                    }
                }

            }
        )
    }

    private fun languageChoosed() {
        if (MyUserData.isUserLoggedIn) {
            observeMyUserDetails()
            openMainFragment()
        } else {
            openLoginFragment()
        }
    }

    fun observeMyUserDetails(): LiveData<User?> {
        return mainActVM.getMyUserDetails()
    }

    fun openLoginFragment() {
        openFragment(LoginFragment(), R.id.navLoginFrg)
    }

    fun openMainFragment() {
        openFragment(MapUsersFragment(), R.id.navMapUsersFrg)
    }

    fun loginCallBack() {
        if (MyUserData.isUserLoggedIn) {
            mainActVM.updateUserTokenId()
        }

        setNavigationStartDestination()

        openMainFragment()
    }


    fun openOtherUserFragment(user: User) {
        val fragment =
            MapOtherUserFragment()
        val bundle = Bundle()
        bundle.putSerializable(MyConstants.INPUT_KEY_FRAGMENT, user)
        fragment.arguments = bundle
        openFragment(fragment, R.id.navMapOtherUserFrg)
    }


    override fun onDestroy() {
        super.onDestroy()
        finishAffinity()
    }


    override fun onStop() {
        super.onStop()
        mainActVM.removeFirebaseListeners()
    }


    fun openFragment(fragment: Fragment, navActionId: Int) {
        if (getCurrentFragment() != null) {
            if (getCurrentFragment()?.javaClass?.name != fragment.javaClass.name) {
                navController.navigate(navActionId, fragment.arguments)
            }
        } else {
            navController.navigate(navActionId, fragment.arguments)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (getCurrentFragment() is LoginFragment
            || getCurrentFragment() is MapUsersFragment
        ) {
            finishAffinity()
        } else {
            navController.popBackStack()
        }
    }


}
