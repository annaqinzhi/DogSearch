package com.example.dogsearch

import com.example.dogsearch.model.ResponseBreed
import com.example.dogsearch.model.ResponseBreedList
import com.example.dogsearch.model.ResponseRandom
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object DogRepository {

    private val base_URL = "https://dog.ceo/api/"

    val getRandomDogObservable: Observable<ResponseRandom>
        get() {

            val retrofit = Retrofit.Builder()
                    .baseUrl(base_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

            val dogApi = retrofit.create(DogApi::class.java)

            return dogApi.randomDogObservable

        }

    val getBreedListAllObservable: Observable<ResponseBreedList>
        get() {

            val retrofit = Retrofit.Builder()
                    .baseUrl(base_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

            val dogApi = retrofit.create(DogApi::class.java)

            return dogApi.breedListAll

        }

    fun getSearchDogObservable(breed: String): Observable<ResponseBreed> {

        val retrofit = Retrofit.Builder()
                .baseUrl(base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        val dogApi = retrofit.create(DogApi::class.java)

        return dogApi.getSearchDogObservable(breed)

    }

}
