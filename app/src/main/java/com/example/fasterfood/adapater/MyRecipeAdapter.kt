package com.example.fasterfood.adapater

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.fasterfood.R
import com.example.fasterfood.model.MyRecipe
import com.google.firebase.storage.FirebaseStorage
import java.io.File

/*
 *  Name of file: MyRecipeAdapter
 *  Author:  Nadish Maredia
 *  Description: This adpater class is responsible for showing all the recipes that user have created
 *  This adapter is used for viewmyrecipe based on user id. It will only show those recipes which have
 *  been created by the user. User can edit or delete them
 * */
class MyRecipeAdapter(private val recipeList: ArrayList<MyRecipe>, val context: Context) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    // just attached the list item view to recycler view
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeAdapter.RecipeViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_all_recipes, parent, false)
        return RecipeAdapter.RecipeViewHolder(itemView)
    }

    // in this function we are binding all the control and also handle click event on particular recipes
    override fun onBindViewHolder(holder: RecipeAdapter.RecipeViewHolder, position: Int) {
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

        holder.itemView.setOnClickListener { v ->
            val prefs: SharedPreferences = context.getSharedPreferences(
                "Recipe", Context.MODE_PRIVATE
            )
            prefs.edit().putString("recipeId", currentItem.recipeId.toString()).apply()

            Navigation.findNavController(v).navigate(R.id.nav_view_my_recipe)
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
}