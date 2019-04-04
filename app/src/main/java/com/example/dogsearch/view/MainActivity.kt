package com.example.dogsearch.view


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle

import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import com.example.dogsearch.BuildConfig
import com.example.dogsearch.DatabaseHandler
import com.example.dogsearch.addTo
import com.example.dogsearch.model.Breed
import com.example.dogsearch.viewmodel.DogState
import com.example.dogsearch.viewmodel.DogViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.google.gson.GsonBuilder
import org.json.JSONObject
import org.json.JSONArray
import android.widget.AdapterView
import com.example.dogsearch.model.Dog
import java.util.*


class MainActivity : BaseActivity<DogViewModel>(DogViewModel::class.java), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private var gridView: GridView? = null
    private var spinnerBreed: Spinner? = null
    private var spinnerSubBreed: Spinner? = null
    private var dogAdapterGridView: DogAdapterGridView_Dog? = null
    private var look: TextView? = null
    private var breedList = mutableListOf<Breed>()
    private var breedHashmap = HashMap<String, List<String>>()
    private var db: DatabaseHandler? = null
    final val defaultString = "show all"
    private var breednSubBreed = ""
    private var breedSeleted= defaultString
    private var subBreedSeleted = defaultString



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.dogsearch.R.layout.activity_main)
        db = DatabaseHandler(this)
        viewModel = ViewModelProviders.of(this).get(DogViewModel::class.java)
        gridView = findViewById<View>(com.example.dogsearch.R.id.gridview) as GridView
        spinnerBreed = findViewById(com.example.dogsearch.R.id.spinnerBreed) as Spinner
        spinnerSubBreed = findViewById(com.example.dogsearch.R.id.spinnerSubBreed) as Spinner
        look = findViewById<TextView>(com.example.dogsearch.R.id.Look) as TextView

        spinnerBreed!!.setOnItemSelectedListener(this)
        spinnerSubBreed!!.setOnItemSelectedListener(this)
        look!!.setOnClickListener(this)

        if(isNetworkAvailable()) {
            getStateListener()
            viewModel.downLoadImage()
            viewModel.getBreedListAll()
            viewModel.getRandomDog()
        } else {
            var size = db!!.allDogs.size - 1
            showDogImage(db!!.allDogs.get((0..size).random()))
        }
    }

    fun getStateListener() {
        viewModel.getState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { state ->
                            when (state) {
                                is DogState.RandomDogRecieved -> {
                                    var dog = Dog(breedSeleted,subBreedSeleted,null, state.image)
                                    showDogImage(dog)
                                }
                                is DogState.SearchDogRecieved -> {
                                    var dogs = mutableListOf<Dog>()
                                    for (i in state.images){
                                        var dog = Dog(breedSeleted,subBreedSeleted,null,i)
                                        dogs.add(dog)
                                    }
                                    showDogImageList(dogs)
                                }
                                is DogState.BreedListAllRecieved -> {
                                    var obj = state.breedObj
                                    setUpBreedList(getBreedObjList(obj))
                                }
                                is DogState.SubDogRansomRecieved -> {
                                    var dog = Dog(breedSeleted,subBreedSeleted,null, state.image)
                                    showDogImage(dog)
                                }
                                is DogState.DownLoadImage -> {
                                    var dog = Dog(breedSeleted,subBreedSeleted,state.image,null)
                                    db!!.addDogs(dog)
                                }
                            }
                        },
                        {
                            if (BuildConfig.DEBUG) Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                        }
                )
                .addTo(disposable)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {

        if (parent.id == com.example.dogsearch.R.id.spinnerBreed) {
            var seletedItem1 = spinnerBreed!!.getItemAtPosition(pos).toString()
            if(!seletedItem1.equals(defaultString)) {
                breedSeleted = ""
                subBreedSeleted = ""
                breedSeleted = seletedItem1
                setUpSubBreedList(getSubBreedList(breedSeleted))
            }

        } else if (parent.id == com.example.dogsearch.R.id.spinnerSubBreed) {
            var seletedItem2 = spinnerSubBreed!!.getItemAtPosition(pos).toString()
                subBreedSeleted = ""
                subBreedSeleted = seletedItem2
        }
    }
    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

    override fun onClick(v: View) {
        when (v.id) {
            com.example.dogsearch.R.id.Look -> {
              if(isNetworkAvailable()) {
                  if (breedSeleted.equals(defaultString) && subBreedSeleted.equals(defaultString)) {
                      getStateListener()
                      viewModel.getRandomDog()

                  } else if (!breedSeleted.equals(defaultString) && subBreedSeleted.isBlank()) {
                      getStateListener()
                      viewModel.getSearchDog(breedSeleted)

                  } else if (!breedSeleted.equals(defaultString) && subBreedSeleted.isNotBlank()) {
                      breednSubBreed = breedSeleted + "-" + subBreedSeleted
                      getStateListener()
                      viewModel.getSubDogRansom(breednSubBreed)
                  }
              } else {
                  var size = db!!.allDogs.size - 1
                  showDogImage(db!!.allDogs.get((0..size).random()))
              }
            }
            else -> {
                // else condition
            }
        }
    }

    fun setUpBreedList(breedObjList: List<Breed>) {
        var breedStrList = mutableListOf<String>()
        breedStrList.add(defaultString)

        for (i in breedObjList) {
            breedStrList.add(i.breed)
        }

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, breedStrList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBreed!!.setAdapter(aa)
    }

    fun getBreedObjList(obj: Any): List<Breed> {
        var list = mutableListOf<Breed>()
        val gson1 = GsonBuilder().setPrettyPrinting().create()
        val jsonStr: String = gson1.toJson(obj)
        val jsonObj = JSONObject(jsonStr)
        var keys = jsonObj.keys()

        while (keys.hasNext()) {
            var key = keys.next()
            var value: Any = jsonObj.get((key))
            val subBreeds = value.toString()

            var subBreedList = mutableListOf<String>()

            if (subBreeds.isNotBlank()) {
                val jsonarray = JSONArray(subBreeds)
                for (i in 0 until jsonarray.length()) {
                    val subBreedStr = jsonarray.getString(i)
                    subBreedList.add(subBreedStr)
                }
            }

            var objBreed = object : Breed(key, subBreedList) {}
            list.add(objBreed)
        }

        breedList = list
        return breedList
    }


    fun setUpSubBreedList(subBreedList: List<String>) {
        val bb = ArrayAdapter(this, android.R.layout.simple_spinner_item, subBreedList)
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSubBreed!!.setAdapter(bb)
    }

    fun getSubBreedList(breed: String): List<String> {
        if(breed.equals(defaultString)){
            return listOf(defaultString)
        } else {
            getBreedHashmap(breedList)
            return breedHashmap.get(breed)!!
        }
    }


    fun getBreedHashmap(list: List<Breed>): HashMap<String, List<String>> {
        val map = HashMap<String, List<String>>()
        for (i in list) map.put(i.breed, i.subBreed)
        breedHashmap = map
        return breedHashmap
    }

    fun showDogImage(dog:Dog) {
        var list = listOf<Dog>(dog)
        dogAdapterGridView = DogAdapterGridView_Dog(this, com.example.dogsearch.R.layout.item_view, list)
        gridView?.setAdapter(dogAdapterGridView)
    }

    fun showDogImageList(dogs: List<Dog>) {
        dogAdapterGridView = DogAdapterGridView_Dog(this, com.example.dogsearch.R.layout.item_view, dogs)
        gridView?.setAdapter(dogAdapterGridView)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

}
