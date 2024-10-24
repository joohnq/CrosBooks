package com.joohnq.crosbooks.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.joohnq.crosbooks.R
import com.joohnq.crosbooks.UiState.Companion.onSuccess
import com.joohnq.crosbooks.databinding.ActivityLoadingBinding
import com.joohnq.crosbooks.view.setOnApplyWindowInsetsListener
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
        binding.root.setOnApplyWindowInsetsListener()
        userPreferencesViewModel.getToken()
        observers()
    }

    private fun observers() {
        userPreferencesViewModel.token.observe(this) { state ->
            Log.i("CROSBOOKS - MY TAG", state.toString())
            state.onSuccess { token ->
                val activity = if (token.isNullOrEmpty()) AuthActivity() else HomeActivity()
                Intent(this, activity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        }
    }
}