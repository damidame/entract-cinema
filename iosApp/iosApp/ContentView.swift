import SwiftUI
import shared

struct ContentView: View {
    let utils = PlatformUtils()
    var body: some View {
        Text(utils.platformName())
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
