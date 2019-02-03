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

import com.cinema.entract.core.ui.ScopedViewModel

class NavigationViewModel : ScopedViewModel<NavAction, NavState>() {

    override suspend fun bindActions(action: NavAction) {
        val newState = when (action) {
            is NavAction.OnScreen -> {
                when (action.origin) {
                    NavOrigin.ON_SCREEN -> NavState.OnScreen
                    NavOrigin.SCHEDULE,
                    NavOrigin.DETAILS,
                    NavOrigin.INFO,
                    NavOrigin.SETTINGS -> NavState.Home
                }
            }
            is NavAction.Schedule -> NavState.Schedule
            is NavAction.Back -> {
                when (action.origin) {
                    NavOrigin.ON_SCREEN,
                    NavOrigin.DETAILS -> NavState.Back
                    NavOrigin.SCHEDULE -> NavState.Home
                    NavOrigin.INFO,
                    NavOrigin.SETTINGS -> NavState.Home
                }
            }
            is NavAction.Details -> NavState.Details
            is NavAction.Info -> NavState.Info
            is NavAction.Settings -> NavState.Settings
        }
        state.postValue(newState)
    }
}

sealed class NavAction {
    data class OnScreen(val origin: NavOrigin) : NavAction()
    data class Schedule(val origin: NavOrigin) : NavAction()
    data class Back(val origin: NavOrigin) : NavAction()
    object Details : NavAction()
    object Info : NavAction()
    object Settings : NavAction()
}

enum class NavState {
    Home,
    OnScreen,
    Details,
    Schedule,
    Info,
    Settings,
    Back
}

enum class NavOrigin {
    ON_SCREEN,
    SCHEDULE,
    DETAILS,
    INFO,
    SETTINGS
}