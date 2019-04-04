package com.example.dogsearch.viewmodel

import android.graphics.Bitmap
import com.example.dogsearch.DogRepository
import com.example.dogsearch.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL


class DogViewModel (): BaseViewModel<DogState>() {

    private val dogRepository = DogRepository
    var imageByteList = mutableListOf<ByteArray>()

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
        dogRepository.getSubDogRandomObservable(input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            statePublisher.onNext(DogState.SubDogRansomRecieved(it.message))
                        },
                        {
                            statePublisher.onNext(DogState.Error(it))
                        }
                ).addTo(disposable)
    }

    fun downLoadImage() {
        dogRepository.getRandomDogObservable
                .map{
                     it -> bitmapToByte(getBitmapFromUrl(it.message)!!)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            statePublisher.onNext(DogState.DownLoadImage(it))
                        },
                        {
                            statePublisher.onNext(DogState.Error(it))
                        }
                ).addTo(disposable)
    }

   private fun getBitmapFromUrl (imageUrl : String) : Bitmap? {
        var bpImage : Bitmap ? = null
        try {
            val url = URL(imageUrl)
            bpImage = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: IOException) {
            System.out.println(e)
        }
        return bpImage
    }

    //Convert bitmap to bytes
    fun bitmapToByte(b: Bitmap): ByteArray {
        val bos = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        return bos.toByteArray()

    }
}