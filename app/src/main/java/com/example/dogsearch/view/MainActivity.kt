package com.example.dogsearch.view


import android.os.Bundle

import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import com.example.dogsearch.BuildConfig
import com.example.dogsearch.addTo
import com.example.dogsearch.model.Breed
import com.example.dogsearch.viewmodel.DogState
import com.example.dogsearch.viewmodel.DogViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.google.gson.GsonBuilder
import org.json.JSONObject
import org.json.JSONArray


class MainActivity : BaseActivity<DogViewModel>(DogViewModel::class.java), View.OnClickListener,
                     AdapterView.OnItemSelectedListener {

    private var gridView: GridView? = null
    private var spinnerBreed:Spinner? = null
    private var spinnerSubBreed:Spinner? = null
    private var dogAdapterGridView: DogAdapterGridView? = null
    private var look: TextView? = null
    private var breedList = mutableListOf<Breed>()
    private var breedHashmap = HashMap<String, List<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.dogsearch.R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(DogViewModel::class.java)
        gridView = findViewById<View>(com.example.dogsearch.R.id.gridview) as GridView
        spinnerBreed = findViewById(com.example.dogsearch.R.id.spinnerBreed) as Spinner
        spinnerSubBreed = findViewById(com.example.dogsearch.R.id.spinnerSubBreed) as Spinner
        look = findViewById<TextView>(com.example.dogsearch.R.id.Look) as TextView

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
                                    var obj = state.breedObj
                                    setUpBreedList(getBreedObjList(obj))
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
        var seletedItem1 = spinnerBreed!!.getItemAtPosition(position).toString()
        if(seletedItem1 != "show all"){
           setUpSubBreedList(getSubBreedList(seletedItem1))
        }
}

    override fun onNothingSelected(arg0: AdapterView<*>) {
    }

    fun setUpBreedList(breedObjList : List<Breed>){
        var breedStrList = mutableListOf<String>()
        breedStrList.add("show all")

        for(i in breedObjList){
            breedStrList.add(i.breed)
        }

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, breedStrList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBreed!!.setAdapter(aa)
    }

    fun getBreedObjList(obj : Any) : List<Breed> {
        var list = mutableListOf<Breed>()
        val gson1 = GsonBuilder().setPrettyPrinting().create()
        val jsonStr: String = gson1.toJson(obj)
        val jsonObj = JSONObject(jsonStr)
        var keys = jsonObj.keys()

        while (keys.hasNext()) {
            var key = keys.next()
            var value : Any = jsonObj.get((key))
            val subBreeds = value.toString()

            var subBreedList = mutableListOf<String>()

            if(subBreeds.isNotBlank()) {
                val jsonarray = JSONArray(subBreeds)
                for (i in 0 until jsonarray.length()) {
                    val subBreedStr = jsonarray.getString(i)
                    subBreedList.add(subBreedStr)
                }
            }

            var objBreed = object : Breed(key, subBreedList){}
            list.add(objBreed)
        }

        breedList = list
        return breedList
    }


    fun setUpSubBreedList(subBreedList: List<String>){
        val bb = ArrayAdapter(this, android.R.layout.simple_spinner_item, subBreedList)
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSubBreed!!.setAdapter(bb)
    }

    fun getSubBreedList(breed : String ): List<String>{
        getBreedHashmap(breedList)
        return breedHashmap.get(breed)!!
    }


    fun getBreedHashmap (list : List<Breed>) : HashMap<String, List<String>>{
        val map = HashMap<String, List<String>>()
        for (i in list) map.put(i.breed, i.subBreed)
        breedHashmap = map
        return breedHashmap
    }

    override fun onClick(v: View) {
        when (v.id) {
            com.example.dogsearch.R.id.Look -> {
                var text1 = spinnerBreed!!.getSelectedItem().toString()
                if(text1 != "show all"){
                    getStateListener()
                    viewModel.getSearchDog(text1)
                } else {
                    getStateListener()
                    viewModel.getRandomDog()
                }
            }
            else -> {
                // else condition
            }
        }
    }

    fun showDogImage(image: String) {
        var list = listOf<String>(image)
        dogAdapterGridView = DogAdapterGridView(this, com.example.dogsearch.R.layout.item_view, list)
        gridView?.setAdapter(dogAdapterGridView)
    }

    fun showDogImageList(images: List<String>) {
        dogAdapterGridView = DogAdapterGridView(this, com.example.dogsearch.R.layout.item_view, images)
        gridView?.setAdapter(dogAdapterGridView)
    }

}
