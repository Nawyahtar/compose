package com.example.composeapp.presentation.screen.testUi

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mukeshsolanki.OTP_VIEW_TYPE_BORDER
import com.mukeshsolanki.OtpView

@Composable
fun TestScreen() {
    val context = LocalContext.current
    val length = 6
    var otpValue by remember { mutableStateOf("") }

    LaunchedEffect(otpValue) {
        if (otpValue.length == length) {
            Toast.makeText(context, otpValue, Toast.LENGTH_SHORT).show()

        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Verify your number",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Enter the 6-digit code we sent to your phone.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        OtpView(
            otpText = otpValue,
            onOtpTextChange = { text ->
                otpValue = text.filter(Char::isDigit).take(length)
            },
            type = OTP_VIEW_TYPE_BORDER,
            password = false,
            otpCount = 6,
            passwordChar = "•",
            containerSize = 48.dp,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            charColor = MaterialTheme.colorScheme.onSurface
        )

    }
}
