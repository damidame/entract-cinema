/*
 * Copyright 2018 Stéphane Baiget
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.cinema.entract.ui.base

import androidx.fragment.app.Fragment
import com.cinema.entract.remote.network.NoConnectivityException
import com.cinema.entract.ui.R
import java.net.SocketTimeoutException

open class BaseFragment : Fragment() {

    open fun getErrorMessage(throwable: Throwable?): String {
        return when (throwable) {
            is NoConnectivityException -> getString(R.string.error_no_connectivity)
            is SocketTimeoutException -> getString(R.string.error_no_connectivity)
            else -> getString(R.string.error_general)
        }
    }
}