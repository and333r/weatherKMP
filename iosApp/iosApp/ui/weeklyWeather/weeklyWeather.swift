//
//  weeklyWeather.swift
//  iosApp
//
//  Created by Ander Caro on 5/5/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct weeklyWeather: View {
    var body: some View {
        VStack(alignment: .leading){
            Text("Predicción (7 días):")
            ScrollView(.vertical, showsIndicators: false) {
                VStack(alignment: .leading, spacing: 10) {
                    ForEach(0..<7) {_ in
                        weeklycardConstructor(code: 1, max: 18, min: 12, weekDay: "Monday")
                    }
                }.frame(height: .infinity)
            }.frame(height: .infinity)
        }.frame(height: .infinity)
        
    }
}

#Preview {
    weeklyWeather()
}
