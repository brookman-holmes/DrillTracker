package com.brookmanholmes.drilltracker.presentation.model

/**
 * Created by Brookman Holmes on 8/9/2017.
 */
data class DrillPackModel(
    val id: String,
    val name: String,
    var price: String,
    val description: String,
    val sku: String,
    val url: String?,
    var purchased: Boolean,
    var token: String
) : Model {
    override fun getModelId(): String {
        return id
    }
}