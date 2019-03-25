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

package com.cinema.entract.core.widget

import android.view.View
import androidx.annotation.LayoutRes

abstract class ItemAdapter(@LayoutRes open val layoutId: Int) {

    var holder: BaseViewHolder? = null
        private set

    fun onCreateViewHolder(itemView: View) = BaseViewHolder(itemView)

    @Suppress("UNCHECKED_CAST")
    fun onBindBaseViewHolder(holder: BaseViewHolder) {
        this.holder = holder
        holder.onBindViewHolder()
    }

    abstract fun BaseViewHolder.onBindViewHolder()
}