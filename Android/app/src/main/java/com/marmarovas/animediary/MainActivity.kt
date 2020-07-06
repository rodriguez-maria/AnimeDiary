package com.marmarovas.animediary

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.marmarovas.animediary.databinding.ActivityMainBinding
import com.marmarovas.animediary.utils.OnFragmentChangedActionBarListener
import com.marmarovas.animediary.utils.TokenDataAccess

/**
 * MainActivity is only responsible for setting the content view that contains the
 * Navigation Host.
 */

class MainActivity : AppCompatActivity(){

    private var actionBar: ActionBar? = null
    private var actionBarMenu : Menu? = null

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainNavController : NavController

    private val sharedViewModel by viewModels<SharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Get instance of binding class
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        mainNavController = findNavController(R.id.myNavHostFragment)

        //set action bar
        setSupportActionBar(binding.appActionBar)

        //Up button will not be displayed on these destinations
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.titlePage, R.id.myCollectionListFragment))

        //Set up the action "Up Button"
        setupActionBarWithNavController(mainNavController, appBarConfiguration)

        actionBar = supportActionBar
        actionBar?.title = getString(R.string.app_name)

        addObservers()
    }

    //Inflate the action bar and set actions listeners
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_actions, menu)

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        val addView = menu.findItem(R.id.action_add_review)

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
//                searchView.setQuery("", false)
//                searchItem.collapseActionView()
                sharedViewModel.setSearchQuery(query)
                Toast.makeText(this@MainActivity, "Query is $query", Toast.LENGTH_LONG).show()
                return true
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                //This tells Android OS that the content of the menu has changed and menu should be redrawn
                invalidateOptionsMenu()
                sharedViewModel.setSearchButtonExpanded(false)
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
//                menu.findItem(R.id.action_add_review)?.isVisible = false
                sharedViewModel.setSearchButtonExpanded(true)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        actionBarMenu = menu

//        if(menu != null){
//            val addItem = menu.findItem(R.id.action_add_review)
//            val searchItem = menu.findItem(R.id.action_search)
//
//            if(!searchItem.isActionViewExpanded && !addItem.isVisible){
//                addItem.isVisible = true
//            }
//
//            if(!searchItem.isActionViewExpanded){
//                sharedViewModel.setSearchButtonExpanded(false)
//            }
//
//            if(!addItem.isVisible){
//                searchItem.expandActionView()
//            }
//        }
        return super.onPrepareOptionsMenu(menu);
    }


    //Handle action bar click action event
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_add_review -> {
            //go to add screen
//            sharedViewModel.onAddReviewAction(true)
            item.isVisible = false
            actionBarMenu?.findItem(R.id.action_search)?.expandActionView()
            mainNavController.navigate(R.id.action_myCollectionListFragment_to_addAnimeListFragment)
            true
        }

        R.id.action_search -> {
            actionBarMenu?.findItem(R.id.action_add_review)?.isVisible = false
            true
        }

        R.id.action_log_out -> {
            //delete current token
            TokenDataAccess.setTokenToNull(this)

            //Reset action bar
            invalidateOptionsMenu()

            //Reset the value (fix bug temporarily. Not sure why the observer gets triggered after logging out and logging in again)
            sharedViewModel.onAddReviewAction(false)

            //Go to title screen
            mainNavController.navigate(R.id.action_go_back_to_title_page)
            true
        }

        else -> {
            false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return mainNavController.navigateUp()
    }

    private fun addObservers(){
        //Observer action bar title changes
        sharedViewModel.actionBarTitle.observe(this, Observer {
            actionBar?.title = it
        })

        //Observe action bar subtitle changes
        sharedViewModel.actionBarSubtitle.observe(this, Observer {
            actionBar?.subtitle = it
        })

        //Observer show or hide action bar
        sharedViewModel.showActionBar.observe(this, Observer {
            if(it){
                actionBar?.show()
            } else {
                actionBar?.hide()
            }
        })
    }

}
