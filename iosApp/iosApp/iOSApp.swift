import SwiftUI
import BareKit
import ComposeApp

@main
struct iOSApp: App {
    
    private var worklet = Worklet()
    
    @Environment(\.scenePhase) private var scenePhase
    
    init() {
        DependenciesProviderHelper().doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onAppear {
                    worklet.start(name: "app", ofType: "bundle")
                }
                .onDisappear {
                    worklet.terminate()
                }
        }
        .onChange(of: scenePhase) { oldValue, newValue in
            switch newValue {
            case .background:
                worklet.suspend()
            case .active:
                worklet.resume()
            default:
                break
            }
        }
    }
}
