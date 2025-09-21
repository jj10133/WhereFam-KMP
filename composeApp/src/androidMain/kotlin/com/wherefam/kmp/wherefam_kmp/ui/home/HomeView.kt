package com.wherefam.kmp.wherefam_kmp.ui.home

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback
import com.wherefam.kmp.wherefam_kmp.R
import com.wherefam.kmp.wherefam_kmp.managers.LocationTrackerService
import com.wherefam.kmp.wherefam_kmp.ui.home.people.PeopleView
import com.wherefam.kmp.wherefam_kmp.ui.home.share.ShareIDView
import com.wherefam.kmp.wherefam_kmp.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.location.modes.CameraMode
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.plugins.annotation.Symbol
import org.maplibre.android.plugins.annotation.SymbolManager
import org.maplibre.android.plugins.annotation.SymbolOptions
import org.maplibre.android.style.layers.Property.TEXT_ANCHOR_TOP


@Composable
@ExperimentalMaterial3Api
fun HomeView(
    navController: NavHostController,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val context = LocalContext.current


    var selectedOption by remember { mutableStateOf<MenuOption?>(null) }
    var bottomSheetVisible by remember { mutableStateOf(false) }
    var shouldShareLink by remember { mutableStateOf(false) }
    var shouldRateApp by remember { mutableStateOf(false) }
    var dialogInput by remember { mutableStateOf("") }
    var shouldShowPaywall by remember { mutableStateOf(false) }
    var isSupporter by remember { mutableStateOf(false) }

    val peers by homeViewModel.peers.collectAsState()

    var mapLibreMap by remember { mutableStateOf<MapLibreMap?>(null) }
    var symbolManager by remember { mutableStateOf<SymbolManager?>(null) }
    val peerSymbols = remember { mutableStateMapOf<String, Symbol>() }

    LaunchedEffect(Unit) {
        val intent = Intent(context, LocationTrackerService::class.java).apply {
            action = LocationTrackerService.Action.START.name
        }
        context.startService(intent)
    }

    LaunchedEffect(peers, symbolManager) {
        if (symbolManager != null) {
            val map = mapLibreMap
            if (map != null && map.style?.isFullyLoaded == true) {

                val style = map.style!!
                val peerIconId = "peer-icon"
                style.addImageAsync(
                    peerIconId, BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.icons8_test_account_24
                    )
                )

                // Keep track of peer IDs currently in the `peers` list
                val currentPeerIds = peers.map { it.id }.toSet()

                // Remove symbols for peers that are no longer in the list
                val peersToRemove = peerSymbols.keys.toSet() - currentPeerIds
                peersToRemove.forEach { id ->
                    val symbol = peerSymbols[id]
                    if (symbol != null) {
                        symbolManager?.delete(symbol)
                        peerSymbols.remove(id)
                    }
                }

                // Create or update symbols for the current peers
                peers.forEach { peer ->
                    val latLng = LatLng(peer.latitude, peer.longitude)
                    val existingSymbol = peerSymbols[peer.id]

                    if (existingSymbol != null) {
                        existingSymbol.latLng = latLng
                        existingSymbol.textField = peer.name
                        symbolManager?.update(existingSymbol)
                    } else {
                        val symbolOptions = SymbolOptions()
                            .withLatLng(latLng)
                            .withIconImage(peerIconId)
                            .withIconSize(1f)
                            .withTextField(peer.name)
                            .withTextSize(15.0f)
                            .withTextFont(arrayOf("Noto Sans Regular"))
                            .withTextAnchor(TEXT_ANCHOR_TOP)
                            .withTextOffset(arrayOf(0f, 0.8f))

                        val newSymbol = symbolManager?.create(symbolOptions)
                        if (newSymbol != null) {
                            peerSymbols[peer.id] = newSymbol

                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        Purchases.sharedInstance.getCustomerInfo(object : ReceiveCustomerInfoCallback {
            override fun onError(error: PurchasesError) {
                TODO("Not yet implemented")
            }

            override fun onReceived(customerInfo: CustomerInfo) {
                // Replace with your actual entitlement ID from RevenueCat dashboard
                val entitlementId = "Tip"

                val hasActiveEntitlement = customerInfo.entitlements.active.containsKey(entitlementId)
                isSupporter = hasActiveEntitlement
            }
        })
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
                    shouldShowPaywall = true
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
                MapXmlViewWrapper(
                    modifier = Modifier.fillMaxSize()
                ) { map, mapView ->
                    mapLibreMap = map

                    map.getStyle { style ->
                        symbolManager = SymbolManager(mapView, map, style).apply {
                            iconAllowOverlap = true
                            textAllowOverlap = true
                        }
                        val cameraPosition = CameraPosition.Builder().zoom(14.0).build()
                        map.cameraPosition = cameraPosition

                        homeViewModel.start(context.filesDir.path)

                        val locationComponent = map.locationComponent
                        val activationOptions = LocationComponentActivationOptions.Builder(context, style).build()
                        locationComponent.activateLocationComponent(activationOptions)
                        locationComponent.isLocationComponentEnabled = true
                        locationComponent.cameraMode = CameraMode.TRACKING
                    }
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

            if (shouldShowPaywall) {
                if (isSupporter) {
                    ThankYouView()
                } else {
                    navController.navigate("Paywall")
                    shouldShowPaywall = false
                }
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

@Composable
fun MapXmlViewWrapper(
    modifier: Modifier = Modifier,
    onMapReady: (MapLibreMap, MapView) -> Unit
) {

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            // Inflate from XML
            val inflater = LayoutInflater.from(ctx)
            val view = inflater.inflate(R.layout.map_view, null, false)
            val mapView = view.findViewById<MapView>(R.id.map_view)

            mapView.onCreate(null)
            mapView.onStart()
            mapView.onResume()

            mapView.getMapAsync { mapLibreMap ->
                mapLibreMap.setStyle("https://tiles.openfreemap.org/styles/liberty")
                onMapReady(mapLibreMap, mapView)
            }

            view
        },
        update = {}
    )
}

@Composable
fun ThankYouView() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "❤️ Thank you for your support!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
