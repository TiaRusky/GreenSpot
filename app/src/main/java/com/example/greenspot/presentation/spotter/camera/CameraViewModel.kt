package com.example.greenspot.presentation.spotter.camera

import androidx.lifecycle.ViewModel
import com.example.greenspot.presentation.spotter.camera.CameraState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class CameraViewModel() : ViewModel() {
    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()
}