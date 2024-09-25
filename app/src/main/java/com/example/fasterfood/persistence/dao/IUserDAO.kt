package com.example.fasterfood.persistence.dao

import androidx.fragment.app.FragmentActivity

/*
 *  Name of file: IUserDAO
 *  Author: Nadish Maredia
 *  Description: This interface hold all methods that are linked with user profile like updateprofile,
 *               and service class will implement these methods
 * */
interface IUserDAO {
    fun updateUserProfile(
        fname: String,
        lname: String,
        email: String,
        isProfessional: Boolean,
        activity: FragmentActivity
    )
}