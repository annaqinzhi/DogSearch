package com.example.dogsearch;


import com.example.dogsearch.model.ResponseBreed;
import com.example.dogsearch.model.ResponseBreedList;
import com.example.dogsearch.model.ResponseRandom;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DogApi {

    @GET("breeds/image/random")
    Observable<ResponseRandom> getRandomDogObservable();

    @GET("breeds/list/all")
    Observable<ResponseBreedList> getBreedListAll();

    @GET("breed/{breed}/images")
    Observable<ResponseBreed> getSearchDogObservable(
            @Path("breed") String breed);

    @GET("breed/{totBreed}//images/random")
    Observable<ResponseRandom> getSubBreedDogRansomObservable(
            @Path("totBreed") String totBreed);

}
