package com.marmarovas.animediary

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.marmarovas.animediary.databinding.ActivityMainBinding
import com.marmarovas.animediary.screens.animeslistpage.AnimesListPageFragment
import com.marmarovas.animediary.utils.OnFragmentChangedActionBarListener
import com.marmarovas.animediary.utils.TokenDataAccess

/**
 * MainActivity is only responsible for setting the content view that contains the
 * Navigation Host.
 */

class MainActivity : AppCompatActivity(), OnFragmentChangedActionBarListener {

    private var actionBar: ActionBar? = null

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainNavController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Get instance of binding class
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        mainNavController = findNavController(R.id.myNavHostFragment)

        //set action bar
        setSupportActionBar(binding.appActionBar)
        actionBar = supportActionBar
    }

    //Inflate the action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Handle action bar click action event
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_add_review -> {
            //go to add screen
            AnimesListPageFragment.getInstance().addAnimeReview()
            true
        }

        R.id.action_log_out -> {
            //delete current token
            TokenDataAccess.setTokenToNull(this)

            //Go to title screen
            mainNavController.navigate(R.id.action_go_back_to_title_page)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    //OnFragmentChangedActionBarListener interface methods
    override fun setActionBarVisibility(makeItVisible: Boolean) {
        if (makeItVisible) {
            actionBar?.show()
        } else {
            actionBar?.hide()
        }
    }
}
