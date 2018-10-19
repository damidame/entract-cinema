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

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.cinema.entract.app.R
import com.cinema.entract.app.ui.details.DetailsFragment
import com.cinema.entract.app.ui.event.EventDialogFragment
import com.cinema.entract.app.ui.information.InformationFragment
import com.cinema.entract.app.ui.onscreen.OnScreenFragment
import com.cinema.entract.app.ui.schedule.ScheduleFragment
import com.cinema.entract.app.ui.settings.SettingsFragment
import com.cinema.entract.core.ext.addFragment
import com.cinema.entract.core.ext.observe
import com.cinema.entract.core.ext.replaceFragment
import com.cinema.entract.core.ui.BaseActivity
import com.cinema.entract.core.views.bindView
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.inject

class CinemaActivity : BaseActivity() {

    private val cinemaViewModel by inject<CinemaViewModel>()
    private val bottomNav by bindView<BottomNavigationView>(R.id.bottomNavigation)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cinema)
        initBottomNavigation()

        observe(cinemaViewModel.getEventUrl(), ::handleEvent)
        savedInstanceState ?: addFragment(R.id.mainContainer, OnScreenFragment.newInstance())
    }

    fun selectMovies() {
        bottomNav.selectedItemId = R.id.movies
    }

    private fun initBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.movies -> handleMovies()
                R.id.schedule -> handleSchedule()
                R.id.information -> handleInformation()
                R.id.settings -> handleSettings()
                else -> false
            }
        }
    }

    private fun handleMovies(): Boolean =
        when (supportFragmentManager.findFragmentById(R.id.mainContainer)) {
            is DetailsFragment -> {
                supportFragmentManager.popBackStack()
                true
            }
            is OnScreenFragment -> false
            else -> {
                replaceFragment(R.id.mainContainer, OnScreenFragment.newInstance())
                true
            }
        }

    private fun handleSchedule(): Boolean =
        when (supportFragmentManager.findFragmentById(R.id.mainContainer)) {
            is ScheduleFragment -> false
            else -> {
                replaceFragment(R.id.mainContainer, ScheduleFragment.newInstance())
                true
            }
        }

    private fun handleInformation(): Boolean {
        replaceFragment(R.id.mainContainer, InformationFragment.newInstance())
        return true
    }

    private fun handleSettings(): Boolean {
        replaceFragment(R.id.mainContainer, SettingsFragment.newInstance())
        return true
    }

    private fun handleEvent(url: String?) {
        if (!url.isNullOrEmpty()) {
            val requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
            Glide.with(this)
                .asBitmap()
                .load(url)
                .apply(requestOptions)
                .addListener(object : RequestListener<Bitmap?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        isFirstResource: Boolean
                    ): Boolean = true

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        EventDialogFragment.show(supportFragmentManager, url)
                        return true
                    }
                })
                .submit()
        }
    }
}

fun ImageView.load(url: String) = Glide.with(context).load(url).into(this)