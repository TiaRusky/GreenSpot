package com.example.greenspot.presentation.spotter


//This class collects the informations about the reports made by the logged spotter
data class SpotterReportsData (
    val userId : String = "",
    val reportsMade : Int = 0,
    val resolvedReports : Int = 0,
)

