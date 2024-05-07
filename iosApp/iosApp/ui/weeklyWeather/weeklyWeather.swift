//
//  weeklyWeather.swift
//  iosApp
//
//  Created by Ander Caro on 5/5/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct weeklyWeather: View {
    @ObservedObject var WeeklyWeatherViewModel: weeklyWeatherViewModel
    var body: some View {
        VStack(alignment: .leading){
            Text("Predicción (7 días):")
            ScrollView(.vertical, showsIndicators: false) {
                VStack(alignment: .leading, spacing: 10) {
                    ForEach(0..<WeeklyWeatherViewModel.maxMin.count) {i in
                        weeklycardConstructor(code: WeeklyWeatherViewModel.maxMin[i].1, max: WeeklyWeatherViewModel.maxMin[i].0.1, min: WeeklyWeatherViewModel.maxMin[i].0.2, weekDay: WeeklyWeatherViewModel.maxMin[i].0.0!)
                    }
                }
            }
        }.task{
            await WeeklyWeatherViewModel.getAllData()
        }
        
    }
}

#Preview {
    weeklyWeather(WeeklyWeatherViewModel: weeklyWeatherViewModel.init())
}
