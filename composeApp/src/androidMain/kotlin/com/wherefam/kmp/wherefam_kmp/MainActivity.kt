package com.wherefam.kmp.wherefam_kmp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.wherefam.kmp.wherefam_kmp.data.IPCMessageConsumer
import com.wherefam.kmp.wherefam_kmp.data.IpcManager
import com.wherefam.kmp.wherefam_kmp.managers.LocationTrackerService
import com.wherefam.kmp.wherefam_kmp.processing.GenericMessageProcessor
import com.wherefam.kmp.wherefam_kmp.ui.onboarding.SplashViewModel
import com.wherefam.kmp.wherefam_kmp.ui.root.ContentView
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import to.holepunch.bare.kit.IPC
import to.holepunch.bare.kit.Worklet

class MainActivity : ComponentActivity() {

    private var worklet: Worklet? = null
    private var ipc: IPC? = null
    private val messageProcessor: GenericMessageProcessor by inject()
    private var ipcMessageConsumer: IPCMessageConsumer? = null
    private val splashViewModel: SplashViewModel by viewModel()
    private val ipcManager by inject<IpcManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
        }

        worklet = Worklet(null)

        try {
            worklet!!.start("/app.bundle", assets.open("app.bundle"), null)
            ipc = IPC(worklet)
            ipcManager.setIPC(ipc)
            ipcMessageConsumer = IPCMessageConsumer(ipc!!, messageProcessor)
            ipcMessageConsumer?.lifecycleScope = lifecycleScope
            ipcMessageConsumer?.startConsuming()


        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        val channel = NotificationChannel(
            LocationTrackerService.LOCATION_CHANNEL,
            "Location",
            NotificationManager.IMPORTANCE_LOW
        )

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        installSplashScreen().setKeepOnScreenCondition {
            !splashViewModel.isLoading.value
        }

        lifecycleScope.launch {
            setContent {
                val screen by splashViewModel.startDestination
                val navController = rememberNavController()
                ContentView(navController, screen)
            }
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

        Intent(applicationContext, LocationTrackerService::class.java).apply {
            action = LocationTrackerService.Action.STOP.name
            startService(this)
        }
    }
}
