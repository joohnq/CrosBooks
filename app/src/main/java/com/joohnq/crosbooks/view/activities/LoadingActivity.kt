package com.joohnq.crosbooks.view.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.joohnq.crosbooks.ConnectivityManager
import com.joohnq.crosbooks.databinding.ActivityLoadingBinding
import com.joohnq.crosbooks.view.navigation.navigateToActivity
import com.joohnq.crosbooks.view.setOnApplyWindowInsetsListener
import com.joohnq.crosbooks.view.state.UiState.Companion.onSuccess
import com.joohnq.crosbooks.viewmodel.UserPreferencesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoadingActivity : AppCompatActivity() {
    private var _binding: ActivityLoadingBinding? = null
    private val binding: ActivityLoadingBinding get() = _binding!!
    private val userPreferencesViewModel: UserPreferencesViewModel by viewModel()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.isError = false
        binding.root.setOnApplyWindowInsetsListener()
        val hasInternet = ConnectivityManager.isInternetAvailable(this@LoadingActivity)
        if (hasInternet) {
            userPreferencesViewModel.getToken()
            observers()
        }else{
            binding.isError = true
        }
    }

    private fun observers() {
        userPreferencesViewModel.token.observe(this@LoadingActivity) { state ->
            state.onSuccess { token ->
                val activity =
                    if (token.isNullOrEmpty()) AuthActivity::class.java else HomeActivity::class.java
                navigateToActivity(activity, true)
            }
        }
    }
}