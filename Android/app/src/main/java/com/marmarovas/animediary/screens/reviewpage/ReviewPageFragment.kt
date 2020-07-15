package com.marmarovas.animediary.screens.reviewpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs

import com.marmarovas.animediary.R
import com.marmarovas.animediary.ActionBarViewModel
import com.marmarovas.animediary.AnimeItemViewModel
import com.marmarovas.animediary.databinding.ReviewPageFragmentBinding
import com.marmarovas.animediary.network.animes.AnimeData
import com.marmarovas.animediary.screens.animeslistpage.AddAnimeListFragment
import com.marmarovas.animediary.utils.AnimeDiaryUtils

class ReviewPageFragment : Fragment() {

    private val actionBarViewModel by activityViewModels<ActionBarViewModel>()
    private val animeItemViewModel by activityViewModels<AnimeItemViewModel>()
    private val reviewPageViewModel by viewModels<ReviewPageViewModel>()

    private val args : ReviewPageFragmentArgs by navArgs()

    private lateinit var animeItem: AnimeData
    private lateinit var binding: ReviewPageFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.review_page_fragment, container, false)

        //Observe data and actions
        addObservers()

        //Set the title for this screen in the action bar
        actionBarViewModel.setActionBarTitle("Add Review")

        //Display the right action bar menu for this screen
        actionBarViewModel.showReviewPageActionBarMenu.sendAction(true)

        //show action bar on this fragment
        actionBarViewModel.setShowActionBar(true)

        return binding.root
    }

    private fun addObservers() {
        //Observe the item that has been selected
        animeItemViewModel.selectedAnimeItem.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                showMessage("Something went wrong. Try again.")
            } else {
                animeItem = it
                displayItemDetails()

                //Show the save button and editable option if coming from "add anime fragment"
                if(args.showEdit){
                    actionBarViewModel.showSaveButton.sendAction(true)
                    onEditClicked()
                }
            }
        })

        //Observer save action
        actionBarViewModel.saveClickedAction.observe(viewLifecycleOwner, Observer {
            //Only for testing purposes
            Toast.makeText(context, "save clicked action observer", Toast.LENGTH_SHORT).show()
            onSaveClicked()
        })

        //Observe edit action
        actionBarViewModel.editClickedAction.observe(viewLifecycleOwner, Observer {
            //Only for testing purposes
            Toast.makeText(context, "edit clicked action observer", Toast.LENGTH_SHORT).show()
            onEditClicked()
        })

        //Observe error message
        reviewPageViewModel.error.observe(viewLifecycleOwner, Observer {
            showMessage(it)
        })

        //Observe success message
        reviewPageViewModel.success.observe(viewLifecycleOwner, Observer {
            showMessage(it)
        })
    }

    private fun displayItemDetails() {
        //Set image
        binding.animeTitle.text = animeItem.anime.title
//        binding.reviewTextView.text = animeItem.notes

        //Set rating
        var numStars = 0f
        if (animeItem.rating != null) {
            numStars = animeItem.rating!!.toFloat()
        }

        binding.ratingBar.rating = numStars

        //set image
        AnimeDiaryUtils.bindImage(binding.animeImage, animeItem.anime.image)

        //Set notes
        binding.myReviewTextView.text = animeItem.notes

    }

    private fun onEditClicked() {
        binding.myReviewTextView.visibility = View.GONE
        binding.ratingInstructionTextView.visibility = View.VISIBLE
        binding.writeReviewTextView.visibility = View.VISIBLE

        binding.reviewEditText.setText(animeItem.notes)
        binding.reviewEditText.visibility = View.VISIBLE
    }

    private fun onSaveClicked() {
        verifyChanges()

        binding.ratingInstructionTextView.visibility = View.GONE
        binding.writeReviewTextView.visibility = View.GONE
        binding.reviewEditText.visibility = View.GONE

        binding.myReviewTextView.text = binding.reviewEditText.text
        binding.myReviewTextView.visibility = View.VISIBLE
    }

    private fun verifyChanges() {
        if (binding.ratingBar.rating == 0f) {
            showMessage("Please add a rating for this anime")
            return
        }

        if(binding.reviewEditText.text.isEmpty()){
            showMessage("Please add a review")
            return
        }

        var itemAfterChanges = AnimeData(
            animeItem.anime,
            binding.reviewEditText.text.toString(),
            binding.ratingBar.rating,
            animeItem.tags
        )

        reviewPageViewModel.confirmChanges(animeItem, itemAfterChanges, context)
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}
