package com.joohnq.crosbooks.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.joohnq.crosbooks.common.exceptions.EmailException
import com.joohnq.crosbooks.common.FieldValidation
import com.joohnq.crosbooks.common.exceptions.NameException
import com.joohnq.crosbooks.common.exceptions.PasswordConfirmException
import com.joohnq.crosbooks.common.exceptions.PasswordException
import com.joohnq.crosbooks.UiState.Companion.fold
import com.joohnq.crosbooks.view.helper.clearAllErrors
import com.joohnq.crosbooks.databinding.FragmentRegisterBinding
import com.joohnq.crosbooks.view.helper.SnackBarHelper
import com.joohnq.crosbooks.view.helper.onChange
import com.joohnq.crosbooks.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModel()
    private val onLoading: () -> Unit = { binding.toggleIsLoading(true) }
    private val onError: (String) -> Unit = {
        binding.toggleIsLoading(false)
        SnackBarHelper(requireView(), it)
    }

    private fun FragmentRegisterBinding.toggleIsLoading(state: Boolean) {
        isLoadingProgressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun FragmentRegisterBinding.clearAllErrors() {
        listOf(
            textInputLayoutName,
            textInputLayoutEmail,
            textInputLayoutPassword,
            textInputLayoutConfirmPassword
        ).clearAllErrors()
    }

    private fun FragmentRegisterBinding.onRegister() {
        clearAllErrors()
        val name = textEditTextName.text.toString()
        val email = textEditTextEmail.text.toString()
        val password = textEditTextPassword.text.toString()
        val confirmPassword = textEditTextConfirmPassword.text.toString()

        try {
            FieldValidation.validateName(name)
            FieldValidation.validateEmail(email)
            FieldValidation.validatePassword(password)
            FieldValidation.validatePasswordConfirm(password, confirmPassword)

            authViewModel.register(name, email, password, confirmPassword)
        } catch (e: NameException) {
            textInputLayoutName.error = e.message
        } catch (e: EmailException) {
            textInputLayoutEmail.error = e.message
        } catch (e: PasswordException) {
            textInputLayoutPassword.error = e.message
        } catch (e: PasswordConfirmException) {
            textInputLayoutConfirmPassword.error = e.message
        }
    }

    private fun FragmentRegisterBinding.bindButtons() {
        btnRegister.setOnClickListener { onRegister() }
        btnGotToLogin.setOnClickListener {
            findNavController().navigate(
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            )
        }
    }

    private fun FragmentRegisterBinding.observers() {
        authViewModel.register.observe(viewLifecycleOwner) { state ->
            state.fold(
                onLoading = onLoading,
                onError = onError,
                onSuccess = {
                    SnackBarHelper(requireView(), "Successfully registered")
                    toggleIsLoading(false)
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindButtons()
        binding.textEditTextName.setText("João")
        binding.textEditTextEmail.setText("joao@gmail.com")
        binding.textEditTextPassword.setText("joao1234")
        binding.textEditTextConfirmPassword.setText("joao1234")
        binding.observers()
        binding.whenInputValueChange()
    }

    private fun FragmentRegisterBinding.whenInputValueChange() {
        textEditTextName.onChange(textInputLayoutName)
        textEditTextEmail.onChange(textInputLayoutEmail)
        textEditTextPassword.onChange(textInputLayoutPassword)
        textEditTextConfirmPassword.onChange(textInputLayoutConfirmPassword)
    }
}