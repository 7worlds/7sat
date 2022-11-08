package fhnw.ws6c.sevensat.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


// Set of Material typography styles to start with
val typography = Typography(
        h1 = TextStyle(
                fontSize      = 20.sp,
                fontWeight    = FontWeight.Bold,
                lineHeight    = 23.sp,
        ),
        h2 = TextStyle(
                fontSize      = 14.sp,
                fontWeight    = FontWeight.SemiBold,
                lineHeight    = 18.sp,
        ),
        h3 = TextStyle(
                fontSize      = 12.sp,
                fontWeight    = FontWeight.Black,
                lineHeight    = 14.sp
        ),

        body1 = TextStyle(
                fontSize      = 12.sp,
                fontWeight    = FontWeight.Normal,
                lineHeight    = 13.sp,

        ),

)