package com.general.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.general.R
import com.general.utils.AppFont.showAppFont
import javax.inject.Inject

class CustomProgressBar @Inject constructor() {
    private var dialog: Dialog? = null
     lateinit var dialogView: View
    var txt_message: TextView? = null
    var pbar_normal: ProgressBar? = null
    var pbar_progress: ProgressBar? = null
    var containerWhenNotDialog: ViewGroup? = null
    var showInDialog //if false then hasProgress ignored as we will treat it as true
            = false
    var dialogThemeResId = 0
    fun show(
        activity: AppCompatActivity,
        showInDialog: Boolean,
        containerWhenNotDialog: ViewGroup?,
        message: String?,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?,
        hasProgress: Boolean,
        dialogLayout: Int,
        dialogThemeResId: Int
    ) {
        var dialogLayout = dialogLayout
        this.showInDialog = showInDialog
        this.containerWhenNotDialog = containerWhenNotDialog
        this.dialogThemeResId = dialogThemeResId
        if (showInDialog) {
            if (containerWhenNotDialog != null) {
                containerWhenNotDialog.visibility = View.GONE
            }
            dialog = Dialog(activity, dialogThemeResId)
            if (activity.isDestroyed) {
                return
            }
            if (dialogLayout == 0) {
                dialogLayout = R.layout.dg_progress_bar
            } else {
                dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            val inflator = activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            dialogView = inflator.inflate(dialogLayout, null)
            txt_message = dialogView.findViewById(R.id.id_txt_message)
            pbar_normal = dialogView.findViewById(R.id.id_pbar_normal)
            pbar_progress = dialogView.findViewById(R.id.id_pbar_progress)
            if (message != null ) {
                showAppFont(activity, txt_message)
                txt_message?.text = message
            } else {
                txt_message?.visibility = View.GONE
            }
            dialog?.setContentView(dialogView)
            dialog?.setCancelable(cancelable)
            dialog?.setOnCancelListener(cancelListener)
            if (hasProgress) {
                pbar_progress?.visibility = View.VISIBLE
                pbar_normal?.visibility = View.GONE
            } else {
                pbar_progress?.visibility = View.GONE
                pbar_normal?.visibility = View.VISIBLE
            }
            dialog?.show()
        } else {
            if (containerWhenNotDialog != null) {
                containerWhenNotDialog.visibility = View.VISIBLE
                txt_message = containerWhenNotDialog.findViewById(R.id.id_txt_message)
                pbar_normal = containerWhenNotDialog.findViewById(R.id.id_pbar_normal)
                pbar_progress = containerWhenNotDialog.findViewById(R.id.id_pbar_progress)
                if (message != null ) {
                    showAppFont(activity, txt_message)
                    txt_message?.text = message
                } else {
                    txt_message?.visibility = View.GONE
                }
                if (hasProgress) {
                    pbar_progress?.visibility = View.VISIBLE
                    pbar_normal?.visibility = View.GONE
                } else {
                    pbar_progress?.visibility = View.GONE
                    pbar_normal?.visibility = View.VISIBLE
                }
            }
        }
    }

    fun dismissProgress(activity: Activity) {
        if (activity.isDestroyed) {
            return
        }
        if (showInDialog) {
                if (dialog?.isShowing == true) {
                    dialog?.dismiss()
                }
        } else {
                containerWhenNotDialog?.visibility = View.GONE
        }
    }
    fun setMessage(message: String?) {
        txt_message?.text = message
    }

    fun setProgress(progress: Int) {
        pbar_progress?.progress = progress
    }

    fun setIndeterminateForProgress(indeterminate: Boolean) {
            pbar_progress?.isIndeterminate = indeterminate
    }
}