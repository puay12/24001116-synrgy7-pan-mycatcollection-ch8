package com.example.mycatcollections.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.mycatcollections.R
import com.example.mycatcollections.presentation.viewmodel.NavigatorViewModel

class NavigatorActivity : AppCompatActivity() {
    private val viewModel by viewModels<NavigatorViewModel> {
        NavigatorViewModel.provideFactory(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigator)

        viewModel.getIsLoggedIn().observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                MainActivity.startActivity(this)
            } else {
                LoginActivity.startActivity(this)
            }
        }
    }
}