package com.joohnq.crosbooks.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.joohnq.crosbooks.view.state.UiState.Companion.fold
import com.joohnq.crosbooks.common.FieldValidation
import com.joohnq.crosbooks.common.exceptions.CustomException
import com.joohnq.crosbooks.common.exceptions.EmailException
import com.joohnq.crosbooks.common.exceptions.PasswordConfirmException
import com.joohnq.crosbooks.common.exceptions.PasswordException
import com.joohnq.crosbooks.databinding.FragmentHomeBinding
import com.joohnq.crosbooks.databinding.FragmentRegisterBinding
import com.joohnq.crosbooks.view.helper.clearAllErrors
import com.joohnq.crosbooks.view.helper.initHorizontal
import com.joohnq.crosbooks.view.helper.initVerticalWithScrollEvent
import com.joohnq.crosbooks.view.helper.onChange
import com.joohnq.crosbooks.view.helper.showSnackBar
import com.joohnq.crosbooks.view.hideKeyboard
import com.joohnq.crosbooks.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModel()

    private fun FragmentRegisterBinding.onRegister() {
        listOf(
            textInputLayoutName,
            textInputLayoutEmail,
            textInputLayoutPassword,
            textInputLayoutConfirmPassword
        ).clearAllErrors()
        val name = textEditTextName.text.toString()
        val email = textEditTextEmail.text.toString()
        val password = textEditTextPassword.text.toString()
        val confirmPassword = textEditTextConfirmPassword.text.toString()

        try {
            FieldValidation.validateName(name)
            FieldValidation.validateEmail(email)
            FieldValidation.validatePassword(password)
            FieldValidation.validatePasswordConfirm(password, confirmPassword)

            requireActivity().hideKeyboard()

            authViewModel.register(name, email, password, confirmPassword)
        } catch (e: CustomException.NameCannotBeEmpty) {
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

    private fun observers() {
        authViewModel.register.observe(viewLifecycleOwner) { state ->
            state.fold(
                onLoading = { binding.isLoading = true },
                onError = {
                    binding.isLoading = false
                    requireView().showSnackBar(it)
                },
                onSuccess = {
                    requireView().showSnackBar("Successfully registered")
                    binding.isLoading = false
                }
            )
        }
    }

    private fun FragmentRegisterBinding.initTestFields() {
        textEditTextName.setText("João")
        textEditTextEmail.setText("joao@gmail.com")
        textEditTextPassword.setText("joao1234")
        textEditTextConfirmPassword.setText("joao1234")
    }

    private fun FragmentRegisterBinding.whenInputValueChange() {
        textEditTextName.onChange(textInputLayoutName)
        textEditTextEmail.onChange(textInputLayoutEmail)
        textEditTextPassword.onChange(textInputLayoutPassword)
        textEditTextConfirmPassword.onChange(textInputLayoutConfirmPassword)
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
        binding.apply {
            bindButtons()
            initTestFields()
            whenInputValueChange()
            isLoading = false
        }
        observers()
    }

}