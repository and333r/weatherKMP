//
//  dailyWeatherViewModel.swift
//  iosApp
//
//  Created by Ander Caro on 6/5/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import Shared
import SwiftUI

class dailyWeatherViewModel: ObservableObject{
    
    @Published var temperatures: [Double] = [1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 ,11.0, 12.0, 13.0]
    @Published var codes: [Int] = [1, 2, 3, 2, 3, 1, 3, 3, 3, 23, 1, 2, 3]
    @Published var hours: [String] = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"]
    
    @Published var latitude = 0.0
    @Published var longitude = 0.0
    
    private var WeatherBL: weatherBL = weatherBL()
    
    func getAllData() async {
        do{
            let weekW = try await WeatherBL.getAllData(latitude: latitude, longitude: longitude)
            let dayW = WeatherBL.getDailyWeather(weekWeather: weekW)
            // Obtener la fecha actual
            let ahora = Date()
            // Crear un objeto de calendario
            let calendario = Calendar.current
            // Obtener las horas de la fecha actual
            let currentHour = calendario.component(.hour, from: ahora)
            
            let range = currentHour...23
            
            var my_array = [String]()
            var my_temperatures = [Double]()
            var my_codes = [Int]()
            for i in range {
                my_array.append(String(i))
                my_temperatures.append(Double(truncating: dayW.temperatures[i]))
                my_codes.append(Int(truncating: dayW.codes[i]))
            }
            
            temperatures = my_temperatures
            codes = my_codes
            hours = my_array

            print(temperatures)
            print(hours)
            print(codes)
        }
        catch {
                print(error)
            }
        
        }
    
    func setLatAndLong(Latitude: Double, Longitude: Double){
           latitude = Latitude
           longitude = Longitude
       }

}

