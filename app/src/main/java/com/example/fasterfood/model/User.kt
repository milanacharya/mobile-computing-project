package com.example.fasterfood.model

/*
 *  Name of file: Steps
 *  Author:  Nadish Maredia
 *  Description: This model class is used to fetch user data from firebase
 * */
class User(
    var email: String,
    var password: String,
    var first_name: String,
    var last_name: String,
    var isProfessional: Boolean
)