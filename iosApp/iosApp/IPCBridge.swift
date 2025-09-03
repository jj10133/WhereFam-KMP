//
//  IPCBridge.swift
//  iosApp
//
//  Created by joker on 2025-09-02.
//

import Foundation
import BareKit
import ComposeApp


class IPCBridge: IPCProvider {
    
    private var ipc: IPC?
    
    func getIPC() -> Any {
        return self.ipc as Any
    }
    
    func setIPC(ipc: Any?) {
        self.ipc = ipc as? IPC
    }
    
    func __write(jsonString: String) async throws {
        guard let ipc = self.ipc else {
            throw NSError(domain: "IPC", code: 1, userInfo: [NSLocalizedDescriptionKey: "IPC not initialized"])
        }
        
        guard let data = jsonString.data(using: .utf8) else {
            throw NSError(domain: "IPC", code: 2, userInfo: [NSLocalizedDescriptionKey: "Invalid UTF-8 string"])
        }
        
        try await ipc.write(data: data)
    }
    
    
}

