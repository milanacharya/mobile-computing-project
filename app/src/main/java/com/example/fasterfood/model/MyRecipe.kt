package com.example.fasterfood.model

/*
 *  Name of file: MyRecipe
 *  Author:  Nadish Maredia
 *  Description: This model class is used to fetch data from firebase and display that data for edit
 *               recipe screen. It is a simple model class for recipes with some attributes
 * */
data class MyRecipe(
    var recipeId: String? = null,
    var Name: String? = null,
    var Description: String? = null,
    var ImageId: String? = null,
    var userId: String? = null,
    var deleted: Boolean? = false
)
