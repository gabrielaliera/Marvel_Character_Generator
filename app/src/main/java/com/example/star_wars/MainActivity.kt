package com.example.star_wars

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import java.math.BigInteger
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    var characterImageURL = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timestamp = System.currentTimeMillis()
        val privateKey = getString(R.string.private_key_api_marval)
        val publicKey = getString(R.string.public_key_api_marval)
        val hash = stringToMd5("$timestamp$privateKey$publicKey")

        Log.d("marvelHash", "succeshuL-$timestamp$privateKey$publicKey")
        Log.d("marvelHash", "succeshuL-$hash")
        val url =
            "https://gateway.marvel.com/v1/public/characters?ts=$timestamp&apikey=$publicKey&hash=$hash"
    }

    private fun characterImageURL( url : String){
        val client = AsyncHttpClient()
        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JsonHttpResponseHandler.JSON
            ) {
                Log.d("Marvel", "response successful$json")
                characterImageURL = json.jsonObject.getString("message")
                Log.d("MarvelImageURL", "Marvel image URL set")
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