package com.example.dogsearch.view


import android.os.Bundle

import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView

import com.example.dogsearch.BuildConfig

import com.example.dogsearch.R
import com.example.dogsearch.addTo
import com.example.dogsearch.viewmodel.DogState
import com.example.dogsearch.viewmodel.DogViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import androidx.recyclerview.widget.LinearLayoutManager

import com.bumptech.glide.Glide
import com.example.dogsearch.model.Breed
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<DogViewModel>(DogViewModel::class.java), View.OnClickListener,
                     AdapterView.OnItemSelectedListener {

    private var gridView: GridView? = null
    private var spinnerBreed:Spinner? = null
    private var spinnerSubBreed:Spinner? = null
    private var dogAdapterGridView: DogAdapterGridView? = null
    private var look: TextView? = null
    private var breeds = mutableListOf<String>()
    private var subBreedList = mutableListOf<String>()
    private var breed : String ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(DogViewModel::class.java)
        gridView = findViewById<View>(R.id.gridview) as GridView
        spinnerBreed = findViewById(R.id.spinnerBreed) as Spinner
        spinnerSubBreed = findViewById(R.id.spinnerSubBreed) as Spinner
        look = findViewById<TextView>(R.id.Look) as TextView

        spinnerBreed!!.setOnItemSelectedListener(this)
        spinnerSubBreed!!.setOnItemSelectedListener(this)
        look!!.setOnClickListener(this)

        getStateListener()
        viewModel.getRandomDog()

        getStateListener()
        viewModel.getBreedListAll()

    }

    fun getStateListener(){
        viewModel.getState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { state ->
                            when (state) {
                                is DogState.RandomDogRecieved -> {
                                    showDogImage(state.image)
                                }
                                is DogState.SearchDogRecieved -> {
                                    showDogImageList(state.images)
                                }
                                is DogState.BreedListAllRecieved -> {
                                    var list = mutableListOf<Breed>()
                                    list.addAll(state.breedList)
                                    breeds.add("show all")
                                    for (it in list){
                                        breeds.add(it.breed)
                                    }
                                    setUpBreedList(breeds)
                                }
                            }
                        },
                        {
                            if (BuildConfig.DEBUG) Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                        }
                )
                .addTo(disposable)
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        //breed = breedList[position]
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

    fun setUpBreedList(breedList : List<String>){
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, breedList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBreed!!.setAdapter(aa)
    }

    fun setUpSubBreedList(subBreedList : List<String>){
        val bb = ArrayAdapter(this, android.R.layout.simple_spinner_item, subBreedList)
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSubBreed!!.setAdapter(bb)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.Look -> {
                getStateListener()
                viewModel.getRandomDog()
            }
            else -> {
                // else condition
            }
        }
    }

    fun showDogImage(image: String) {
        var list = listOf<String>(image)
        dogAdapterGridView = DogAdapterGridView(this, R.layout.item_view, list)
        gridView?.setAdapter(dogAdapterGridView)
    }

    fun showDogImageList(images: List<String>) {
        dogAdapterGridView = DogAdapterGridView(this, R.layout.item_view, images)
        gridView?.setAdapter(dogAdapterGridView)
    }

    fun getDogByBreed(breed : String ){

        viewModel.getState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { state ->
                            when (state) {
                                is DogState.RandomDogRecieved -> {
                                    showDogImage(state.image)
                                }
                                is DogState.SearchDogRecieved -> {
                                   showDogImageList(state.images)
                                }
                            }
                        },
                        {
                            if (BuildConfig.DEBUG) Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                        }
                )
                .addTo(disposable)

        viewModel.getSearchDog(breed)

    }
}
