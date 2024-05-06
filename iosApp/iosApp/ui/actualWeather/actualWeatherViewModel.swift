//
//  actualWeatherViewModel.swift
//  iosApp
//
//  Created by Ander Caro on 5/5/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import Shared
import SwiftUI

class actualWeatherViewModel: ObservableObject{
    @Published var actualT = "14"
    @Published var actualC = "2"
    @Published var actualH = "65"
    @Published var actualRT = "12"
    @Published var actualP = "0"
    @Published var estado = "Soleado"
    @Published var gradientColorList = Color.blue.gradient
    
    @Published var latitude = 0.0
    @Published var longitude = 0.0
    
    private var WeatherBL: weatherBL = weatherBL()

    
    func getAllData() async {
        print(latitude)
        print(longitude)
        print("Estoy en getAllData en iOS")
        do{
            let weekW = try await WeatherBL.getAllData(latitude: latitude, longitude: longitude)
            let dayW = WeatherBL.getDailyWeather(weekWeather: weekW)
            // Obtener la fecha actual
            let ahora = Date()
            // Crear un objeto de calendario
            let calendario = Calendar.current
            // Obtener las horas de la fecha actual
            let currentHour = calendario.component(.hour, from: ahora)
            
            let actualWeather = WeatherBL.getActualTemperature(dayWeather: dayW, hour: Int32(currentHour+1))
            
            var dayseven = WeatherBL.getSpecificWeekDayTemperature(weekWeather: weekW, dayNumber: 6)
            var aux = String(round(actualWeather.temperature))
            actualT = aux + "º"
            actualC = String(actualWeather.code)
            aux = String(actualWeather.humidity)
            actualH = "Humedad: \(aux)%"
            aux = String(round(actualWeather.relativeT))
            actualRT = "Sensación térmica: \(aux)" + "º"
            aux = String(actualWeather.precipitation)
            actualP = "Precipitaciones: \(aux)%"
            estado = WeatherBL.returnEstado(code: Int32(actualC)!)
            
            print(actualT)
            print(actualC)
            print(actualH)
            print(actualRT)
            print(actualP)
            print(estado)
            
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
