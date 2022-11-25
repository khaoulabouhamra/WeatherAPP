package com.khaoula4.weatherapp

import org.json.JSONObject

import java.text.SimpleDateFormat
import java.util.*

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import java.net.URL

class MainActivity : AppCompatActivity() {
    val CITY:String ="Marrakech"
            val API:String ="06c92170b9a82d8f5d1294e158676f"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherTask().execute()

    }
    inner class weatherTask() : AsyncTask<String,void,String>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility= View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility=View.GONE
            findViewById<TextView>(R.id.errorText).visibility=View.GONE

        }

        override fun doInBackground(vararg p0: String?): String {
            TODO("Not yet implemented")
        }
    }
    fun doInBackground(vararg p0:String): String? {
        var response :String?
        try{ response=URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(Charsets.UTF_8)
        }catch (e:Exception){
            response= null
        }return response
    }
    fun onPostExecute(result: String?) {
        super.onPause()
        try {
            /* Extracting JSON returns from the API */
            val jsonObj = JSONObject(result)
            val main = jsonObj.getJSONObject("main")
            val sys = jsonObj.getJSONObject("sys")
            val wind = jsonObj.getJSONObject("wind")
            val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

            val updatedAt:Long = jsonObj.getLong("dt")
            val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
            val temp = main.getString("temp")+"°C"
            val tempMin = "Min Temp: " + main.getString("temp_min")+"°C"
            val tempMax = "Max Temp: " + main.getString("temp_max")+"°C"


            val sunrise:Long = sys.getLong("sunrise")

            val weatherDescription = weather.getString("description")

            val address = jsonObj.getString("name")+", "+sys.getString("country")

            /* Populating extracted data into our views */
            findViewById<TextView>(R.id.address).text = address
            findViewById<TextView>(R.id.updated_at).text =  updatedAtText
            findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
            findViewById<TextView>(R.id.temp).text = temp
            findViewById<TextView>(R.id.temp_min).text = tempMin
            findViewById<TextView>(R.id.temp_max).text = tempMax
            findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))


            /* Views populated, Hiding the loader, Showing the main design */
            findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE

        } catch (e: Exception) {
            findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
        }

    }
}


