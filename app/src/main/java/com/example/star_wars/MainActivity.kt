package com.example.star_wars

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.star_wars.databinding.ActivityMainBinding
import okhttp3.Headers
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var characterImageURL = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // val button = binding.button
      // val imageView = binding.characterImage

        val button = findViewById<Button>(R.id.button)
        val imageView = findViewById<ImageView>(R.id.characterImage)

      // getCharacterName()
      //  getCharacterImageURL()
        getNextImage(button, imageView)
    }

    private fun getCharacterName(randomNum: Int){

        val timestamp = System.currentTimeMillis()
        val privateKey = getString(R.string.private_key_api_marval)
        val publicKey = getString(R.string.public_key_api_marval)
        val hash = stringToMd5("$timestamp$privateKey$publicKey")

        Log.d("Marvel Hash", "successful-$hash")

        val url =
            "https://gateway.marvel.com/v1/public/characters?limit=1&offset=$randomNum&ts=$timestamp&apikey=$publicKey&hash=$hash"

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

    private fun getCharacterImageURL(randomNum: Int){

        val timestamp = System.currentTimeMillis()
        val privateKey = getString(R.string.private_key_api_marval)
        val publicKey = getString(R.string.public_key_api_marval)
        val hash = stringToMd5("$timestamp$privateKey$publicKey")

        Log.d("marvelHash", "succeshuL-$hash")

        val url =
            "https://gateway.marvel.com/v1/public/characters?limit=1&offset=$randomNum&ts=$timestamp&apikey=$publicKey&hash=$hash"

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

                characterImageURL = characterImageURL.replace("http:", "https:")
                Log.d("Marvel replace image", "Marvel image $characterImageURL")


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

    private fun getNextImage(button: Button, imageView: ImageView){

        button.setOnClickListener{
            val randomNum = Random.nextInt(1000)
            Log.d("Marvel Num", "random $randomNum")

            getCharacterName(randomNum)
            getCharacterImageURL(randomNum)

            Glide.with(this)
                . load(characterImageURL)
                .fitCenter()
                .into(imageView)
        }
    }

    private fun stringToMd5(input : String) : String{
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}