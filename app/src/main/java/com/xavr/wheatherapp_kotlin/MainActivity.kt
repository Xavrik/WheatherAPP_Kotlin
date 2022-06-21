package com.xavr.wheatherapp_kotlin

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

import java.net.URL


class MainActivity : AppCompatActivity() {

    private var user_field: EditText? =null
    private var main_btm: Button? = null
    private var result_info: TextView? = null
    private val client = OkHttpClient()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user_field = findViewById(R.id.user_field)
        main_btm = findViewById(R.id.main_btn)
        result_info = findViewById(R.id.result_info)

        main_btm?.setOnClickListener {
            if (user_field?.text?.toString()?.trim()?.equals("")!!) {
                Toast.makeText(this, "Write City", Toast.LENGTH_SHORT).show()
            } else {
                var city: String = user_field?.text.toString()
                var key: String = "61e19bf39c70eb08c42cc9ae8b30ccc2"
                var url: String ="https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$key&units=metric&lang=ru"

                seeWheather(url)

            }

        }
    }

    fun seeWheather(url: String): Deferred<String> = GlobalScope.async {
        val apiResponse = URL(url).readText()
        Log.d("INFO", apiResponse)
        val weather = JSONObject(apiResponse).getJSONArray("weather")
        val desc = weather.getJSONObject(0).getString("description")

        val main = JSONObject(apiResponse).getJSONObject("main")
        val temp = main.getString("temp")
        val feel = main.getString("feels_like")

        val wind = JSONObject(apiResponse).getJSONObject("wind")
        val windSpeed = wind.getString("speed")

        val clouds = JSONObject(apiResponse).getJSONObject("clouds")
        val cloud = clouds.getString("all")

        result_info?.text = "Температура $temp градусов \n $desc\n Ощущаетс как: $feel \n Порыв ветра $windSpeed М/С \n Облачность: $cloud "

        return@async apiResponse
    }


}