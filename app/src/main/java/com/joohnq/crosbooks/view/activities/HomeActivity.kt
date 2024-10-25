package com.joohnq.crosbooks.view.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.joohnq.crosbooks.R
import com.joohnq.crosbooks.databinding.ActivityHomeBinding
import com.joohnq.crosbooks.model.entities.Book
import com.joohnq.crosbooks.view.helper.showSnackBar
import com.joohnq.crosbooks.view.setOnApplyWindowInsetsListener
import com.joohnq.crosbooks.view.state.UiState
import com.joohnq.crosbooks.view.state.UiState.Companion.toRecyclerViewState
import com.joohnq.crosbooks.viewmodel.BooksViewModel
import com.joohnq.crosbooks.viewmodel.CategoriesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding: ActivityHomeBinding get() = _binding!!
    private val booksViewModel: BooksViewModel by viewModel()
    private val categoriesViewModel: CategoriesViewModel by viewModel()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        booksViewModel.getAllBooks()
    }

    private fun ActivityHomeBinding.setUpBottomNavigation(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentHome) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.setOnApplyWindowInsetsListener(true)
        binding.setUpBottomNavigation()
        categoriesViewModel.getCategories()
    }
}