package com.example.star_wars

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.star_wars.databinding.ActivityMainBinding
import okhttp3.Headers
import java.math.BigInteger
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var characterImageURL = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getCharacterName()
        getCharacterImageURL()
    }

    private fun getCharacterName(){

        val timestamp = System.currentTimeMillis()
        val privateKey = getString(R.string.private_key_api_marval)
        val publicKey = getString(R.string.public_key_api_marval)
        val hash = stringToMd5("$timestamp$privateKey$publicKey")

        Log.d("Marvel Hash", "successful-$hash")

        val url =
            "https://gateway.marvel.com/v1/public/characters?limit=1&ts=$timestamp&apikey=$publicKey&hash=$hash"

        val client = AsyncHttpClient()

        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JsonHttpResponseHandler.JSON
            ) {
                Log.d("Marvel JSON", "response successful $json")

                val dataObject = json.jsonObject.getJSONObject("data")
                Log.d("Marvel Data Object", "Marvel data $dataObject")

                val results = dataObject.getJSONArray("results")
                Log.d("Marvel results array", "Marvel results array $results")

                val resultIndex0 = results.getJSONObject(0)
                val name = resultIndex0.optString("name")
                Log.d("Marvel name", "Marvel $name")

                binding.characterName.text = name
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Marvel Name Error", errorResponse)
            }
        }]
    }

    private fun getCharacterImageURL(){

        val timestamp = System.currentTimeMillis()
        val privateKey = getString(R.string.private_key_api_marval)
        val publicKey = getString(R.string.public_key_api_marval)
        val hash = stringToMd5("$timestamp$privateKey$publicKey")

        Log.d("marvelHash", "succeshuL-$hash")

        val url =
            "https://gateway.marvel.com/v1/public/characters?limit=1&ts=$timestamp&apikey=$publicKey&hash=$hash"

        val client = AsyncHttpClient()

        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JsonHttpResponseHandler.JSON
            ) {
                Log.d("Marvel JSON", "response successful $json")

                val dataObject = json.jsonObject.getJSONObject("data")
                Log.d("Marvel Data Object", "Marvel data $dataObject")

                val results = dataObject.getJSONArray("results")
                Log.d("Marvel results array", "Marvel results array $results")

                val resultIndex0 = results.getJSONObject(0)
               // val name = resultIndex0.optString("thumbnail")
                //Log.d("Marvel name", "Marvel $name")

              // binding.characterName.text = name

                val characterImageObject = resultIndex0.getJSONObject("thumbnail")
                val path = characterImageObject.getString("path")
                Log.d("Marvel path", "Marvel thumb $path")
                val imageExtension = characterImageObject.getString("extension")

                characterImageURL = "$path.$imageExtension"
                Log.d("Marvel image", "Marvel image $characterImageURL")


            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Marvel Error", errorResponse)
            }
        }]
    }
    private fun stringToMd5(input : String) : String{
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}