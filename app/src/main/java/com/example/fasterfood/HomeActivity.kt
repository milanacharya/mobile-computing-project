package com.example.fasterfood

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.fasterfood.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

/*
 *  Name of file: HomeActivity
 *  Author:  Nadish Maredia
 *  Description: This is just the home screen of the application showing different menus
 * */
class HomeActivity : AppCompatActivity() {

    //declare all variables
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private lateinit var nav_drawer: NavigationView
    private lateinit var prefs: SharedPreferences
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //getting firebaseauth instance
        mAuth = FirebaseAuth.getInstance()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHome.toolbar)

        nav_drawer = findViewById(R.id.nav_view)

        // checking if user is logged in or not using shared preferences. If user is logged in show welcome msg
        if (isUserLoggedIn()) {

            var hview = nav_drawer.getHeaderView(0)
            var tt = hview.findViewById<TextView>(R.id.drawerheading)
            tt.setText("Welcome " + getUserNameFromSession())
        } else {
            var hview = nav_drawer.getHeaderView(0)
            var tt = hview.findViewById<TextView>(R.id.drawerheading)
            tt.setText("You are not log in")
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_viewAllRecipes, R.id.nav_create_recipe
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // signout button operation remove the sharedPreferences
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        // if user pressed signout button clear the sharedpreference
        if (id == R.id.action_signout) {
            mAuth.signOut()
            prefs = getSharedPreferences(
                "Auth", Context.MODE_PRIVATE
            )
            prefs.edit().remove("UserId").commit();

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            Toast.makeText(this, "Sign out", Toast.LENGTH_SHORT).show()

        }
        return super.onOptionsItemSelected(item)
    }

    // functions to check is user logged in to the system or not it will return true if user is logged in
    fun isUserLoggedIn(): Boolean {
        val prefs: SharedPreferences? = this.getSharedPreferences(
            "Auth", Context.MODE_PRIVATE
        )
        var id = prefs?.getString("UserId", "novalue")
        if (id != "novalue") return true

        return false
    }

    // fun which will return logged in user name
    fun getUserNameFromSession(): String {
        val prefs: SharedPreferences? = this.getSharedPreferences(
            "Auth", Context.MODE_PRIVATE
        )
        var name = prefs?.getString("UserName", "novalue")
        if (name != "novalue") return name.toString()

        return ""
    }
}