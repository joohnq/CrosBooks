package com.joohnq.crosbooks.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.joohnq.crosbooks.view.state.UiState.Companion.fold
import com.joohnq.crosbooks.common.FieldValidation
import com.joohnq.crosbooks.common.exceptions.EmailException
import com.joohnq.crosbooks.common.exceptions.PasswordException
import com.joohnq.crosbooks.databinding.FragmentLoginBinding
import com.joohnq.crosbooks.view.helper.clearAllErrors
import com.joohnq.crosbooks.view.helper.onChange
import com.joohnq.crosbooks.view.helper.showSnackBar
import com.joohnq.crosbooks.view.hideKeyboard
import com.joohnq.crosbooks.view.navigation.navigateToHomeActivity
import com.joohnq.crosbooks.viewmodel.AuthViewModel
import com.joohnq.crosbooks.viewmodel.UserPreferencesViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModel()
    private val userPreferencesViewModel: UserPreferencesViewModel by viewModel()

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

            requireActivity().hideKeyboard()

            lifecycleScope.launch {
                try {
                    val token = authViewModel.login(email, password)
                    val res = userPreferencesViewModel.setToken(token)
                    if (!res) throw Exception("Failed to save token")
                    navigateToHomeActivity()
                } catch (e: Exception) {
                    requireView().showSnackBar(e.message.toString())
                }
            }
        } catch (e: EmailException) {
            textInputLayoutEmail.error = e.message
        } catch (e: PasswordException) {
            textInputLayoutPassword.error = e.message
        }
    }

    private fun FragmentLoginBinding.bindButtons() {
        btnLogin.setOnClickListener { onLogin() }
        btnGotToRegister.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        }
    }

    private fun observers() {
        authViewModel.login.observe(viewLifecycleOwner) { state ->
            state.fold(
                onLoading = { binding.isLoading = true },
                onError = {
                    binding.isLoading = false
                    requireView().showSnackBar(it)
                },
                onSuccess = {
                    requireView().showSnackBar("Successfully login")
                    binding.isLoading = false
                }
            )
        }
    }

    private fun FragmentLoginBinding.whenInputValueChange() {
        textEditTextEmail.onChange(textInputLayoutEmail)
        textEditTextPassword.onChange(textInputLayoutPassword)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            bindButtons()
            whenInputValueChange()
            isLoading = false
        }
        observers()
    }
}