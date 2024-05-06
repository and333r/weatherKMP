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
    @Published var actualT = ""
    @Published var actualC = ""
    @Published var actualH = ""
    @Published var actualRT = ""
    @Published var actualP = ""
    @Published var estado = ""
    @Published var gradientColorList = Color.blue.gradient
    
    @Published var latitude = ""
    @Published var longitude = ""
    
    private var WeatherBL: weatherBL = weatherBL()

    
    func getAllData(latitude: Double, longitude: Double) async {
        print(latitude)
        print(longitude)
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

}
