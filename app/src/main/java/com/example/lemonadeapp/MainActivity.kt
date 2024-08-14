package com.example.lemonadeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lemonadeapp.ui.theme.LemonadeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LemonadeAppTheme {
                LemonadeScreen()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    LemonadeAppTheme {
        LemonadeScreen()
    }
}

class LemonadeScreenDataProvider {
    enum class CookingStep {
        TREE,
        LEMON,
        FULL_GLASS,
        EMPTY_GLASS
    }

    var requiredTapsCount: Int = 1
        private set

    private var currentStep = CookingStep.TREE
        set(value) {
            field = value
            requiredTapsCount = if (value == CookingStep.LEMON) (1..5).random() else 1
        }

    val painterResourceId: Int
        get() = when (currentStep) {
            CookingStep.TREE -> R.drawable.lemon_tree
            CookingStep.LEMON -> R.drawable.lemon_squeeze
            CookingStep.FULL_GLASS -> R.drawable.lemon_drink
            CookingStep.EMPTY_GLASS -> R.drawable.lemon_restart
        }

    val textResourceId
        get() = when (currentStep) {
            CookingStep.TREE -> R.string.lemon_select
            CookingStep.LEMON -> R.string.lemon_squeeze
            CookingStep.FULL_GLASS -> R.string.lemon_drink
            CookingStep.EMPTY_GLASS -> R.string.lemon_empty_glass
        }

    fun nextStep() {
        currentStep = CookingStep.entries[(currentStep.ordinal + 1) % CookingStep.entries.size]
    }
}

@Composable
fun LemonadeStepView(dataProvider: LemonadeScreenDataProvider) {
    var tapsCount by remember { mutableIntStateOf(0) }

    if(tapsCount == dataProvider.requiredTapsCount) {
        dataProvider.nextStep()
        tapsCount = 0
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { tapsCount++ },
            shape = RoundedCornerShape(16),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.step_image_color)
            )
        ) {
            Image(
                painter = painterResource(id = dataProvider.painterResourceId),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = dataProvider.textResourceId),
            color = Color.DarkGray
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LemonadeScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lemonade",
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = colorResource(id = R.color.accent_color)
                )
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.tertiaryContainer),
            color = MaterialTheme.colorScheme.background
        ) {
            LemonadeStepView(dataProvider = LemonadeScreenDataProvider())
        }
    }
}

