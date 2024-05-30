import SwiftUI
//import Shared


struct ContentView: View {
    @StateObject private var locationManager = LocationManager()
    private var actualWVM = actualWeatherViewModel()
    private var dailyWVM = dailyWeatherViewModel()
    private var weeklyWVM = weeklyWeatherViewModel()
    @State private var isLoading = true

    var body: some View {
        Group {
            if isLoading {
                ProgressView("Loading...")
            } else {
                VStack {
                    ActualWeather(ActualWeatherViewModel: actualWVM)
                    DailyWeather(DailyWeatherViewModel: dailyWVM).offset(CGSize(width: 0.0, height: -70.0))
                    weeklyWeather(WeeklyWeatherViewModel: weeklyWVM).offset(CGSize(width: 0.0, height: -70.0)).padding(.bottom, -100)
                }
            }
        }
       .onAppear{
            locationManager.checkLocationAuthorization()
        }.onReceive(locationManager.$lastKnownLocation) { coordinate in
            guard let coordinate = coordinate else { return }
            actualWVM.setLatAndLong(Latitude: coordinate.latitude, Longitude: coordinate.longitude)
            dailyWVM.setLatAndLong(Latitude: coordinate.latitude, Longitude: coordinate.longitude)
            weeklyWVM.setLatAndLong(Latitude: coordinate.latitude, Longitude: coordinate.longitude)
            isLoading = false
        }
        .padding()
        .background(.blue.gradient)
    }
}


struct previewer: PreviewProvider {
    static var previews: some View {
        VStack{
            ContentView()
        }
    }
}

