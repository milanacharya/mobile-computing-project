package com.example.fasterfood.model

/*
 *  Name of file: CreateRecipes
 *  Author:  Siyuan, Zhu
 *  Description: This model class is used for getting and sending data to firebase. It is a simple
 *               model class for create recipes with some attributes
 * */
class CreateRecipes(
    var userId: String,
    var Name: String? = null,
    var Description: String? = null,
    var Ingredients: String? = null,
    var imageId: String,
    var isDeleted: Boolean,
    var steps: List<String>,
    var ratings: Rating? = null
)