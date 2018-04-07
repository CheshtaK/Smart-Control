package com.example.cheshta.smartcontrol.help


import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.cheshta.smartcontrol.R
import kotlinx.android.synthetic.main.fragment_help.*


/**
 * A simple [Fragment] subclass.
 */
class HelpFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        helpFragmentWebView.loadUrl(getString(R.string.help_url))
        val webSettings = helpFragmentWebView.settings
        webSettings.javaScriptEnabled = true

        helpFragmentWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                view!!.visibility = View.VISIBLE
                view.bringToFront()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                view!!.visibility = View.GONE
            }
        }

        Toast.makeText(activity, "Loading, official github repository", Toast.LENGTH_LONG).show()

        return inflater!!.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setTitle(resources.getString(R.string.help))
    }
}// Required empty public constructor
