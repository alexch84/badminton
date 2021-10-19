package com.example.badmintonapp.utils

import android.text.Editable

class ViewUtils {

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

}