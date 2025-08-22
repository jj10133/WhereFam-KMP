import SwiftUI
import BareKit
import ComposeApp

@main
struct iOSApp: App {
    
    private var worker = Worker()
    private var ipcViewModel = IPCViewModel()
    
    @Environment(\.scenePhase) private var scenePhase
    
    init() {
        DependenciesProviderHelper().doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onAppear {
                    worker.start()
                    ipcViewModel.configure(with: worker.ipc)
                    Task(priority: .background) {
                        await ipcViewModel.readFromIPC()
                    }
                }
                .onDisappear {
                    worker.terminate()
                }
        }
        .onChange(of: scenePhase) { oldValue, newValue in
            switch newValue {
            case .background:
                worker.suspend()
            case .active:
                worker.resume()
            default:
                break
            }
        }
    }
}
