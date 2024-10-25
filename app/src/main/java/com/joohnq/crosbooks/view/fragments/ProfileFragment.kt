package com.joohnq.crosbooks.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.joohnq.crosbooks.R
import com.joohnq.crosbooks.common.exceptions.CustomException
import com.joohnq.crosbooks.databinding.FragmentProfileBinding
import com.joohnq.crosbooks.view.helper.showSnackBar
import com.joohnq.crosbooks.view.navigation.navigateToAuthActivity
import com.joohnq.crosbooks.viewmodel.UserPreferencesViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!
    private val userPreferencesViewModel: UserPreferencesViewModel by viewModel()
    private val ioDispatcher: CoroutineDispatcher by inject()

    private fun FragmentProfileBinding.bindButtons() {
        topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.logoutAction -> {
                    try {
                        lifecycleScope.launch(ioDispatcher) {
                            val res = userPreferencesViewModel.resetToken()
                            if (!res) throw CustomException.LogoutFailed()
                            navigateToAuthActivity()
                        }
                    } catch (e: Exception) {
                        root.showSnackBar(e.message.toString())
                    }
                    true
                }

                else -> false
            }
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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindButtons()
    }
}