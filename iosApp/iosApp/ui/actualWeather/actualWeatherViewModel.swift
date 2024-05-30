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
        do{
            let ds_aw = actualWeatherDataSource(db: db)
            let repo_aw = actualWeatherRepositorySQL(dataSource: ds_aw)
            
            //try await repo_aw.deleteAll()
            
            try await repo_aw.getAlliOS().collect(collector: Collector<actualWeather?>(){ value in
                        Task {
                            do {
                                guard !self.hasRun else { return }
                                self.hasRun = true
                                let ahora = Date()
                                let calendario = Calendar.current
                                let currentHour = calendario.component(.hour, from: ahora)
                                let weekW = try await self.WeatherBL.getAllData(latitude: self.latitude, longitude: self.longitude)
                                let dayW = self.WeatherBL.getDailyWeather(weekWeather: weekW)
                                let actualWeather = self.WeatherBL.getActualTemperature(dayWeather: dayW, hour: Int32(currentHour))
                                if (value != nil) {
                                    if value!.latitude == self.latitude || value!.longitude == self.longitude {
                                        if currentHour != value!.hour {
                                            self.cambio = true
                                        }else{
                                            print("actualWeather: VM -> BD -> VM")
                                            var aux = String(Int(round(value!.temperature)))
                                            print(aux)
                                            self.actualT = aux + "º"
                                            self.actualC = String(value!.code)
                                            aux = String(value!.humidity)
                                            self.actualH = "Humedad: \(aux)%"
                                            aux = String(Int(round(value!.relativeT)))
                                            self.actualRT = "Sensación térmica: \(aux)" + "º"
                                            aux = String(value!.precipitation)
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
                                    print("actualWeather: VM -> BD -> API -> BD -> VM")
                                    self.cambio = false
                                    try await repo_aw.deleteAll()
                                    try await repo_aw.insert(hour: Int64(currentHour), latitude: self.latitude, longitude: self.longitude, temperature: actualWeather.temperature, humidity: Int64(actualWeather.humidity), code: Int64(actualWeather.code), relativeT: actualWeather.relativeT, precipitation: Int64(actualWeather.relativeT))
                                    try await repo_aw.getAlliOS().collect(collector: Collector<actualWeather?>{w in
                                        var aux = String(Int(round(w!.temperature)))
                                        self.actualT = aux + "º"
                                        self.actualC = String(w!.code)
                                        aux = String(w!.humidity)
                                        self.actualH = "Humedad: \(aux)%"
                                        aux = String(Int(round(w!.relativeT)))
                                        self.actualRT = "Sensación térmica: \(aux)" + "º"
                                        aux = String(w!.precipitation)
                                        self.actualP = "Precipitaciones: \(aux)%"
                                        self.estado = self.WeatherBL.returnEstado(code: Int32(self.actualC)!)
                                    })
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
