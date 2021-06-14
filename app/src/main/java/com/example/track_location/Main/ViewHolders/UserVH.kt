package com.example.track_location.Main.ViewHolders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.track_location.R
import com.example.track_location.databinding.RowUserBinding
import com.example.track_location.utils.AppUtil
import com.example.track_location.utils.IItemSelectListener
import com.general.data.bean.User
import com.general.utils.AppFont

class UserVH(var context: Context, itemView: View) : RecyclerView.ViewHolder(
    itemView
) {
    var myItemView: View
    var binding: RowUserBinding
    fun bindData(
        item: Any, position: Int,
        iItemSelectListener: IItemSelectListener
    ) {
        val user = item as User
        if (AppUtil.isMyUser(user)) {
            binding.rowTxtName.text = context.getString(R.string.you) + " : "+ user.name.trim { it <= ' ' }
        }
        else{
            binding.rowTxtName.text = user.name.trim { it <= ' ' }
        }
        binding.rowContainer.setOnClickListener { iItemSelectListener.onItemSelected(user) }
    }

    companion object {
        fun getView(context: Context, viewGroup: ViewGroup?): View {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            return layoutInflater.inflate(R.layout.row_user, viewGroup, false)
        }
    }

    init {
        this.myItemView = itemView
        binding = RowUserBinding.bind(itemView)
        AppFont.showAppFont(
            context, arrayOf<View>(binding.rowTxtName), false
        )
    }
}