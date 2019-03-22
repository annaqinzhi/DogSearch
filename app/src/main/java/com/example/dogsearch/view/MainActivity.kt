package com.example.dogsearch.view


import android.annotation.TargetApi
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
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
import java.io.ByteArrayOutputStream
import android.widget.AdapterView


class MainActivity : BaseActivity<DogViewModel>(DogViewModel::class.java), View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private var gridView: GridView? = null
    private var spinnerBreed: Spinner? = null
    private var spinnerSubBreed: Spinner? = null
    private var dogAdapterGridView: DogAdapterGridView? = null
    private var look: TextView? = null
    private var breedList = mutableListOf<Breed>()
    private var breedHashmap = HashMap<String, List<String>>()
    private var db: DatabaseHandler? = null
    private var breednSubBreed = ""
    private var breedSeleted= ""
    private var subBreedSeleted = ""
    final val defaultString = "show all"

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

        getStateListener()
        viewModel.getRandomDog()

        getStateListener()
        viewModel.getBreedListAll()

    }

    fun getStateListener() {
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
                                is DogState.SubDogRansomRecieved -> {
                                    showDogImage(state.image)
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
            if (seletedItem1 != defaultString) {
                setUpSubBreedList(getSubBreedList(seletedItem1))
                breedSeleted = ""
                subBreedSeleted = ""
                breedSeleted = seletedItem1

            }
        } else if (parent.id == com.example.dogsearch.R.id.spinnerSubBreed) {
            var seletedItem2 = spinnerSubBreed!!.getItemAtPosition(pos).toString()
            if (seletedItem2 != defaultString) {
                subBreedSeleted = ""
                subBreedSeleted = seletedItem2
            }
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {
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
        getBreedHashmap(breedList)
        return breedHashmap.get(breed)!!
    }


    fun getBreedHashmap(list: List<Breed>): HashMap<String, List<String>> {
        val map = HashMap<String, List<String>>()
        for (i in list) map.put(i.breed, i.subBreed)
        breedHashmap = map
        return breedHashmap
    }

    override fun onClick(v: View) {
        when (v.id) {
            com.example.dogsearch.R.id.Look -> {
                if (breedSeleted != defaultString && subBreedSeleted.isBlank()) {
                    getStateListener()
                    viewModel.getSearchDog(breedSeleted)
                } else if (breedSeleted != defaultString && subBreedSeleted.isNotBlank()){
                    breednSubBreed = breedSeleted + "-"+ subBreedSeleted
                    getStateListener()
                    viewModel.getSubDogRansom(breednSubBreed)
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


    //COnvert and resize our image to 320dp for faster uploading our images to DB
    protected fun decodeUri(selectedImage: Uri, REQUIRED_SIZE: Int): Bitmap? {

        try {

            // Decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeStream(contentResolver.openInputStream(selectedImage), null, o)

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            var width_tmp = o.outWidth
            var height_tmp = o.outHeight
            var scale = 1
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                    break
                }
                width_tmp /= 2
                height_tmp /= 2
                scale *= 2
            }

            // Decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            return BitmapFactory.decodeStream(contentResolver.openInputStream(selectedImage), null, o2)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    //Convert bitmap to bytes
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private fun profileImage(b: Bitmap): ByteArray {

        val bos = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.PNG, 0, bos)
        return bos.toByteArray()

    }


//    // function to get values from the Edittext and image
//    private fun getValues() {
//        f_name = fname.getText().toString()
//        photo = profileImage(bp)
//    }
//
//    //Insert data to the database
//    private fun addContact() {
//        getValues()
//
//        db.addContacts(Contact(f_name, photo))
//        Toast.makeText(applicationContext, "Saved successfully", Toast.LENGTH_LONG).show()
//    }
//
//    //Retrieve data from the database and set to the list view
//    private fun ShowRecords() {
//        val contacts = ArrayList(db.getAllContacts())
//        data = DataAdapter(this, contacts)
//
//        lv.setAdapter(data)
//
//        lv.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
//            dataModel = contacts.get(position)
//
//            Toast.makeText(applicationContext, String.valueOf(dataModel.getID()), Toast.LENGTH_SHORT).show()
//        })
//    }

}
