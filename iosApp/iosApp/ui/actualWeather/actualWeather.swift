//
//  actualWeather.swift
//  iosApp
//
//  Created by Ander Caro on 5/5/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import Shared

struct ActualWeather: View {
    var latitude: Double
    var longitude: Double
    
    @ObservedObject var ActualWeatherViewModel = actualWeatherViewModel()
    var body: some View {
        VStack{
            HStack(alignment: .top){
                VStack(){
                    Text("Ta en tu ubicación:")
                        .multilineTextAlignment(.center).padding(.top, 40).padding(.leading, 6).padding(.bottom, 0).font(.system(size: 16))
                    Text(ActualWeatherViewModel.actualT)
                        .fontWeight(.bold)
                        .multilineTextAlignment(.center).font(.system(size: 72))
                }
                VStack{
                    Image(systemName: "sun.max")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 150, height: 150)
                        .padding(.bottom, 20)
                    VStack{
                        Text("Humedad: \(ActualWeatherViewModel.actualH)%").font(.system(size: 16))
                        Text("Sensación termica: 12º").font(.system(size: 16))
                        Text("Precipitaciones: 0%").font(.system(size: 16))
                    }
                }.padding(.leading, 22)
            }.padding(.top , 100).task{
                do{
                    await ActualWeatherViewModel.getAllData(latitude: latitude, longitude: longitude)
                }
            }
        }.offset(CGSize(width: 0.0, height: -80.0))
    }
}


struct actualWeather_previewer: PreviewProvider {
    static var previews: some View {
        VStack{
            ActualWeather(latitude: 0.0, longitude: 0.0)
        }
    }
}
