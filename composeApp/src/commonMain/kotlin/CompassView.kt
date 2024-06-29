import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import compass.composeapp.generated.resources.Res
import compass.composeapp.generated.resources.compose_multiplatform
import compass.composeapp.generated.resources.qibla
import org.jetbrains.compose.resources.painterResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import shared.utils.CompassSensorManager


@Composable
fun CompassView(
    azimuth:Float,
    qiblaDirection:Double,
) {



    Scaffold(

    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                    Canvas(modifier = Modifier.size(100.dp)) {
                        drawCircle(
                            color = Color.Red,
                            radius = 20f
                        )
                    }

                Spacer(modifier = Modifier.height(40.dp))
                Icon(
                    painter = painterResource(Res.drawable.qibla),
                    contentDescription = "Compass Needle",
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .size(300.dp)
                        .rotate(((qiblaDirection - azimuth)).toFloat())
                )
            }
        }
    }
}



class CompassScreen(
) : Screen ,KoinComponent{

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val compassSensorManager: CompassSensorManager by inject()
        val azimuth by compassSensorManager.azimuth().collectAsState(initial = 0f)
        val qiblaDirection by compassSensorManager.qiblaDirection().collectAsState(initial = 0.0)
        LaunchedEffect(true){
            compassSensorManager.start()
        }

        CompassView(
            azimuth=azimuth,
            qiblaDirection=qiblaDirection,
        )


    }


}