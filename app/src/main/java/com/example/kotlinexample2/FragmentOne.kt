package com.example.kotlinexample2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject


class FragmentOne : Fragment() {

    // retrieve the ViewModel in the activity scope
    private val viewModel: UserViewModel by activityViewModels()

    private lateinit var editTextName: EditText
    private lateinit var editTextZipCode: EditText
    private lateinit var button: Button

    private val api_root: String = "http://api.zippopotam.us/us/"
    private val client: AsyncHttpClient = AsyncHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_one, container, false)
        editTextName = view.findViewById(R.id.editText_name)
        editTextZipCode = view.findViewById(R.id.editText_zip)
        button = view.findViewById(R.id.button_submit)

        // when button is clicked
        // I want to update the user information in the view model with the new data

        button.setOnClickListener{
            // validate here
            val zip = editTextZipCode.text.toString()
            var valid = true
            if (zip.length == 5){
                for (i in zip){
                    if (!i.isDigit()){
                        valid = false
                    }
                }
            }
            else{
                valid = false
            }
            if (valid){
                callApi(zip)
            }
            else{
                Toast.makeText(view.context, "Invalid zipcode",Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    fun callApi(zipcode:String){
        var cityName: String = ""
        val api_call = api_root + zipcode
        client.get(api_call, object: AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                if (responseBody != null){
                    Log.d("api", String(responseBody))
                    val json = JSONObject(String(responseBody))
                    Log.d("json", json.toString())
                    val jsonArray = json.getJSONArray("places")
                    Log.d("jsonArray", jsonArray.toString())
                    val places = jsonArray.getJSONObject(0)
                    Log.d("places", places.toString())
                    cityName = places.getString("place name")
                    Log.d("cityName", cityName)
                    val userInfo = UserInformation(editTextName.text.toString(), editTextZipCode.text.toString(), cityName)
                    viewModel.setInformation(userInfo)
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.e("api error", responseBody.toString())
            }

        })
    }

}