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

package com.cinema.entract.data.di

import com.cinema.entract.data.interactor.CinemaUseCase
import com.cinema.entract.data.interactor.NotifUseCase
import com.cinema.entract.data.interactor.TagUseCase
import com.cinema.entract.data.source.CinemaDataStore
import com.cinema.entract.data.source.DataStore
import org.koin.dsl.module

val dataModule = module {

    single { CinemaUseCase(get(), get()) }
    single { TagUseCase(get()) }
    single { NotifUseCase(get()) }
    single<DataStore> { CinemaDataStore(get(), get(), get()) }
}