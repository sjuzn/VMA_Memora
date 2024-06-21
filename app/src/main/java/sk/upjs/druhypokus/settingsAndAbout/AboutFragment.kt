package sk.upjs.druhypokus.settingsAndAbout

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import sk.upjs.druhypokus.R

//https://www.geeksforgeeks.org/how-to-use-webview-in-android/

class AboutFragment : Fragment() {

    private lateinit var mainView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainView = inflater.inflate(R.layout.fragment_about, container, false)

        mainView.findViewById<TextView>(R.id.a)?.setOnClickListener(otvorWebView)
        mainView.findViewById<TextView>(R.id.b)?.setOnClickListener(otvorWebView)
        mainView.findViewById<TextView>(R.id.c)?.setOnClickListener(otvorWebView)
        mainView.findViewById<TextView>(R.id.d)?.setOnClickListener(otvorWebView)
        mainView.findViewById<TextView>(R.id.e)?.setOnClickListener(otvorWebView)
        mainView.findViewById<TextView>(R.id.f)?.setOnClickListener(otvorWebView)
        mainView.findViewById<TextView>(R.id.g)?.setOnClickListener(otvorWebView)
        mainView.findViewById<TextView>(R.id.h)?.setOnClickListener(otvorWebView)

        return mainView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AboutFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    @SuppressLint("SetJavaScriptEnabled")
    val otvorWebView: OnClickListener = View.OnClickListener { view ->

        val info = mainView.findViewById<LinearLayout>(R.id.info)
        info.visibility = View.INVISIBLE
        val webView = mainView.findViewById<WebView>(R.id.web)
        webView.visibility = View.VISIBLE

        val kliknute = view as TextView

        webView.loadUrl("https://" + kliknute.text.toString())

        // this will enable the javascript.
        webView.settings.javaScriptEnabled = true

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.webViewClient = WebViewClient()

    }

}