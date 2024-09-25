package com.example.fasterfood.model

/*
 *  Name of file: Rating
 *  Author:  Nadish Maredia
 *  Description: This model class is used to fetch rating data from firebase
 * */
class Rating(
    var ratingNumber: Int? = 0,
    var totalNumberOfUsersRated: Int? = 0,
    var userId: List<String>? = null
)