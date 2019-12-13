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

package com.cinema.entract.remote.model

import com.cinema.entract.data.model.MovieData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieRemote(
    val id_seance: String = "",
    val id_film: String = "",
    val titre: String = "",
    val date: String = "",
    val horaire: String = "",
    @SerialName("3d") val troisDimension: Boolean = false,
    val vo: Boolean = false,
    val art_essai: Boolean = false,
    val moins_douze: Boolean = false,
    val avertissement: Boolean = false,
    val affiche: String = "",
    val duree: String = "",
    val annee: String = "",
    val pays: String = "",
    val style: String = "",
    val de: String = "",
    val avec: String = "",
    val synopsis: String = "",
    val bande_annonce: String = "",
    val autres_dates: List<MovieRemote> = emptyList()
) {

    fun mapToData(): MovieData = MovieData(
        id_seance,
        id_film,
        titre,
        date,
        horaire,
        troisDimension,
        vo,
        art_essai,
        moins_douze,
        avertissement,
        affiche,
        duree,
        annee,
        style,
        de,
        avec,
        synopsis,
        bande_annonce,
        autres_dates.map { it.mapToData() }
    )
}
