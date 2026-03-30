package com.tuananh.traceart.presentation.screen.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuananh.traceart.presentation.navigation.LocalNavController
import com.tuananh.traceart.presentation.navigation.Routes

@Composable
fun ResultScreen(score: Int) {
    val navController = LocalNavController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFF8F9FA), Color(0xFFE9ECEF))
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFFFFAE00),
            modifier = Modifier.size(100.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Well Done!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF1B1B1B)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Your accuracy score is",
            fontSize = 16.sp,
            color = Color.Gray
        )
        
        Text(
            text = "$score%",
            fontSize = 64.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF6C63FF)
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.HOME) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF))
        ) {
            Icon(Icons.Default.Home, contentDescription = null)
            Spacer(modifier = Modifier.size(8.dp))
            Text("Back to Home", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
