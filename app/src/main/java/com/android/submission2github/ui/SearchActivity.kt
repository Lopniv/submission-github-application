package com.android.submission2github.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.android.submission2github.R
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        inputUsername()
    }

    private fun inputUsername(){
        et_search_user.setOnEditorActionListener { v, actionId, event ->
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
            et_search_user.text.isNullOrEmpty() ->
                Toast.makeText(this, "Fill the input field!", Toast.LENGTH_SHORT).show()
            else -> {
                val search = Intent(this, ListUserActivity::class.java)
                search.putExtra("USERNAME", et_search_user.text.toString().trim())
                startActivity(search)
            }
        }
    }
}