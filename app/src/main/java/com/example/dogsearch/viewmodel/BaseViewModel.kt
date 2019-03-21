package com.example.dogsearch.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
//import timber.log.Timber

abstract class BaseViewModel<T> : ViewModel() {

    var disposable = CompositeDisposable()

    protected val statePublisher = PublishSubject.create<T>()
    fun getState() = statePublisher

    fun publish(state: T) {
        statePublisher.onNext(state)
    }

    override fun onCleared() {
       // Timber.d("Disposing for ${this::class.java.simpleName}")
        disposable.clear()
        super.onCleared()
    }
}
