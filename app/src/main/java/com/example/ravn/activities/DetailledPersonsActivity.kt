package com.example.ravn.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.ravn.R
import com.example.ravn.adapters.VehiclesAdapter
import com.example.ravn.network.apolloClient
import com.example.ravnstarwars.DetailedPersonsStarWarsQuery
import kotlinx.android.synthetic.main.activity_detailed_person.*


import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class   DetailledPersonsActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DetailledPersonsActivity"
    }
    private lateinit var actionBar: ActionBar
    private lateinit var mVehiclesListAdapter: VehiclesAdapter
    private lateinit var myApolloClient: ApolloClient
    private var mVehiclesList = mutableListOf<DetailedPersonsStarWarsQuery.Vehicle>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_person)
        myApolloClient = apolloClient
        actionBar = supportActionBar!!
        mVehiclesListAdapter = VehiclesAdapter(this, mVehiclesList)

        rv_vehicles.adapter = mVehiclesListAdapter
        rv_vehicles.layoutManager = LinearLayoutManager(this)

        rv_vehicles.setHasFixedSize(true)

        actionBar.setDisplayHomeAsUpEnabled(true)

        val id = intent.getStringExtra("id")
        if (id != null) {
            lifecycleScope.launch {
                getInformation(id)
            }
        } else {
            showLoadError()
        }

    }

    @SuppressLint("LongLogTag")
    private suspend fun getInformation(id: String): Unit = coroutineScope {
        // Get information from a specific person
        val response = try {
            myApolloClient.query(DetailedPersonsStarWarsQuery(id = id)).await()
        } catch (e: ApolloException) {
            runOnUiThread {
                showLoadError()
            }
            return@coroutineScope
        }

        val person = response.data?.person

        if (person == null || response.hasErrors()) {
            runOnUiThread {
                showLoadError()
            }
            return@coroutineScope
        }
        Log.d(TAG, "getPersonInformation: $person")

        mVehiclesList
            .addAll(person.vehicleConnection?.vehicles?.filterNotNull() ?: emptyList())

        runOnUiThread {
            ll_detail.visibility = View.VISIBLE
            actionBar.title = person.name?: "Unknown"
            addItemDataView(getString(R.string.eye_color), person.eyeColor ?: "Unknown")
            addItemDataView(getString(R.string.hair_color), person.hairColor ?: "Unknown")
            addItemDataView(getString(R.string.skin_color), person.skinColor ?: "Unknown")
            addItemDataView(getString(R.string.birth_year), person.birthYear ?: "Unknown")
            mVehiclesListAdapter.notifyDataSetChanged()
            ll_loading.visibility = View.GONE
        }
    }


    private fun showLoadError() {
        ll_loading.visibility = View.GONE
        ll_failed.visibility = View.VISIBLE
    }


    private fun addItemDataView(left: String, right: String) {
        val view = layoutInflater.inflate(R.layout.item_info, ll_information, false)
        view.findViewById<TextView>(R.id.dataL).text = left
        view.findViewById<TextView>(R.id.dataR).text = right
        ll_information.addView(view)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}