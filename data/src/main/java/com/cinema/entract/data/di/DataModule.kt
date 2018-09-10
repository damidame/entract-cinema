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

package com.cinema.entract.data.di

import com.cinema.entract.data.model.DataMapper
import com.cinema.entract.data.model.MovieData
import com.cinema.entract.data.model.MovieDataMapper
import com.cinema.entract.data.source.CinemaDataStoreFactory
import com.cinema.entract.domain.model.MovieDomain
import org.koin.dsl.module.module

val dataModule = module {

    single { MovieDataMapper() as DataMapper<MovieData, MovieDomain> }
    factory { CinemaDataStoreFactory(get(), get()) }
}