package com.example.mypharmacy.model.remote

import com.example.mypharmacy.model.domain.Role
import com.example.mypharmacy.model.domain.TypeAlert
import com.example.mypharmacy.model.domain.TypeMouvement
import com.example.mypharmacy.model.remote.api.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.io.IOException

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:5050/" // Android Emulator address for localhost
    // Create LocalDateTime adapter for Gson
    class LocalDateTimeAdapter : TypeAdapter<LocalDateTime>() {
        private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: LocalDateTime?) {
            if (value == null) {
                out.nullValue()
            } else {
                out.value(formatter.format(value))
            }
        }

        @Throws(IOException::class)
        override fun read(reader: JsonReader): LocalDateTime? {
            return if (reader.peek() == com.google.gson.stream.JsonToken.NULL) {
                reader.nextNull()
                null
            } else {
                LocalDateTime.parse(reader.nextString(), formatter)
            }
        }
    }

    // Create LocalDate adapter for Gson
    class LocalDateAdapter : TypeAdapter<LocalDate>() {
        private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: LocalDate?) {
            if (value == null) {
                out.nullValue()
            } else {
                out.value(formatter.format(value))
            }
        }

        @Throws(IOException::class)
        override fun read(reader: JsonReader): LocalDate? {
            return if (reader.peek() == com.google.gson.stream.JsonToken.NULL) {
                reader.nextNull()
                null
            } else {
                LocalDate.parse(reader.nextString(), formatter)
            }
        }
    }

    // Create enum adapters for Gson
    class RoleAdapter : TypeAdapter<Role>() {
        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: Role?) {
            if (value == null) {
                out.nullValue()
            } else {
                out.value(value.name)
            }
        }

        @Throws(IOException::class)
        override fun read(reader: JsonReader): Role? {
            return if (reader.peek() == com.google.gson.stream.JsonToken.NULL) {
                reader.nextNull()
                null
            } else {
                try {
                    Role.valueOf(reader.nextString())
                } catch (e: Exception) {
                    Role.MEDICIN  // Default value
                }
            }
        }
    }

    class TypeAlertAdapter : TypeAdapter<TypeAlert>() {
        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: TypeAlert?) {
            if (value == null) {
                out.nullValue()
            } else {
                out.value(value.name)
            }
        }

        @Throws(IOException::class)
        override fun read(reader: JsonReader): TypeAlert? {
            return if (reader.peek() == com.google.gson.stream.JsonToken.NULL) {
                reader.nextNull()
                null
            } else {
                try {
                    TypeAlert.valueOf(reader.nextString())
                } catch (e: Exception) {
                    TypeAlert.STOCK  // Default value
                }
            }
        }
    }

    class TypeMouvementAdapter : TypeAdapter<TypeMouvement>() {
        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: TypeMouvement?) {
            if (value == null) {
                out.nullValue()
            } else {
                out.value(value.name)
            }
        }

        @Throws(IOException::class)
        override fun read(reader: JsonReader): TypeMouvement? {
            return if (reader.peek() == com.google.gson.stream.JsonToken.NULL) {
                reader.nextNull()
                null
            } else {
                try {
                    TypeMouvement.valueOf(reader.nextString())
                } catch (e: Exception) {
                    TypeMouvement.ENTREE  // Default value
                }
            }
        }
    }

    // Create Gson with custom adapters
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .registerTypeAdapter(Role::class.java, RoleAdapter())
        .registerTypeAdapter(TypeAlert::class.java, TypeAlertAdapter())
        .registerTypeAdapter(TypeMouvement::class.java, TypeMouvementAdapter())
        .create()

    // Create OkHttpClient with logging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Create Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    // API services
    val adminApi: AdminApi = retrofit.create(AdminApi::class.java)
    val alerteApi: AlerteApi = retrofit.create(AlerteApi::class.java)
    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val logApi: LogApi = retrofit.create(LogApi::class.java)
    val medicinApi: MedicinApi = retrofit.create(MedicinApi::class.java)
    val mouvementStockApi: MouvementStockApi = retrofit.create(MouvementStockApi::class.java)
    val userApi: UserApi = retrofit.create(UserApi::class.java)
}