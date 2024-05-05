//
//  weeklyCardConstructor.swift
//  iosApp
//
//  Created by Ander Caro on 5/5/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct weeklycardConstructor: View {
    var code: Int
    var max: Int
    var min: Int
    var weekDay: String
    var body: some View {
        HStack(alignment: .center, spacing: 30){
                Text(weekDay)
                .font(.title3)
                .fontWeight(.bold)
                .padding(.leading)
                Image(systemName: "sun.max")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 40, height: 40)
            HStack{
                Text("max.:" + String(max) + "º")
                    .font(.body)
                Text("min.:" + String(min) + "º")
                    .font(.body)
            }
            .padding(.trailing)
                
        }.frame(width: 360, height: 80).background(.white.gradient).cornerRadius(16)
        
    }
}

#Preview {
    weeklycardConstructor(code: 1, max: 18, min: 12, weekDay: "Monday")
}
