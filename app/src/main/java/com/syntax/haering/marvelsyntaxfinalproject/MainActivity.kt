package com.syntax.haering.marvelsyntaxfinalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Bottom navigation section
        val navController = this.findNavController(R.id.fragmentContainerView)
        val navView: BottomNavigationView = findViewById(R.id.activityMain_bottomNavigation)
        navView.setupWithNavController(navController)

        viewModel.apiStatus.observe(this){
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (
                    destination.id == R.id.detailCharacterFragment ||
                    destination.id == R.id.detailSerieFragment ||
                    destination.id == R.id.detailComicFragment ||
                    destination.id == R.id.signInFragment ||
                    destination.id == R.id.registerFragment ||
                    it == HomeViewModel.APIStatus.LOADING
                ) {
                    navView.visibility = View.GONE
                } else {
                    navView.visibility = View.VISIBLE
                }
            }
        }

    }
}