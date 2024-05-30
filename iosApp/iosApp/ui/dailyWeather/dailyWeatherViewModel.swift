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
    
    var db = DatabaseDriverFactoryKt.createDatabase(driverFactory: DatabaseDriverFactory())
    var cambio = false
    var hasRun = false
    var changed = false
    
    func getAllData() async {
        do{
            let ahora = Date()
            // Crear un objeto de calendario
            let calendario = Calendar.current
            // Obtener las horas de la fecha actual
            let currentHour = calendario.component(.hour, from: ahora)
            let range = currentHour...23
            let ds_dw = dailyWeatherDataSource(db: db)
            let repo_dw = dailyWeatherRepositorySQL(dataSource: ds_dw)
            
            //try await repo_dw.deleteAll()

            try await repo_dw.getAlliOS().collect(collector: Collector<hourlyWeatherList?>{ value in
                Task{
                    do{
                        let weekW = try await self.WeatherBL.getAllData(latitude: self.latitude, longitude: self.longitude)
                        let dayW = self.WeatherBL.getDailyWeather(weekWeather: weekW)
                        self.cambio = false
                        guard !self.hasRun else { return } // Asegura que solo se ejecute una vez (Esto es una solución temporal para evitar que se ejecute en varios hilos)
                        if(self.changed){
                            self.hasRun = true
                        }
                        if(!value!.dailyList.isEmpty){
                            if(value!.dailyList.last?.longitude == self.longitude && value!.dailyList.last?.latitude == self.latitude){
                                if(Int(value!.dailyList.first!.date) != currentHour){
                                    self.cambio = true
                                }
                                else{
                                    print("dailyWeathre: VM -> BD -> VM")
                                    var my_array = [String]()
                                    var my_temperatures = [Double]()
                                    var my_codes = [Int]()
                                    for i in range {
                                        my_array.append(String(i))
                                        my_temperatures.append(round(value!.dailyList[i - currentHour].temperature))
                                        my_codes.append(Int(truncating: value!.dailyList[i - currentHour].code as NSNumber))
                                    }
                                    self.codes = my_codes
                                    self.hours = my_array
                                    self.temperatures = my_temperatures

                                }
                                
                            }else{
                                self.cambio = true
                            }
                            
                        }else{
                            self.cambio = true
                        }
                        if(self.cambio){
                            print("dailyWeathre: VM -> BD -> API -> BD -> VM")
                            try await repo_dw.deleteAll()
                            let list_aux1 = dayW.temperatures
                            let list_aux2 = dayW.codes
                            for i in range {
                                try await repo_dw.insert(date: String(i), latitude: self.latitude, longitude: self.longitude, temperature: Double(truncating: list_aux1[i]), code: Int64(truncating: list_aux2[i]))
                            }
                            try await repo_dw.getAlliOS().collect(collector: Collector<hourlyWeatherList>{w in
                                var my_array = [String]()
                                var my_temperatures = [Double]()
                                var my_codes = [Int]()
                                for i in range {
                                    my_array.append(String(i))
                                    my_temperatures.append(round(w.dailyList[i - currentHour].temperature))
                                    my_codes.append(Int(truncating: w.dailyList[i - currentHour].code as NSNumber))
                                }
                                self.codes = my_codes
                                self.hours = my_array
                                self.temperatures = my_temperatures
                            })
                            
                        }
                    }catch{
                        print(error)
                    }
                }
                
                
            })
        }
        catch {
                print(error)
            }
        
        }
    
    func setLatAndLong(Latitude: Double, Longitude: Double){
        self.changed = true
           latitude = Latitude
           longitude = Longitude
       }

}

