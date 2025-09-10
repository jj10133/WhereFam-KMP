package com.wherefam.kmp.wherefam_kmp.ui.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.wherefam.kmp.wherefam_kmp.managers.LocationManager
import com.wherefam.kmp.wherefam_kmp.managers.LocationTrackerService
import com.wherefam.kmp.wherefam_kmp.ui.home.people.PeopleView
import com.wherefam.kmp.wherefam_kmp.ui.home.share.ShareIDView
import com.wherefam.kmp.wherefam_kmp.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.location.modes.CameraMode
import org.maplibre.android.location.modes.RenderMode
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.plugins.annotation.Symbol
import org.maplibre.android.plugins.annotation.SymbolManager


@Composable
@ExperimentalMaterial3Api
fun HomeView(
    homeViewModel: HomeViewModel = koinViewModel(),
    locationManager: LocationManager = koinInject()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

//    val cameraPosition = rememberSaveable { mutableStateOf(CameraPosition(zoom = 14.0)) }
    val renderMode = rememberSaveable { mutableIntStateOf(RenderMode.NORMAL) }

    var selectedOption by remember { mutableStateOf<MenuOption?>(null) }
    var bottomSheetVisible by remember { mutableStateOf(false) }
    var shouldShareLink by remember { mutableStateOf(false) }
    var shouldRateApp by remember { mutableStateOf(false) }
    var dialogInput by remember { mutableStateOf("") }

    val peers by homeViewModel.peers.collectAsState()
    val userLocation = locationManager.trackLocation().collectAsState(initial = null)

    var mapLibreMap by remember { mutableStateOf<MapLibreMap?>(null) }
    var symbolManager by remember { mutableStateOf<SymbolManager?>(null) }
    var userSymbol by remember { mutableStateOf<Symbol?>(null) }

    LaunchedEffect(Unit) {
        homeViewModel.start(context.filesDir.path)
        delay(3000)
        homeViewModel.joinAllExistingPeers()
    }

    LaunchedEffect(Unit) {
        val intent = Intent(context, LocationTrackerService::class.java).apply {
            action = LocationTrackerService.Action.START.name
        }
        context.startService(intent)
    }

    Scaffold(
        floatingActionButton = {
            Menu(
                onPeopleSelected = {
                    selectedOption = MenuOption.People
                    bottomSheetVisible = true
                },
                onShareIDSelected = {
                    selectedOption = MenuOption.ShareID
                    bottomSheetVisible = true
                },
                onReferFriendSelected = {
                    shouldShareLink = true
                },
                onRateAppSelected = {
                    shouldRateApp = true
                },
                onSupportAppSelected = {

                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        Box {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                AndroidView(
                    factory = {
                       MapView(it).apply {
                           onCreate(null)
                           getMapAsync { map ->
                               mapLibreMap = map
                               mapLibreMap?.setStyle(Style.Builder().fromUri("https://tiles.openfreemap.org/styles/liberty")) { style ->
                                   val locationComponent = mapLibreMap?.locationComponent
                                   val activationOptions = LocationComponentActivationOptions.Builder(context, style).build()
                                   locationComponent?.activateLocationComponent(activationOptions)
                                   locationComponent?.isLocationComponentEnabled = true
                                   locationComponent?.cameraMode = CameraMode.TRACKING
                               }
                           }
                       }
                    },
                    modifier = Modifier.fillMaxSize()
                ) {

                }
            }


            if (bottomSheetVisible) {
                ModalBottomSheet(
                    sheetState = rememberModalBottomSheetState(),
                    content = {
                        Box(modifier = Modifier.fillMaxSize()) {
                            when (selectedOption) {
                                MenuOption.People -> {
                                    PeopleView()
                                }

                                MenuOption.ShareID -> ShareIDView()
                                else -> {}
                            }
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    onDismissRequest = { bottomSheetVisible = false }
                )
            }

            if (shouldShareLink) {
                ReferView()
                shouldShareLink = false
            }

            if (shouldRateApp) {
                RateView()
                shouldRateApp = false
            }
        }
    }
}

@Composable
fun ReferView() {
    val context = LocalContext.current
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Check out: https://www.wherefam.com")
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share URL"))
}

@Composable
fun RateView() {
    val context = LocalContext.current
    val appPackageName = context.packageName
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

