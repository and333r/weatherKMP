//
//  dailyWeather.swift
//  iosApp
//
//  Created by Ander Caro on 5/5/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI

@available(iOS 16.0, *)
struct DailyWeather: View {
    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 10) {
                ForEach(0..<23) {_ in 
                    cardConstructor(code: 1, temperature: 12, hour: "16")
                }
            }
        }
        
    }
}



struct dailyWeather_previewer: PreviewProvider {
    static var previews: some View {
        VStack{
            DailyWeather()
        }
    }
}
