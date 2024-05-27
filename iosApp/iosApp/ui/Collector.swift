//
//  Collector.swift
//  iosApp
//
//  Created by Ander Caro on 26/5/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import Shared

class Collector<T>: Kotlinx_coroutines_coreFlowCollector{
    
    let callback:(T) -> Void
    
    init(callback: @escaping (T) -> Void){
        self.callback = callback
    }
    
    func emit(value: Any?, completionHandler: @escaping ((any Error)?) -> Void) {
        callback(value as! T)
        
        completionHandler(nil)
    }

    
    
}
