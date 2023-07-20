package com.team.cat_hackathon.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.team.cat_hackathon.R
import com.team.cat_hackathon.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStore: DataStoreImpl

    private var isUserLoggedIn : Boolean? = null
    private var isOnBoardingFinished : Boolean? = null

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch(Dispatchers.IO) {
            isOnBoardingFinished = dataStore.getIsOnBoardingFinished()
            isUserLoggedIn = dataStore.getIsLoggedIn()
        }
        setupNavigation()
        setupBottomNavigationVisibility()
    }

    private fun setupBottomNavigationVisibility() {
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                //todo: add all fragments in bottom navigation
                R.id.homeFragment -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
                else -> {
                    bottomNavigationView.visibility = View.GONE
                }
            }
        }
    }

    private fun setupNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)
    }

    fun undoFullScreen() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.show(WindowInsetsCompat.Type.systemBars())
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }
    }

    fun handleFullScreen() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
    }

    suspend fun isOnBoardingFinished(): Boolean {
        return isOnBoardingFinished ?: dataStore.getIsOnBoardingFinished()
    }

    suspend fun isUserLoggedIn(): Boolean {
        return isUserLoggedIn ?: dataStore.getIsLoggedIn()
    }
}