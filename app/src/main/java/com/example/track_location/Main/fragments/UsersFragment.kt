package com.example.track_location.Main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.track_location.Base.MyBaseFragment
import com.example.track_location.Main.MainActivity
import com.example.track_location.Main.ViewHolders.UserVH
import com.example.track_location.Main.ViewModels.vm_fragments.UsersFrgVM
import com.example.track_location.R
import com.example.track_location.databinding.FrgUsersBinding
import com.example.track_location.utils.AppUtil.isMyUser
import com.example.track_location.utils.IItemSelectListener
import com.general.base_act_frg.myapp.BaseParentActivity
import com.general.data.bean.User
import com.general.firebase.FireBaseManager
import com.general.ui.adapters.GenericRecyclerViewAdapter
import com.general.ui.adapters.GenericRecyclerViewAdapter.AdapterDrawData

import java.util.*

class UsersFragment : MyBaseFragment(), IItemSelectListener {
    lateinit var frgVM: UsersFrgVM
    var adapter: GenericRecyclerViewAdapter<Any>? = null
    var layoutManager: LinearLayoutManager? = null
    private var _binding: FrgUsersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FrgUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initVmWork() {
        frgVM = ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getContainerActivity().application)
        )
                .get(UsersFrgVM::class.java)
    }

    override fun setWork() {
        (getContainerActivity() as BaseParentActivity).updateTitle(getString(R.string.menu_users))
        work()
    }

    override fun onResume() {
        super.onResume()
        (getContainerActivity() as BaseParentActivity).updateTitle(getString(R.string.menu_users))
    }

    private fun work() {
        layoutManager = LinearLayoutManager(
                getContainerActivity(),
                RecyclerView.VERTICAL, false
        )
        binding.rv.layoutManager = layoutManager
        if (adapter == null)
        {
            adapter = GenericRecyclerViewAdapter<Any>(getContainerActivity(),
                    object : AdapterDrawData() {
                        override fun getView(
                                parent: ViewGroup,
                                viewType: Int
                        ): RecyclerView.ViewHolder {
                            return UserVH(
                                    getContainerActivity(),
                                    UserVH.getView(getContainerActivity(), parent)
                            )
                        }

                        override fun bindView(
                                genericRecyclerViewAdapter: GenericRecyclerViewAdapter<*>?,
                                holder: RecyclerView.ViewHolder,
                                item: Any,
                                position: Int
                        ) {
                            (holder as UserVH).bindData(item, position, this@UsersFragment)
                        }
                    })
        }
        binding.rv.adapter = adapter
        binding.rv.setEmptyView(binding.viewEmptyList.emptyLnrView)
            frgVM.usersLiveList.observe(
                    viewLifecycleOwner,
                    { adapter?.setAll(getSortedList(frgVM.getUsersList())) })
        frgVM.usersLiveChanges.observe(viewLifecycleOwner, { user ->
            if (user?.remoteChildEvent == FireBaseManager.RemoteChildAddedEvent) {
                var found = false
                for (i in frgVM.getUsersList().indices) {
                    if (frgVM.getUsersList()[i]?.user_id
                            == user.user_id
                    ) {
                        found = true
                        break
                    }
                }
                if (!found) {
                    frgVM.getUsersList().add(user)
                    adapter?.setAll(getSortedList(frgVM.getUsersList()))
                }
            } else if (user?.remoteChildEvent == FireBaseManager.RemoteChildChangedEvent) {
                for (i in frgVM.getUsersList().indices) {
                    if (frgVM.getUsersList()[i]?.user_id
                            == user.user_id
                    ) {
                        frgVM.getUsersList()[i] = user
                        break
                    }
                }
                for (i in adapter?.list?.indices ?: mutableListOf()) {
                    if ((adapter?.getItem(i) as User).user_id ==
                            user.user_id) {
                        adapter?.updateItem(i, user)
                        break
                    }
                }

            }
        })
    }

    fun getSortedList(list: List<User?>): List<User?> {
        val arrayList = mutableListOf<User?>()
        arrayList.addAll(list)
        Collections.sort(
                arrayList,
                Comparator<User?> { o1, o2 ->
                    java.lang.Long.compare(
                            java.lang.Long.valueOf(o2.createdDate),
                            java.lang.Long.valueOf(o1.createdDate)
                    )
                })
        return arrayList
    }

    override fun onItemSelected(item: Any?) {
        val user = item as User?
        if (user != null) {
            if (!isMyUser(user)) {
                (getContainerActivity() as MainActivity).openOtherUserFragment(user)
            }
        }
    }


    override fun onStop() {
        super.onStop()
        frgVM.removeFirebaseListeners()
     }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}