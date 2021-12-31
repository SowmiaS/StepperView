package com.ss.stepperview.view

import androidx.compose.runtime.Composable

@Composable
fun Step(
    content: @Composable () -> Unit
) {
    content()
}