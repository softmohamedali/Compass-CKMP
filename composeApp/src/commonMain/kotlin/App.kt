import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import shared.utils.CompassSensorManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.component.get

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(
            CompassScreen()
        )

    }
}