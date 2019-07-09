/*
 * Copyright 2019 Stéphane Baiget
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
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.cinema.entract.app.R
import com.cinema.entract.app.ui.onscreen.OnScreenFragment
import com.cinema.entract.app.ui.schedule.ScheduleFragment
import com.cinema.entract.core.ui.BaseActivity
import com.cinema.entract.core.ui.scrollToTop
import com.cinema.entract.data.interactor.CinemaUseCase
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.jetbrains.anko.find
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate


class CinemaActivity : BaseActivity() {

    private val useCase by inject<CinemaUseCase>()
    private val cinemaViewModel by viewModel<CinemaViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(useCase.getThemeMode())

        setContentView(R.layout.activity_cinema)
        val bnv = find<BottomNavigationView>(R.id.bottomNavigation)
        bnv.setupWithNavController(findNavController(R.id.navHost))
        bnv.setOnNavigationItemReselectedListener {
            when (val fragment = getNavHostFragment()?.getDisplayedFragment()) {
                is OnScreenFragment -> if (!fragment.isTodayDisplayed()) cinemaViewModel.process(
                    CinemaAction.LoadMovies(LocalDate.now())
                )
                is ScheduleFragment -> fragment.scrollToTop()
            }
        }
    }

    private fun getNavHostFragment(): NavHostFragment? =
        supportFragmentManager.findFragmentById(R.id.navHost) as? NavHostFragment

    private fun NavHostFragment.getDisplayedFragment(): Fragment? = childFragmentManager.primaryNavigationFragment
}
