package fhnw.ws6c.sevensat.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val typography = Typography(
        h1 = TextStyle(
                fontSize      = 30.sp,
                fontWeight    = FontWeight.Bold,
                lineHeight    = 23.sp,
        ),
        h2 = TextStyle(
                fontSize      = 14.sp,
                fontWeight    = FontWeight.SemiBold,
                lineHeight    = 18.sp,
        ),
        h3 = TextStyle(
                fontSize      = 14.sp,
                fontWeight    = FontWeight.Bold,
                lineHeight    = 14.sp
        ),
        h6 = TextStyle(
                fontSize      = 16.sp,
                fontWeight    = FontWeight.Medium,
                fontStyle = FontStyle.Italic,
                lineHeight    = 14.sp,
                color = Color(0xFFFFFFFF)
        ),
        body1 = TextStyle(
                fontSize      = 12.sp,
                fontWeight    = FontWeight.Normal,
                lineHeight    = 13.sp,
        ),
)