package com.example.dogsearch.viewmodel

import com.example.dogsearch.model.Breed

sealed class DogState {
    class Error(val error: Throwable) : DogState()
    class RandomDogRecieved(val image: String) : DogState()
    class SearchDogRecieved(val images: List<String>) : DogState()
    class BreedListAllRecieved(val breedList: List<Breed>) : DogState()
}