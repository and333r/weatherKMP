//
//  weeklyWeatherViewModel.swift
//  iosApp
//
//  Created by Ander Caro on 6/5/24.
//  Copyright © 2024 orgName. All rights reserved.
//
import Foundation
import Shared
import SwiftUI

class weeklyWeatherViewModel: ObservableObject{
    @Published var maxMin: [((String?, String?, String?), Int?)] = [
        (("Sábado", "14", "9.3"), 0),
        (("Domingo", "14", "9.3"), 0),
        (("Lunes", "14", "9.3"), 0),
        (("Martes", "14", "9.3"), 0),
        (("Miercoles", "14", "9.3"), 0),
        (("Jueves", "14", "9.3"), 0),
        (("Viernes", "14", "9.3"), 0)]
    
    @Published var latitude: Double = 0.0
    @Published var longitude:Double = 0.0
    
    private var WeatherBL: weatherBL = weatherBL()
    
    
    var db = DatabaseDriverFactoryKt.createDatabase(driverFactory: DatabaseDriverFactory())
    var cambio = false
    var hasRun = false
    var change = false
    
    func getAllData() async {
        print(latitude)
        print(longitude)
        print("Estoy en getAllData en iOS")
        do{
            let ds_ww = weeklyWeatherDataSource(db: self.db)
            let repo_ww = weeklyWeatherRepositorySQL(dataSource: ds_ww)
            
            try await repo_ww.deleteAll()
            
            try await repo_ww.getAlliOS().collect(collector: Collector<weeklyWeatherList?>{ value in
                Task{
                    do{
                        guard !self.hasRun else { return } // Asegura que solo se ejecute una vez
                        if(self.change){
                            self.hasRun = true
                        }
                        let weekW = try await self.WeatherBL.getAllData(latitude: self.latitude, longitude: self.longitude)
                        var lista: [((String?,String, String), Int)] = []
                        for i in 1...7{
                            let currentDay = self.WeatherBL.getSpecificWeekDayTemperature(weekWeather: weekW, dayNumber: Int32(i))
                            let currentMaxMin = self.getMaxAndMinT(dayWeather: currentDay)
                            let mostRepeatedCode = self.getAverageCode(dayWeather: currentDay)
                            let weekDaySpanish = self.getDayOfWeek(afterDays: i-1)?.capitalized
                            lista.append(((weekDaySpanish, currentMaxMin.0, currentMaxMin.1), mostRepeatedCode))
                        }
                        self.maxMin = lista
                        if(!value!.wekklyList.isEmpty){
                            if(value!.wekklyList.last?.latitude == self.latitude && value!.wekklyList.last?.longitude == self.longitude){
                                
                                if(value!.wekklyList.first?.date != lista.first?.0.0){
                                    self.cambio = true
                                }
                                else{
                                    print("weeklyWeather: VM -> BD -> VM")

                                    var listar: [((String?,String?, String?), Int?)] = []
                                    for i in 1...7{
                                        listar.append(((value!.wekklyList[i-1].date,
                                                        String(value!.wekklyList[i-1].temperatureMax),
                                                        String(value!.wekklyList[i-1].temperatureMin)),
                                                       Int(value!.wekklyList[i-1].code)))
                                    }
                                    self.maxMin = listar
                                }
                            }else{
                                self.cambio = true
                            }
                        }else{
                            self.cambio = true
                        }
                        if(self.cambio){
                            print("weeklyWeather: VM -> BD -> API -> BD -> VM")
                            try await repo_ww.deleteAll()
                            for i in 1...7{
                                try await repo_ww.insert(date: lista[i-1].0.0!, latitude: self.latitude, longitude: self.longitude, temperatureMax: Double(lista[i-1].0.1)!, temperatureMin: Double(lista[i-1].0.2)!, code: Int64(lista[i-1].1))
                            }
                            try await repo_ww.getAlliOS().collect(collector: Collector<weeklyWeatherList>{w in
                                var listar: [((String?,String?, String?), Int?)] = []
                                for i in 1...7{
                                    listar.append(((w.wekklyList[i-1].date,
                                                    String(w.wekklyList[i-1].temperatureMax),
                                                    String(w.wekklyList[i-1].temperatureMin)),
                                                   Int(w.wekklyList[i-1].code)))
                                }
                                self.maxMin = listar
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
        self.change = true
           latitude = Latitude
           longitude = Longitude
       }
    
    func getDayOfWeek(afterDays days: Int) -> String? {
        let calendar = Calendar(identifier: .gregorian)
        let currentDate = Date()
        
        if let futureDate = calendar.date(byAdding: .day, value: days, to: currentDate) {
            let dateFormatter = DateFormatter()
            dateFormatter.locale = Locale(identifier: "es_ES")
            dateFormatter.dateFormat = "EEEE"
            return dateFormatter.string(from: futureDate)
        } else {
            return nil
        }
    }
    
    func getMaxAndMinT(dayWeather: dayWeather) -> (String, String) {
        var max = -1.0
        var min = 1000.0
            for t in dayWeather.temperatures {
                if (Double(truncating: t) > max) {
                    max = Double(truncating: t)
                } else if (Double(truncating: t)<min){
                    min = Double(truncating: t)
                }
            }
            return (String(max), String(min))
    }

    func getAverageCode(dayWeather: dayWeather) -> Int {
        
        let countedSet = NSCountedSet(array: dayWeather.codes)
        let mostFrequent = countedSet.max { countedSet.count(for: $0) < countedSet.count(for: $1) }
        return mostFrequent as! Int
    }

}

