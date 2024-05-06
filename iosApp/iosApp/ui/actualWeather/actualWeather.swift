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
    @ObservedObject var ActualWeatherViewModel: actualWeatherViewModel
    var body: some View {
        VStack{
            HStack(alignment: .top){
                VStack(){
                    Text("Tª en tu ubicación:")
                        .multilineTextAlignment(.center).padding(.top, 40).padding(.leading, 6).padding(.bottom, 0).font(.system(size: 16))
                    Text(ActualWeatherViewModel.actualT)
                        .fontWeight(.bold)
                        .multilineTextAlignment(.center).font(.system(size: 72))
                    Text(ActualWeatherViewModel.estado)
                        .fontWeight(.bold)
                        .multilineTextAlignment(.center).font(.system(size: 32))
                }
                VStack{
                    Image(systemName: "sun.max")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 150, height: 150)
                        .padding(.bottom, 20)
                    VStack(alignment: .leading){
                        Text(ActualWeatherViewModel.actualH).font(.system(size: 13))
                        Text(ActualWeatherViewModel.actualRT).font(.system(size: 13))
                        Text(ActualWeatherViewModel.actualP).font(.system(size: 13))
                    }
                }.padding(.leading, 22)
            }.padding(.top , 100)
                .task{
                    await ActualWeatherViewModel.getAllData()
                }
        }.offset(CGSize(width: 0.0, height: -80.0))
    }
}


struct actualWeather_previewer: PreviewProvider {
    static var previews: some View {
        VStack{
            ActualWeather(ActualWeatherViewModel: actualWeatherViewModel.init())
        }
    }
}
