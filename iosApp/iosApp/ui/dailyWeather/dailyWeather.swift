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
    @ObservedObject var DailyWeatherViewModel: dailyWeatherViewModel

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 10) {
                ForEach(0...DailyWeatherViewModel.temperatures.count-1, id: \.self) {i in
                    cardConstructor(code: DailyWeatherViewModel.codes[i], temperature: Int(DailyWeatherViewModel.temperatures[i]), hour: DailyWeatherViewModel.hours[i])
                }
            }
        }.task{
            await DailyWeatherViewModel.getAllData()
        }
        
    }
}



struct dailyWeather_previewer: PreviewProvider {
    static var previews: some View {
        VStack{
            DailyWeather(DailyWeatherViewModel: dailyWeatherViewModel.init())
        }
    }
}
