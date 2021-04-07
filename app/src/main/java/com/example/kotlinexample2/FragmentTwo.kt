package com.example.kotlinexample2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import java.util.*

class FragmentTwo : Fragment() {

    private val viewModel:UserViewModel by activityViewModels()
    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_two, container, false)
        textView = view.findViewById(R.id.text_view_two)

        // call getinformation, which returns a mutable live data so that we can observe
        // the live data to get changes
        // then display the information in the UI

        /**viewModel.getInformation().observe(viewLifecycleOwner, object:Observer<UserInformation>{

            override fun onChanged(t: UserInformation?) {
                if (t != null){
                    textView.text = t.name + " : " + t.zipcode
                }
            }

        })*/

        viewModel.getInformation().observe(viewLifecycleOwner, Observer{
            userInfo -> userInfo?.let{
                textView.text = it.name + " : " + it.zipcode + ", " + it.cityName
            }
            println(userInfo)
            // let - scope function
            // the context object is available as an argument called "it"
            // the return value is a lambda result

            // ?. - safe call operator
            // let only works with non-null objects
        })

        return view
    }
}