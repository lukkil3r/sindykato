package com.luismakeapp.sindykato.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.luismakeapp.sindykato.R


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(this, Observer {
            textView.text = it
        })

        //--------------------------------------------------------------------------------------------------------
        val button: Button = root.findViewById(R.id.btn_seguinte)
        val janelaimagem: ImageView = root.findViewById(R.id.image_view)


        var i = 0

        button.setOnClickListener {
            if (i > 2){
                i = 0
            }
            when(i) {
                0 -> janelaimagem.setImageResource(R.drawable.item2)
                1 -> janelaimagem.setImageResource(R.drawable.item3)
                2 -> janelaimagem.setImageResource(R.drawable.item1)
            }
            i++
        }


        //-------------------------------------------------------------------------------------------------------


        return root
    }

}