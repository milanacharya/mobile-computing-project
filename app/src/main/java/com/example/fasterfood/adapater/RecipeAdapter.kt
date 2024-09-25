package com.example.fasterfood.adapater

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fasterfood.R
import com.example.fasterfood.model.Recipes
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import androidx.navigation.Navigation.findNavController

/*
 *  Name of file: RecipeAdapter.kt
 *  Author:  Wenliang Jia
 *  Purpose: This is an adapter class to hold each recipe's view
 *            and to setup the navigation between screens
 *  Description: This adapter class will be used to hold each
 *                recipe's name, description and image.
 *               The navigation is set up to navigate between screens
 * */
class RecipeAdapter(private val recipeList: ArrayList<Recipes>, val context: Context) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    //onCreateViewHolder - return the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_all_recipes, parent, false)
        return RecipeViewHolder(itemView)
    }

    //onBindViewHolder - handle recipe's name, description and image, also setup navigation
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentItem = recipeList[position]
        holder.recipeName.text = currentItem.Name
        holder.recipeDescription.text = currentItem.Description
        val storageRef =
            FirebaseStorage.getInstance().reference.child("images/ " + currentItem.ImageId)

        val localFile = File.createTempFile("tempImage", "jpeg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.recipeImage.setImageBitmap(bitmap)
        }.addOnFailureListener {
            Log.e("onBindViewHolder", "Error while obtaining data from Firebase")
        }

        //navigation
        holder.itemView.setOnClickListener { v ->
            val prefs: SharedPreferences = context.getSharedPreferences(
                "Recipe", Context.MODE_PRIVATE
            )
            prefs.edit().putString("recipeId", currentItem.recipeId.toString()).apply()
            Log.e("ADPA", currentItem.recipeId.toString())

            findNavController(v).navigate(R.id.nav_view_recipe)
        }
    }

    //getItemCount() - return the size of recipeList
    override fun getItemCount(): Int {
        return recipeList.size
    }

    //RecipeViewHolder - ViewHolder of recipe's name, description and image
    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeName: TextView = itemView.findViewById(R.id.recipe_name)
        val recipeDescription: TextView = itemView.findViewById(R.id.recipe_description)
        val recipeImage: ImageView = itemView.findViewById(R.id.recipe_image)
    }
}