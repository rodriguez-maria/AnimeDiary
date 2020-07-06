package com.marmarovas.animediary.screens.reviewpage

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

import com.marmarovas.animediary.R
import com.marmarovas.animediary.SharedViewModel

class ReviewPageFragment : Fragment() {

    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private val viewModel by viewModels<ReviewPageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.review_page_fragment, container, false)
    }

}
