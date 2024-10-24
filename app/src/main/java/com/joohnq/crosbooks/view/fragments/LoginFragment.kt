package com.joohnq.crosbooks.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.joohnq.crosbooks.common.exceptions.EmailException
import com.joohnq.crosbooks.common.FieldValidation
import com.joohnq.crosbooks.common.exceptions.PasswordException
import com.joohnq.crosbooks.UiState.Companion.fold
import com.joohnq.crosbooks.view.helper.clearAllErrors
import com.joohnq.crosbooks.databinding.FragmentLoginBinding
import com.joohnq.crosbooks.view.helper.onChange
import com.joohnq.crosbooks.view.activities.HomeActivity
import com.joohnq.crosbooks.view.helper.SnackBarHelper
import com.joohnq.crosbooks.viewmodel.AuthViewModel
import com.joohnq.crosbooks.viewmodel.UserPreferencesViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModel()
    private val userPreferencesViewModel: UserPreferencesViewModel by viewModel()
    private val onLoading: () -> Unit = { binding.toggleIsLoading(true) }
    private val onError: (String) -> Unit = {
        binding.toggleIsLoading(false)
        SnackBarHelper(binding.root, it)
    }

    private fun FragmentLoginBinding.toggleIsLoading(state: Boolean) {
        isLoadingProgressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun FragmentLoginBinding.clearAllErrors() {
        listOf(
            textInputLayoutEmail,
            textInputLayoutPassword,
        ).clearAllErrors()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun FragmentLoginBinding.onLogin() {
        clearAllErrors()
        val email = textEditTextEmail.text.toString()
        val password = textEditTextPassword.text.toString()

        try {
            FieldValidation.validateEmail(email)
            FieldValidation.validatePassword(password)

            lifecycleScope.launch {
                try {
                    val token = authViewModel.login(email, password)
                    val res = userPreferencesViewModel.setToken(token)
                    if (!res) throw Exception("Failed to save token")

                    Intent(requireContext(), HomeActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                } catch (e: Exception) {
                    onError(e.message.toString())
                }
            }
        } catch (e: EmailException) {
            textInputLayoutEmail.error = e.message
        } catch (e: PasswordException) {
            textInputLayoutPassword.error = e.message
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun FragmentLoginBinding.bindButtons() {
        btnLogin.setOnClickListener { onLogin() }
        btnGotToRegister.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        }
    }

    private fun FragmentLoginBinding.observers() {
        authViewModel.login.observe(viewLifecycleOwner) { state ->
            state.fold(
                onLoading = onLoading,
                onError = onError,
                onSuccess = {
                    SnackBarHelper(requireView(), "Successfully login")
                    toggleIsLoading(false)
                }
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindButtons()
        binding.textEditTextEmail.setText("joao@gmail.com")
        binding.textEditTextPassword.setText("joao1234")
        binding.observers()
        binding.whenInputValueChange()
    }

    private fun FragmentLoginBinding.whenInputValueChange() {
        textEditTextEmail.onChange(textInputLayoutEmail)
        textEditTextPassword.onChange(textInputLayoutPassword)
    }
}