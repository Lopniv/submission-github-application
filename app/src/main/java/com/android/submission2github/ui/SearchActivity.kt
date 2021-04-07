package com.android.submission2github.ui

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.android.submission2github.databinding.ActivitySearchBinding
import com.android.submission2github.utils.Utils

class SearchActivity : AppCompatActivity() {

    private lateinit var b : ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(b.root)
        inputUsername()
    }

    private fun inputUsername(){
        b.etSearchUser.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    search()
                    true
                }
                else -> false
            }
        }
    }

    private fun search() {
        when {
            b.etSearchUser.text.isNullOrEmpty() ->
                Utils.showSnackbarMessage(b.root, "Fill the input field!")
            else -> {
                val search = Intent(this, ListUserActivity::class.java)
                search.putExtra("USERNAME", b.etSearchUser.text.toString().trim())
                startActivity(search)
            }
        }
    }
}