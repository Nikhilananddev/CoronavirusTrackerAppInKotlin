package com.android.coronavirustrackerforindia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
      lateinit var listAdapter: ListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchResults()



    }
    private fun fetchResults() {

        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) { Client.api.clone().execute() }
            if (response.isSuccessful) {

                val data = Gson().fromJson(response.body?.string(), Response::class.java)
                launch(Dispatchers.Main) {
                    bindCombinedData(data.statewise[0])
                    bindStateWiseData(data.statewise.subList(0, data.statewise.size))

                }
            }
        }
    }


    private fun bindStateWiseData(subList: List<StatewiseItem>) {

        listAdapter = ListAdapter(subList)
        list.addHeaderView(LayoutInflater.from(this).inflate(R.layout.list_header, list, false))

        list.adapter = listAdapter
    }
    private fun bindCombinedData(data: StatewiseItem) {


        confirmedTv.text = data.confirmed
        activeTv.text = data.active
        RecoveredTv.text = data.recovered
        DecereasedTv.text = data.deaths

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         super.onCreateOptionsMenu(menu)
    menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        return true
    }
    fun about(item: MenuItem?) {
        val intent = Intent(this@MainActivity, about::class.java)
        startActivity(intent)
        //Toast.makeText(this, "You select nonveg food", Toast.LENGTH_SHORT).show();
    }
}