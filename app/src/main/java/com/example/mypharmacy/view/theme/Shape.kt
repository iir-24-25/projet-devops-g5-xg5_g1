package com.example.mypharmacy.view.theme


import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val PharmacyShapes = Shapes(
    // Small shapes for small components like chips, buttons
    small = RoundedCornerShape(4.dp),

    // Medium shapes for cards
    medium = RoundedCornerShape(8.dp),

    // Large shapes for large components like modal sheets
    large = RoundedCornerShape(12.dp)
)