//
//  IPCViewModel.swift
//  iosApp
//
//  Created by joker on 2025-08-18.
//
//


import Foundation
import BareKit
import ComposeApp


class IPCViewModel: ObservableObject {
    var ipc: IPC?
    
    private var genericProcessor: GenericMessageProcessor = get()
    
    
    private var ipcBuffer = ""
    
    func configure(with ipc: IPC?) {
        self.ipc = ipc
    }
    
    func readFromIPC() async {
        guard let ipc = self.ipc else {
            print("Error: IPC object is nil, cannot read.")
            return
        }
        
        do {
            for try await chunk in ipc {
                if let chunkString = String(data: chunk, encoding: .utf8) {
                    try await genericProcessor.processMessage(message: chunkString)
                }
            }
        } catch {
            print("Error reading from IPC: \(error.localizedDescription)")
        }
    }
}
