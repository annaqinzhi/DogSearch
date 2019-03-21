package com.example.dogsearch.view

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity<T : ViewModel>(private val viewModelClass: Class<T>) : AppCompatActivity(){

    lateinit var viewModel: T

    var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        viewModel = ViewModelProviders.of(this).get(viewModelClass)
        disposable = CompositeDisposable()
    }

    fun onDestroyView() {
        disposable.clear()
        super.onDestroy()
    }
}

