//
//  BareIPC.swift
//  App
//
//  Created by joker on 2025-01-30.
//

import Foundation
import BareKit
import ComposeApp

class IPCViewModel: ObservableObject {
    private var ipc: IPC?
//    private let userRepository: UserRepository

    @Published var publicKey: String = ""
    @Published var people: [People] = []

    private var ipcBuffer = ""
    
//    init(userRepository: UserRepository) {
//        self.userRepository = userRepository
//    }


    func configure(with ipc: IPC?) {
        self.ipc = ipc
//        observePublicKeyFlow()
    }

//    private func observePublicKeyFlow() {
//        let flow = userRepository.currentPublicKey
//
//        Task {
//            for await key in flow {
//                DispatchQueue.main.async { [self] in
//                    self.publicKey = key
//                }
//            }
//        }
//    }
    
    
    func writeToIPC(message: [String: Any]) async {
        guard let ipc = self.ipc else {
            print("Error: IPC object is nil, cannot read.")
            return
        }
        
        do {
            var data = try JSONSerialization.data(withJSONObject: message, options: [])
            data.append("\n".data(using: .utf8)!)
            try await ipc.write(data: data)
        } catch(let error) {
            print("Debug: Error writing to IPC: \(error.localizedDescription)")
        }
    }
}
