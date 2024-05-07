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
    var max: String
    var min: String
    var weekDay: String
    var body: some View {
        HStack(alignment: .center, spacing: 25){
                Text(weekDay)
                .font(.title3)
                .fontWeight(.bold)
                .padding(.leading)
                Image(systemName: "sun.max")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 40, height: 40)
            HStack{
                Text("max.:" + max + "º")
                    .font(.footnote)
                Text("min.:" + min + "º")
                    .font(.footnote)
            }
            .padding(.trailing)
                
        }.frame(width: 360, height: 80).background(.white.gradient).cornerRadius(16)
        
    }
}

#Preview {
    weeklycardConstructor(code: 1, max: "18", min: "12", weekDay: "Monday")
}
