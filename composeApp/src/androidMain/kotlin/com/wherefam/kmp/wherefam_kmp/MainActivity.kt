package com.wherefam.kmp.wherefam_kmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import com.wherefam.kmp.wherefam_kmp.data.IPCMessageConsumer
import com.wherefam.kmp.wherefam_kmp.processing.GenericMessageProcessor
import com.wherefam.kmp.wherefam_kmp.ui.App
import org.koin.android.ext.android.inject
import to.holepunch.bare.kit.IPC
import to.holepunch.bare.kit.Worklet

class MainActivity : ComponentActivity() {

    private var worklet: Worklet? = null
    private var ipc: IPC? = null
    private val messageProcessor: GenericMessageProcessor by inject()
    private var ipcMessageConsumer: IPCMessageConsumer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        worklet = Worklet(null)

        try {
            worklet!!.start("/app.bundle", assets.open("app.bundle"), null)
            ipc = IPC(worklet)

            ipcMessageConsumer = IPCMessageConsumer(ipc!!, messageProcessor)
            ipcMessageConsumer?.lifecycleScope = lifecycleScope
            ipcMessageConsumer?.startConsuming()


        } catch (e: Exception) {
            throw RuntimeException( e)
        }

        setContent {
            App(
                prefs = remember {
                    createDataStore(applicationContext)
                }
            )
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
