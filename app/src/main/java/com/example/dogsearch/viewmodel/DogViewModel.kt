package com.example.dogsearch.viewmodel

import com.example.dogsearch.DogRepository
import com.example.dogsearch.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DogViewModel (): BaseViewModel<DogState>() {

    private val dogRepository = DogRepository

    fun getRandomDog() {
        dogRepository.getRandomDogObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            statePublisher.onNext(DogState.RandomDogRecieved(it.message))
                        },
                        {
                            statePublisher.onNext(DogState.Error(it))
                        }
                ).addTo(disposable)
    }

    fun getBreedListAll() {
        dogRepository.getBreedListAllObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            statePublisher.onNext(DogState.BreedListAllRecieved(it.message))
                        },
                        {
                            statePublisher.onNext(DogState.Error(it))
                        }
                ).addTo(disposable)
    }

    fun getSearchDog(input: String) {
        dogRepository.getSearchDogObservable(input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            statePublisher.onNext(DogState.SearchDogRecieved(it.message))
                        },
                        {
                            statePublisher.onNext(DogState.Error(it))
                        }
                ).addTo(disposable)
    }

    fun getSubDogRansom(input: String) {
        dogRepository.getSearchDogObservable(input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            statePublisher.onNext(DogState.SearchDogRecieved(it.message))
                        },
                        {
                            statePublisher.onNext(DogState.Error(it))
                        }
                ).addTo(disposable)
    }

}