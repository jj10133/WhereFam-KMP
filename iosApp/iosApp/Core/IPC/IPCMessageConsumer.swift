//
//  IPCMessageConsumer.swift
//  iosApp
//
//  Created by joker on 2025-08-18.
//
//


import Foundation
import BareKit
import ComposeApp


class IPCMessageConsumer {
    private var ipc: IPC?
    private var ipcBuffer = ""
    
    private var messageProcessor: MessageProcessor
    
    init(messageProcessor: MessageProcessor) {
        self.messageProcessor = messageProcessor
    }
    
    func configure(with ipc: IPC?) {
        self.ipc = ipc
        IpcManager().setIPC(ipc: self.ipc)
    }
    
    func startConsuming() async {
        guard let ipc = self.ipc else {
            print("Error: IPC object is nil, cannot read.")
            return
        }
        
        do {
            for try await chunk in ipc {
                if let chunkString = String(data: chunk, encoding: .utf8) {
                    try await messageProcessor.processMessage(message: chunkString)
                }
            }
        } catch {
            print("Error reading from IPC: \(error.localizedDescription)")
        }
    }
}
