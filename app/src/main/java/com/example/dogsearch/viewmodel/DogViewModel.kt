package com.example.dogsearch.viewmodel

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.dogsearch.DatabaseHandler
import com.example.dogsearch.DogRepository
import com.example.dogsearch.addTo
import com.example.dogsearch.model.Dog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.graphics.BitmapFactory
import android.os.Build
import com.example.dogsearch.DogApi
import com.example.dogsearch.model.ResponseRandom
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.reflect.Array.get
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
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    fun bitmapToByte(b: Bitmap): ByteArray {
        val bos = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        return bos.toByteArray()

    }

}