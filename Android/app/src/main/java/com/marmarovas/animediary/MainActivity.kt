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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.marmarovas.animediary.databinding.ActivityMainBinding
import com.marmarovas.animediary.utils.TokenDataAccess

/**
 * MainActivity is only responsible for setting the content view that contains the
 * Navigation Host.
 */

class MainActivity : AppCompatActivity() {

    private var actionBar: ActionBar? = null
    private var actionBarMenu: Menu? = null

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainNavController: NavController

    private val actionBarViewModel by viewModels<ActionBarViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Get instance of binding class
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        mainNavController = findNavController(R.id.myNavHostFragment)

        //set action bar
        setSupportActionBar(binding.appActionBar)

        //Up button will not be displayed on these destinations
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.titlePage, R.id.myCollectionListFragment))

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

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
//                searchView.setQuery("", false)
//                searchItem.collapseActionView()
                actionBarViewModel.setSearchQuery(query)
                Toast.makeText(this@MainActivity, "Query is $query", Toast.LENGTH_LONG).show()
                return true
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                if (mainNavController.currentDestination?.id == R.id.addAnimeListFragment){
                    actionBarViewModel.goToMyAnimeCollectionPage.sendAction(true)
                } else {
                    invalidateOptionsMenu()
                }
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        actionBarMenu = menu
        return super.onPrepareOptionsMenu(menu);
    }

    //Handle action bar click action event
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_add_review -> {
            //go to add screen
            item.isVisible = false
            actionBarMenu?.findItem(R.id.action_search)?.expandActionView()
            mainNavController.navigate(R.id.action_myCollectionListFragment_to_addAnimeListFragment)
            true
        }

        R.id.action_search -> {
            actionBarMenu?.findItem(R.id.action_add_review)?.isVisible = false
            actionBarMenu?.findItem(R.id.action_save_or_edit)?.isVisible = false
            true
        }

        R.id.action_save_or_edit -> {
            val saveOrEditView = actionBarMenu?.findItem(R.id.action_save_or_edit)

            if (saveOrEditView?.title != null && saveOrEditView.title == getString(R.string.save_text)) {
                saveOrEditView.title = getString(R.string.edit_text)
                actionBarViewModel.onSaveClicked()
            } else {
                saveOrEditView?.title = getString(R.string.save_text)
                actionBarViewModel.onEditClicked()
            }

            true
        }

        R.id.action_log_out -> {
            //delete current token
            TokenDataAccess.setTokenToNull(this)

            //Reset action bar
            invalidateOptionsMenu()

            //Reset the value (fix bug temporarily. Not sure why the observer gets triggered after logging out and logging in again)
            actionBarViewModel.onAddReviewAction(false)

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

    private fun addObservers() {
        //Observer action bar title changes
        actionBarViewModel.actionBarTitle.observe(this, Observer {
            actionBar?.title = it
        })

        //Observe action bar subtitle changes
        actionBarViewModel.actionBarSubtitle.observe(this, Observer {
            actionBar?.subtitle = it
        })

        //Observer show or hide action bar
        actionBarViewModel.showActionBar.observe(this, Observer {
            if (it) {
                actionBar?.show()
            } else {
                actionBar?.hide()
            }
        })

        //Observe the save review button (hide or show)
        actionBarViewModel.showReviewPageActionBarMenu.observe(this, Observer {
            it?.let {
                actionBarMenu?.findItem(R.id.action_add_review)?.isVisible = false
                actionBarMenu?.findItem(R.id.action_search)?.isVisible = false
                actionBarMenu?.findItem(R.id.action_log_out)?.isVisible = false
                actionBarMenu?.findItem(R.id.action_save_or_edit)?.isVisible = true

                val searchView = actionBarMenu?.findItem(R.id.action_search)
                if(searchView != null && searchView.isActionViewExpanded){
                    actionBarMenu?.findItem(R.id.action_search)?.collapseActionView()
                }

                val saveOrEditView = actionBarMenu?.findItem(R.id.action_save_or_edit)
                saveOrEditView?.title = getString(R.string.edit_text)
            }
        })

        //Observe request to display "My Collection" screen action bar menu
        actionBarViewModel.showMyCollectionPageActionBarMenu.observe(this, Observer {
            it.let {
                actionBarMenu?.findItem(R.id.action_add_review)?.isVisible = true
                actionBarMenu?.findItem(R.id.action_search)?.isVisible = true
                actionBarMenu?.findItem(R.id.action_log_out)?.isVisible = true
                actionBarMenu?.findItem(R.id.action_save_or_edit)?.isVisible = false

                val searchView = actionBarMenu?.findItem(R.id.action_search)
                if(searchView != null && searchView.isActionViewExpanded){
                    actionBarMenu?.findItem(R.id.action_search)?.collapseActionView()
                }
            }
        })

        //Observe request to display "Add Review Page" screen action bar menu
        actionBarViewModel.showAddReviewPageActionBarMenu.observe(this, Observer {
            it.let {
                actionBarMenu?.findItem(R.id.action_add_review)?.isVisible = false
                actionBarMenu?.findItem(R.id.action_log_out)?.isVisible = false
                actionBarMenu?.findItem(R.id.action_save_or_edit)?.isVisible = false

                actionBarMenu?.findItem(R.id.action_search)?.isVisible = true
                actionBarMenu?.findItem(R.id.action_search)?.expandActionView()
            }
        })

        //Observe action when to collapse or expand search view
        actionBarViewModel.collapseSearchAction.observe(this, Observer {
            it?.let {
                actionBarMenu?.findItem(R.id.action_search)?.collapseActionView()
            }
        })

        //Observe whether to show the save button
        actionBarViewModel.showSaveButton.observe(this, Observer {
            actionBarMenu?.findItem(R.id.action_save_or_edit)?.title = getString(R.string.save_text)
        })
    }

}
