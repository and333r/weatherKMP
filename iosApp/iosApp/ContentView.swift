import SwiftUI
//import Shared


struct ContentView: View {
    @StateObject private var locationManager = LocationManager()
    private var actualWVM = actualWeatherViewModel()
    private var dailyWVM = dailyWeatherViewModel()

    var body: some View {
        VStack {
            ActualWeather(ActualWeatherViewModel: actualWVM)
            DailyWeather(DailyWeatherViewModel: dailyWVM).offset(CGSize(width: 0.0, height: -70.0))
            weeklyWeather().offset(CGSize(width: 0.0, height: -70.0)).padding(.bottom, -100)
        }.onAppear{
            locationManager.checkLocationAuthorization()
            let coordinate = locationManager.lastKnownLocation
            actualWVM.setLatAndLong(Latitude: coordinate!.latitude, Longitude: coordinate!.longitude)
            dailyWVM.setLatAndLong(Latitude: coordinate!.latitude, Longitude: coordinate!.longitude)

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

