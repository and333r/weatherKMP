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
    
    private var weatherBL: weatherBL
    
    init(weatherBL: weatherBL) {
        self.weatherBL = weatherBL
      }
    
    func getAllData(latitude: Double, longitude: Double) {
        var weekW = weatherBL.getAllData(latitude, longitude)
        var dayW = weatherBL.getDailyWeather(weekW)
                
        // Obtener la fecha actual
        let ahora = Date()
        // Crear un objeto de calendario
        let calendario = Calendar.current
        // Obtener las horas de la fecha actual
        let currentHour = calendario.component(.hour, from: ahora)
        
        var actualWeather = weatherBL.getActualTemperature(dayW, currentHour+1)
        
        var dayseven = weatherBL.getSpecificWeekDayTemperature(weekW, 6)
        var aux = actualWeather.temperature.roundToInt().toString()
        actualT = aux + "º"
        actualC = actualWeather.code.toString()
        aux = actualWeather.humidity.toString()
        actualH = "Humedad: $aux%"
        aux = actualWeather.relativeT.roundToInt().toString()
        actualRT = "Sensación térmica: $aux" + "º"
        aux = actualWeather.precipitation.toString()
        actualP = "Precipitaciones: $aux%"
        estado = weatherBL.returnEstado(Int(actualC))
        
        print(actualT)
        print(actualC)
        print(actualH)
        print(actualRT)
        print(actualP)
        print(estado)        
        
        }

}
