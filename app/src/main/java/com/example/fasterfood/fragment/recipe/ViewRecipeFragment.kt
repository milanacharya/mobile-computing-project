package com.example.fasterfood.fragment.recipe

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.fasterfood.HomeActivity
import com.example.fasterfood.R
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File

/**
 *  Name of file: ViewRecipeFragment.kt
 *  Author:  Acharya, Milan Ganesh
 *  Description: This fragment class is responsible for displaying single recipe details in my recipe view
 */
class ViewRecipeFragment : Fragment() {
    private lateinit var databaseRef: DatabaseReference
    private lateinit var stepMap: HashMap<String, String>
    private var ratingNumber: String = ""

    private lateinit var recipeImage: ImageView
    private lateinit var recipeName: TextView
    private lateinit var recipeIngredients: TextView
    private lateinit var recipeDescription: TextView
    private lateinit var recipeInstructions: TextView
    private lateinit var recipeRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_viewrecipe, container, false)
        val btnDelete = view.findViewById<Button>(R.id.recipe_delete)
        val btnEdit = view.findViewById<Button>(R.id.btn_edit_recipe)
        getRecipeDetails(view)

        // Handle the submit button click
        btnDelete.setOnClickListener {
            // Check if user is logged in
            if (isUserLoggedIn()) {
                dialog(view.context)
            } else {
                Toast.makeText(
                    view.context,
                    "You must login to delete the recipe",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Handle the edit button click
        btnEdit.setOnClickListener {
            // Check if user is logged in
            if (isUserLoggedIn()) {
                val fragment: Fragment = EditRecipeFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

                // Navigate to edit recipe fragment
                fragmentTransaction.replace(R.id.nav_host_fragment_content_home, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            } else {
                Toast.makeText(
                    view.context,
                    "You must login to delete the recipe",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return view
    }

    /*
     Function to delete recipe
     */
    private fun deleteRecipe() {
        // Get Recipe Id from shared preferences
        val prefs = this.activity?.getSharedPreferences(
            "Recipe", Context.MODE_PRIVATE
        )
        val recipeId = prefs!!.getString("recipeId", "")

        // Update the field in firebase
        databaseRef.child(recipeId!!).child("deleted").setValue(true)
    }

    /*
     Function to retrieve recipe details
     */
    private fun getRecipeDetails(view: View) {
        recipeImage = view.findViewById(R.id.view_recipe)
        recipeName = view.findViewById(R.id.view_recipe_name)
        recipeIngredients = view.findViewById(R.id.ingredients_body)
        recipeDescription = view.findViewById(R.id.description_body)
        recipeInstructions = view.findViewById(R.id.instructions_body)

        stepMap = hashMapOf()   // Hashmap of steps in a recipe

        // Obtain recipe id from shared preferences
        val prefs: SharedPreferences? = this.activity?.getSharedPreferences(
            "Recipe", Context.MODE_PRIVATE
        )
        val recipeId = prefs!!.getString("recipeId", "")

        // Extract information from the recipe reference
        databaseRef = FirebaseDatabase.getInstance().getReference("Recipes")
        recipeRef = databaseRef.child(recipeId!!)
        recipeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val imageId = snapshot.child("imageId").value.toString()
                val recipeText = snapshot.child("name").value.toString()
                val ingredientsText = snapshot.child("ingredients").value.toString()
                val descriptionText = snapshot.child("description").value.toString()
                ratingNumber = snapshot.child("ratings").child("ratingNumber").value.toString()
                for (stepSnapshot in snapshot.child("steps").children) {
                    stepMap.put(stepSnapshot.key.toString(), stepSnapshot.value.toString())
                }

                // Convert the steps string to an ordered list
                var instructionText = ""
                var i = 0
                while (i < stepMap.size) {
                    val step = stepMap.get(i.toString())
                    i++
                    instructionText = "$instructionText$i. $step\n"
                }

                // Set the image in the view
                val storageRef = FirebaseStorage.getInstance().reference.child("images/ $imageId")
                val localFile = File.createTempFile("tempImage", "jpeg")
                storageRef.getFile(localFile).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    recipeImage.setImageBitmap(bitmap)
                }.addOnFailureListener {
                    Log.e("getRecipeDetails", "Could not connect to Firebase")
                }

                // Set the respective details in the view
                recipeName.text = recipeText
                recipeIngredients.text = ingredientsText
                recipeDescription.text = descriptionText
                recipeInstructions.text = instructionText.dropLast(1) // Remove newline character at end
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("getRecipeDetails", "Firebase real time database error")
            }
        })
    }

    /*
     Function to check if user is logged in
     */
    private fun isUserLoggedIn(): Boolean {
        val prefs: SharedPreferences? = this.activity?.getSharedPreferences(
            "Auth", Context.MODE_PRIVATE
        )
        val userId = prefs!!.getString("UserId", "")

        if (userId.toString() != "") return true

        return false
    }

    /*
     This function will display a prompt and will ask user are you sure to update the profile
     */
    private fun dialog(view: Context) {
        val myRecipeBuilder = AlertDialog.Builder(view)
        myRecipeBuilder.setTitle("Recipe Delete")
        myRecipeBuilder.setMessage("Are you sure you want to delete the recipe?")

        // Handle Yes button click
        myRecipeBuilder.setPositiveButton("Yes") { _, _ ->
            // Delete the recipe
            deleteRecipe()

            Toast.makeText(
                view,
                "Recipe Deleted", Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(view.applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }

        // Handle No button click
        myRecipeBuilder.setNegativeButton("No") { _, _ ->
            Toast.makeText(
                view,
                "Delete cancelled", Toast.LENGTH_SHORT
            ).show()
        }

        myRecipeBuilder.show()
    }
}