package com.general.utils

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView

object AppFont {
    var FontEnglish = "fonts/ubuntu_regular.ttf"
    var FontArabic = "fonts/cairo_semi_bold.ttf"
    val fontAppDefault: String
        get() = if (LanguageUtil.appLanguage == LanguageUtil.ARABIC_LANGUAGE) {
            FontArabic
        } else {
            FontEnglish
        }

    fun getFont(context: Context, name: String?): Typeface {
        return Typeface.createFromAsset(context.assets, name)
    }

    fun getFont(context: Context, name: String?, isBold: Boolean): Typeface {
        var typeface = Typeface.createFromAsset(context.assets, name)
        if (isBold) {
            typeface = Typeface.create(typeface, Typeface.BOLD)
        }
        return typeface
    }

    fun setViewsFont(views: Array<View?>, name: String?, context: Context) {
        val typeface = getFont(context, name)
        for (t in views) {
            if (t != null) {
                if (t is Button) {
                    t.typeface = typeface
                } else if (t is TextView) {
                    t.typeface = typeface
                } else if (t is EditText) {
                    t.typeface = typeface
                } else if (t is RadioButton) {
                    t.typeface = typeface
                }
            }
        }
    }

    fun setViewsFont(views: Array<View>, name: String?, context: Context, isBold: Boolean) {
        val typeface = getFont(context, name, isBold)
        for (t in views) {
                if (t is Button) {
                    t.typeface = typeface
                } else if (t is TextView) {
                    t.typeface = typeface
                } else if (t is EditText) {
                    t.typeface = typeface
                }
        }
    }

    fun showAppFont(context: Context, view: View?) {
        setViewsFont(
            arrayOf(view), fontAppDefault,
            context
        )
    }

    fun showAppFont(context: Context, views: Array<View?>) {
        setViewsFont(
            views, fontAppDefault,
            context
        )
    }

    fun showAppFont(context: Context, view: View, isBold: Boolean) {
        setViewsFont(
            arrayOf(view), fontAppDefault,
            context, isBold
        )
    }

    fun showAppFont(context: Context, views: Array<View>, isBold: Boolean) {
        setViewsFont(
            views, fontAppDefault,
            context, isBold
        )
    }


}