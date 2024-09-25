package com.example.fasterfood.model

/*
 *  Name of file: Recipes.kt
 *  Author:  Wenliang Jia
 *  Purpose: This is a data class to get each recipe's info as attributes
 *  Description: This data class will be used to get each recipe's id,
 *                name, description, image and deletion status as attributes
 * */
data class Recipes(
    var deleted: Boolean? = false,
    var recipeId: String? = null,
    var Name: String? = null,
    var Description: String? = null,
    var ImageId: String? = null
)
