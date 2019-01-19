/*
 * Copyright 2018 Stéphane Baiget
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cinema.entract.app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.cinema.entract.app.NavAction
import com.cinema.entract.app.NavOrigin
import com.cinema.entract.app.NavState
import com.cinema.entract.app.NavigationViewModel
import com.cinema.entract.app.R
import com.cinema.entract.app.ui.details.DetailsFragment
import com.cinema.entract.app.ui.event.EventDialogFragment
import com.cinema.entract.app.ui.information.InformationFragment
import com.cinema.entract.app.ui.onscreen.OnScreenFragment
import com.cinema.entract.app.ui.schedule.ScheduleFragment
import com.cinema.entract.app.ui.settings.SettingsFragment
import com.cinema.entract.app.ui.settings.SettingsViewModel
import com.cinema.entract.core.ext.observe
import com.cinema.entract.core.ext.replaceFragment
import com.cinema.entract.core.ui.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.jetbrains.anko.find
import org.koin.androidx.viewmodel.ext.viewModel

class CinemaActivity : BaseActivity() {

    private val cinemaViewModel by viewModel<CinemaViewModel>()
    private val navViewModel by viewModel<NavigationViewModel>()
    private val tagViewModel by viewModel<TagViewModel>()
    private val settingsViewModel by viewModel<SettingsViewModel>()

    private var onScreenState: CinemaState.OnScreen? = null
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(
            if (settingsViewModel.isDarkMode()) R.style.Theme_Cinema_Dark
            else R.style.Theme_Cinema_Light
        )
        setContentView(R.layout.activity_cinema)
        initBottomNavigation()

        savedInstanceState ?: showFragment(OnScreenFragment.newInstance())
        observe(cinemaViewModel.state, ::renderState)
        observe(navViewModel.state, ::manageNavigation)
    }

    private fun renderState(state: CinemaState?) {
        onScreenState = null
        when (state) {
            is CinemaState.OnScreen -> {
                onScreenState = state
                state.eventUrl.getContent()?.let {
                    EventDialogFragment.show(supportFragmentManager, it)
                }
            }
        }
    }

    private fun manageNavigation(state: NavState?) {
        when (state) {
            is NavState.Home -> bottomNav.selectedItemId = R.id.on_screen
            is NavState.OnScreen -> {
                supportFragmentManager.popBackStack()
                showFragment(OnScreenFragment.newInstance())
            }
            NavState.Schedule -> {
                supportFragmentManager.popBackStack()
                showFragment(ScheduleFragment.newInstance())
                tagViewModel.dispatch(TagAction.Schedule)
            }
            is NavState.Details -> showFragment(DetailsFragment.newInstance(), true)
            NavState.Info -> showFragment(InformationFragment.newInstance())
            NavState.Settings -> showFragment(SettingsFragment.newInstance())
            NavState.Back -> super.onBackPressed()
        }
    }

    override fun onBackPressed() {
        val origin = when (displayedFragment()) {
            is DetailsFragment -> NavOrigin.DETAILS
            is ScheduleFragment -> NavOrigin.SCHEDULE
            is OnScreenFragment -> NavOrigin.ON_SCREEN
            is InformationFragment -> NavOrigin.INFO
            is SettingsFragment -> NavOrigin.SETTINGS
            else -> error("Incorrect origin fragment")
        }
        navViewModel.dispatch(NavAction.Back(origin))
    }

    private fun initBottomNavigation() {
        bottomNav = find(R.id.bottomNavigation)
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.on_screen -> {
                    navViewModel.dispatch(
                        NavAction.OnScreen(
                            if (displayedFragment() is OnScreenFragment) NavOrigin.ON_SCREEN
                            else NavOrigin.BOTTOM_NAV
                        )
                    )
                    true
                }
                R.id.schedule -> {
                    navViewModel.dispatch(
                        NavAction.Schedule(
                            if (displayedFragment() is ScheduleFragment) NavOrigin.SCHEDULE
                            else NavOrigin.BOTTOM_NAV
                        )
                    )
                    true
                }
                R.id.information -> {
                    navViewModel.dispatch(NavAction.Info)
                    true
                }
                R.id.settings -> {
                    navViewModel.dispatch(NavAction.Settings)
                    true
                }
                else -> false
            }
        }
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        replaceFragment(
            R.id.mainContainer,
            fragment,
            addToBackStack,
            R.anim.fade_in,
            R.anim.fade_out
        )
    }

    private fun displayedFragment(): Fragment? =
        supportFragmentManager.findFragmentById(R.id.mainContainer)
}