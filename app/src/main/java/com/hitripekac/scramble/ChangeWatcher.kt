package com.hitripekac.scramble

import android.text.Editable
import android.text.TextWatcher

class ChangeWatcher(private val runner: () -> Unit) : TextWatcher {
    override fun afterTextChanged(p0: Editable?) = Unit
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
    override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) = runner()
}