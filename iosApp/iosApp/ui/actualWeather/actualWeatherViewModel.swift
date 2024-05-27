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

    var db = DatabaseDriverFactoryKt.createDatabase(driverFactory: DatabaseDriverFactory())
    var cambio = false
    var hasRun = false

    func getAllData() async {
        print(latitude)
        print(longitude)
        print("Estoy en getAllData en iOS")
        do{
            let ds_aw = actualWeatherDataSource(db: db)
            let repo_aw = actualWeatherRepositorySQL(dataSource: ds_aw)
            print("Una done")
            
            //try await repo_aw.insert(hour: 34, latitude: 0.0, longitude: 0.0, temperature: 23, humidity: 23, code: 23, relativeT: 0.0, precipitation: 23)
            try await repo_aw.getAlliOS().collect(collector: Collector<actualWeather>(){ value in
                print(value)
                        Task {
                            do {
                                guard !self.hasRun else { return } // Asegura que solo se ejecute una vez
                                self.hasRun = true
                                let ahora = Date()
                                // Crear un objeto de calendario
                                let calendario = Calendar.current
                                // Obtener las horas de la fecha actual
                                let currentHour = calendario.component(.hour, from: ahora)
                                let weekW = try await self.WeatherBL.getAllData(latitude: self.latitude, longitude: self.longitude)
                                
                                let dayW = self.WeatherBL.getDailyWeather(weekWeather: weekW)
                                
                                let actualWeather = self.WeatherBL.getActualTemperature(dayWeather: dayW, hour: Int32(currentHour+1))
                                
                                if !value.isEqual(nil) {
                                    print(value.latitude)
                                    print(value.longitude)
                                    print(self.longitude)
                                    print(self.latitude)
                                    if value.latitude == self.latitude || value.longitude == self.longitude {
                                        if currentHour != value.hour {
                                            self.cambio = true
                                        }else{
                                            var aux = String(Int(round(value.temperature)))
                                            self.actualT = aux + "º"
                                            self.actualC = String(value.code)
                                            aux = String(value.humidity)
                                            self.actualH = "Humedad: \(aux)%"
                                            aux = String(Int(round(value.relativeT)))
                                            self.actualRT = "Sensación térmica: \(aux)" + "º"
                                            aux = String(value.precipitation)
                                            self.actualP = "Precipitaciones: \(aux)%"
                                            self.estado = self.WeatherBL.returnEstado(code: Int32(self.actualC)!)
                                        }
                                    } else {
                                        self.cambio = true
                                    }
                                } else {
                                    self.cambio = true
                                }
                                if(self.cambio){
                                    self.cambio = false
                                    try await repo_aw.deleteAll()
                                    print(actualWeather.temperature)
                                    print(actualWeather.humidity)
                                    print(actualWeather.code)
                                    print(actualWeather.latitude)
                                    print(actualWeather.longitude)
                                    print(actualWeather.relativeT)
                                    try await repo_aw.insert(hour: Int64(currentHour), latitude: self.latitude, longitude: self.longitude, temperature: actualWeather.temperature, humidity: Int64(actualWeather.humidity), code: Int64(actualWeather.code), relativeT: actualWeather.relativeT, precipitation: Int64(actualWeather.relativeT))
                                }
                            } catch {
                                print("Error occurred: \(error)")
                            }
                        }
            })
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
