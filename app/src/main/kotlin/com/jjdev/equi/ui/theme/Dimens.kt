package com.jjdev.equi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
class Dimens(
    /** 0dp */
    val none: Dp = 0.dp,
    /** 4dp */
    val extraSmall: Dp = 4.dp,
    /** 8dp */
    val small: Dp = 8.dp,
    /** 16dp */
    val medium: Dp = 16.dp,
    /** 24dp */
    val large: Dp = 24.dp,
    /** 32dp */
    val extraLarge: Dp = 32.dp,
    /** 40dp */
    val extraExtraLarge: Dp = 40.dp,
    /** 48dp */
    val tripleExtraLarge: Dp = 48.dp,

    /** 12dp */
    val cornerRadius: Dp = 12.dp,
)

val MaterialTheme.dimens: Dimens
    get() = Dimens()