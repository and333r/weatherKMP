//
//  cardConstructor.swift
//  iosApp
//
//  Created by Ander Caro on 5/5/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct cardConstructor: View {
    var code: Int
    var temperature: Int
    var hour: String
    var body: some View {
        VStack{
            Text(hour + ":00")
                .font(.footnote)
            Image(systemName: "sun.max")
                .resizable()
                .scaledToFit()
                .frame(width: 30, height: 30)
            Text(String(temperature) + "º")
                .font(.body)
                .fontWeight(.bold)
        }.frame(width: 90, height: 90).background(.white.gradient).cornerRadius(16)
    }
}

#Preview {
    cardConstructor(code: 1, temperature: 12, hour: "16")
}
