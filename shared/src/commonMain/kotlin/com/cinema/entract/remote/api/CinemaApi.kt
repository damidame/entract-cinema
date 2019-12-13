package com.cinema.entract.remote.api

import com.cinema.entract.remote.model.MovieRemote
import com.cinema.entract.remote.model.ParametersRemote
import com.cinema.entract.remote.model.PromotionalRemote
import com.cinema.entract.remote.model.WeekRemote
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import kotlinx.serialization.json.Json
import kotlinx.serialization.list

class CinemaApi {

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun getMovies(day: String): List<MovieRemote> = client.get<String> {
        url {
            host = BASE_URL
            encodedPath = "${BASE_PATH}getFilmsJour.php"
            parameter("jour", day)
        }
    }.run {
        Json.parse(MovieRemote.serializer().list, this)
    }

    suspend fun getSchedule(): List<WeekRemote> = client.get<String> {
        url {
            host = BASE_URL
            encodedPath = "${BASE_PATH}getProgramme.php"
        }
    }.run {
        Json.parse(WeekRemote.serializer().list, this)
    }

    suspend fun getParameters(): ParametersRemote = client.get<String> {
        url {
            host = BASE_URL
            encodedPath = "${BASE_PATH}getParametres.php"
        }
    }.run {
        Json.parse(ParametersRemote.serializer(), this)
    }

    suspend fun getPromotional(): PromotionalRemote = client.get<String> {
        url {
            host = BASE_URL
            encodedPath = "${BASE_PATH}getLiensEvenement.php"
        }
    }.run {
        Json.parse(PromotionalRemote.serializer(), this)
    }

    suspend fun registerNotifications(type: String, token: String): Unit = client.put {
        url {
            host = BASE_URL
            encodedPath = "${BASE_PATH}registerPushNotifications"
            parameter("type", type)
            parameter("token", token)
        }
    }

    suspend fun tagSchedule(): Unit = client.put {
        url {
            host = BASE_URL
            encodedPath = "${BASE_PATH}updateStatistiques.php"
            parameter("page", "page_programme")
        }
    }

    suspend fun tagPromotional(): Unit = client.put {
        url {
            host = BASE_URL
            encodedPath = "${BASE_PATH}updateStatistiques.php"
            parameter("page", "page_evt")
        }
    }

    suspend fun tagDetails(sessionId: String): Unit = client.put {
        url {
            host = BASE_URL
            encodedPath = "${BASE_PATH}updateStatistiques.php"
            parameter("page", "page_detail")
            parameter("seance", sessionId)
        }
    }

    suspend fun tagCalendar(sessionId: String): Unit = client.put {
        url {
            host = BASE_URL
            encodedPath = "${BASE_PATH}updateStatistiques.php"
            parameter("page", "ajout_cal")
            parameter("seance", sessionId)
        }
    }

    companion object {
        private const val BASE_URL = "mobile-grenadecinema.fr"
        private const val BASE_PATH = "/php/rest/"
    }
}
