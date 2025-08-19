import Foundation
import BareKit

class Worker: ObservableObject {
    private var worklet: Worklet?
    public var ipc: IPC?
    
    func start() {
        worklet = Worklet()
        worklet?.start(name: "app", ofType: "bundle")
        
        if let worklet = worklet {
            ipc = IPC(worklet: worklet)
        }
    }
    
    func suspend() {
        worklet?.suspend()
    }
    
    func resume() {
        worklet?.resume()
    }
    
    func terminate() {
        worklet?.terminate()
    }
}
