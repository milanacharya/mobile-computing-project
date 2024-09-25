package com.example.fasterfood.persistence.dao

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.DatabaseReference

/*
 *  Name of file: IRecipeDAO
 *  Author: Nadish Maredia
 *  Description: This interface hold the methods that are linked with recipe like editrecipe,
 *               and service classes will implement these methods
 * */
interface IRecipeDAO {
    fun editRecipe(
        databaseRef: DatabaseReference,
        name: String? = null,
        description: String? = null,
        ingredients: String? = null,
        steps: String? = null,
        factivity: FragmentActivity,
        view: View
    )
}