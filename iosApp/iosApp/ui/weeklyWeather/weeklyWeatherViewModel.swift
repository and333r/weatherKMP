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
    @Published var maxMin: [((String?, String, String), Int)] = [
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
    
    func getAllData() async {
        print(latitude)
        print(longitude)
        print("Estoy en getAllData en iOS")
        do{
            
            
            
            let weekW = try await WeatherBL.getAllData(latitude: latitude, longitude: longitude)
            
            var lista: [((String?,String, String), Int)] = []
                
            for i in 1...7{
                var currentDay = WeatherBL.getSpecificWeekDayTemperature(weekWeather: weekW, dayNumber: Int32(i))
                var currentMaxMin = getMaxAndMinT(dayWeather: currentDay)
                var mostRepeatedCode = getAverageCode(dayWeather: currentDay)
                var weekDaySpanish = getDayOfWeek(afterDays: i-1)?.capitalized
                lista.append(((weekDaySpanish, currentMaxMin.0, currentMaxMin.1), mostRepeatedCode))
            }
            print(lista)
            maxMin = lista
            
        }
        catch {
                print(error)
            }
        
        }
    
    func setLatAndLong(Latitude: Double, Longitude: Double){
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

