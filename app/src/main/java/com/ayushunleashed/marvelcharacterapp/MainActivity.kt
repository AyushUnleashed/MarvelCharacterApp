package com.ayushunleashed.marvelcharacterapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    var imageURL:String? = null
    var imagePath:String? = null
    var characterName:String? = null
    private var idx:Int =0
    var detailsUrl:String = "sampleURL"
    var comicsUrl:String = "sampleURL"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMarvelCharacter()
    }
    private fun loadMarvelCharacter()
    {
        progressBar.visibility = View.VISIBLE
        //creating request queue
        val queue = Volley.newRequestQueue(this)
        //getting jsonObject from given url

        val apiUrlCharacter = "https://gateway.marvel.com/v1/public/characters?ts=1&apikey=fc9b9123fa5470cc9d992cf776975686&hash=b26883f2efd63c25536f035e8d8e30e8"


        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, apiUrlCharacter, null,
            { response ->
                //getting array
                var jsonArrayResults: JSONArray = response.getJSONObject("data").getJSONArray("results")
                var urlArray:JSONArray = jsonArrayResults.getJSONObject(idx).getJSONArray("urls");
                var attributionText:String = response.getString("attributionText")
                var characterId:Int = jsonArrayResults.getJSONObject(idx).getInt("id")

                if(characterId == 1010870)
                {
                    idx=0; //so that it become 0 next time
                }
                attributionView.text = attributionText + "@AyushUnleashed";

                detailsUrl = urlArray.getJSONObject(0).getString("url");
                comicsUrl = urlArray.getJSONObject(urlArray.length()-1).getString("url")
                //getting image path and charcter Name

                imagePath = jsonArrayResults.getJSONObject(idx).getJSONObject("thumbnail").getString("path")
                imageURL = imagePath.toString() + "/portrait_incredible.jpg"
                characterName = jsonArrayResults.getJSONObject(idx).getString("name")
                characterNameView.text = characterName

                //increasing index each time app is loaded
                idx++;

                Log.d("HogyaLoadImage",imagePath.toString())
                Glide.with(this).load(imageURL).listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                }).into(characterImageView)
            },
            { error ->
                Log.d("NhiHuaImageLoad",error.localizedMessage)
                Log.d("Marvel",imagePath.toString())
            }
        )
            // Access the RequestQueue through your singleton class.
       MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun shareMarvelCharacter(view: View) {

        //explicit intent  to travel between processes
        //creating intent to send action
        val intent = Intent(Intent.ACTION_SEND)
        //type of file to send
        intent.type = "text/plain"
      // putting text to be shared
        intent.putExtra(Intent.EXTRA_TEXT,"Hey guys checkout this character ${imageURL}")

        //chooser for sharing with what to say
        val chooser = Intent.createChooser(intent,"Share via:")
        //starting chooser
        startActivity(chooser)
    }

    fun nextMarvelCharacter(view: View) {
    loadMarvelCharacter();
    }

    fun listAllComics(view: View) {
        val intent= Intent(this,MyWebViewOP::class.java);
        intent.putExtra("comicsUrl",comicsUrl)
        startActivity(intent);
    }

    fun showComicDetails(view: View)
    {
            val intent= Intent(this,MyWebViewOP::class.java);
            intent.putExtra("detailsUrl",detailsUrl)
            startActivity(intent);
    }
}

