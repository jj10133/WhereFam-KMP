package com.wherefam.kmp.wherefam_kmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import to.holepunch.bare.kit.Worklet

class MainActivity : ComponentActivity() {

    private var worklet: Worklet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        worklet = Worklet(null)

        try {
            worklet!!.start("/app.bundle", assets.open("app.bundle"), null)
        } catch (e: Exception) {
            throw RuntimeException( e)
        }

        setContent {
            App()
        }
    }

    override fun onPause() {
        super.onPause()
        worklet!!.suspend()
    }

    override fun onResume() {
        super.onResume()
        worklet!!.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        worklet!!.terminate()
        worklet = null
    }
}
