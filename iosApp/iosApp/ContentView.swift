import SwiftUI
//import Shared


struct ContentView: View {
    @StateObject private var locationManager = LocationManager()

    var body: some View {
        VStack {
            if let coordinate = locationManager.lastKnownLocation {
                ActualWeather(latitude: coordinate.latitude, longitude: coordinate.longitude)
                DailyWeather().offset(CGSize(width: 0.0, height: -70.0))
                weeklyWeather().offset(CGSize(width: 0.0, height: -70.0)).padding(.bottom, -100)
            } else {
                Text("Unknown Location")
            }

        }.onAppear{
            locationManager.checkLocationAuthorization()
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

