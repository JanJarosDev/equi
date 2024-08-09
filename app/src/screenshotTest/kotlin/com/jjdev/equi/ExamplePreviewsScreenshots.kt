import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jjdev.equi.core.ui.theme.EquiTheme

class ExamplePreviewsScreenshots {

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        EquiTheme {
            Text("Android!")
        }
    }
}