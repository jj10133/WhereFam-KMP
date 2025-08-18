import SwiftUI
import BareKit

@main
struct iOSApp: App {
    
    private var worklet = Worklet()
    
    @Environment(\.scenePhase) private var scenePhase
    
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
