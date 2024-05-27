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
            
            try await repo_dw.getAlliOS().collect(collector: Collector<hourlyWeatherList>{ value in
                Task{
                    do{
                        let weekW = try await self.WeatherBL.getAllData(latitude: self.latitude, longitude: self.longitude)
                        let dayW = self.WeatherBL.getDailyWeather(weekWeather: weekW)
                        self.cambio = false
                        guard !self.hasRun else { return } // Asegura que solo se ejecute una vez
                        if(self.changed){
                            self.hasRun = true
                        }
                        print(value.dailyList)
                        print(self.latitude)
                        print(self.longitude)
                        if(!value.isEqual(nil)){
                            if(value.dailyList.last?.longitude == self.longitude && value.dailyList.last?.latitude == self.latitude){
                                if(Int(value.dailyList.first!.date) != currentHour){
                                    self.cambio = true
                                }
                                else{
                                    var my_array = [String]()
                                    var my_temperatures = [Double]()
                                    var my_codes = [Int]()
                                    for i in range {
                                        my_array.append(String(i))
                                        my_temperatures.append(round(value.dailyList[i - currentHour].temperature))
                                        my_codes.append(Int(truncating: value.dailyList[i - currentHour].code as NSNumber))
                                    }
                                    self.temperatures = my_temperatures
                                    self.codes = my_codes
                                    self.hours = my_array
                                }
                                
                            }else{
                                self.cambio = true
                            }
                            
                        }else{
                            self.cambio = true
                        }
                        if(self.cambio){
                            try await repo_dw.deleteAll()
                            let list_aux1 = dayW.temperatures
                            let list_aux2 = dayW.codes
                            for i in range {
                                try await repo_dw.insert(date: String(i), latitude: self.latitude, longitude: self.longitude, temperature: Double(truncating: list_aux1[i]), code: Int64(truncating: list_aux2[i]))
                            }
                            
                        }
                    }catch{
                        print(error)
                    }
                }
                
                
            })
            
            

            print(temperatures)
            print(hours)
            print(codes)
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

