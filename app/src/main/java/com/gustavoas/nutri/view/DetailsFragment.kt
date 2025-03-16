package com.gustavoas.nutri.view

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.transition.MaterialContainerTransform
import com.gustavoas.nutri.R
import com.gustavoas.nutri.Utils.getRatingString
import com.gustavoas.nutri.model.NutritionProfessional
import com.gustavoas.nutri.repository.ProfessionalsDatabase
import com.gustavoas.nutri.repository.ProfessionalsRepository
import com.gustavoas.nutri.viewmodel.DetailsViewModel
import com.gustavoas.nutri.viewmodel.DetailsViewModelFactory

class DetailsFragment: Fragment() {
    private val database by lazy { ProfessionalsDatabase.getDatabase(requireContext()) }
    private val storageDao by lazy { database.professionalsStorageDao() }
    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this, DetailsViewModelFactory(ProfessionalsRepository(storageDao)))[DetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postponeEnterTransition()
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        val professional = arguments?.getParcelable("professional", NutritionProfessional::class.java)

        view.findViewById<TextView>(R.id.languages).visibility = View.GONE

        val sharedView = view.findViewById<LinearLayout>(R.id.detailsContainer)
        sharedView?.transitionName = "shared_element_transition"

        val transition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragmentContainer
            duration = 500
            scrimColor = Color.TRANSPARENT
            fadeMode = MaterialContainerTransform.FADE_MODE_CROSS
        }

        sharedElementEnterTransition = transition

        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

        professional?.let {
            populateProfessional(it, view)
        }

        if (savedInstanceState == null) {
            observeData()
        }

        if (viewModel.professional.value == null) {
            professional?.let {
                viewModel.loadProfessional(requireContext(), professional.id)
            }
        }

        return view
    }

    private fun populateProfessional(professional: NutritionProfessional, view: View? = this.view) {
        if (view == null) return

        view.findViewById<TextView>(R.id.name).text = professional.name
        view.findViewById<TextView>(R.id.rating).text = getRatingString(professional.rating, professional.rating_count)

        Glide.with(requireContext())
            .load(professional.profile_picture_url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .into(view.findViewById(R.id.image))

        val aboutMeText = view.findViewById<TextView>(R.id.aboutMeText)

        val aboutMeContainer = view.findViewById<LinearLayout>(R.id.aboutMeContainer)
        if (!professional.about_me.isNullOrEmpty()) {
            aboutMeContainer.visibility = View.VISIBLE

            aboutMeText?.text = professional.about_me

            aboutMeText.post {
                val moreContainer =view.findViewById<LinearLayout>(R.id.moreContainer)
                if (aboutMeText.layout.getEllipsisCount(aboutMeText.lineCount - 1) > 0) {
                    moreContainer.visibility = View.VISIBLE
                } else {
                    moreContainer.visibility = View.GONE
                }
            }
        } else {
            aboutMeContainer?.visibility = View.GONE
        }

        val showMore = view.findViewById<Button>(R.id.more)
        val moreArrow = view.findViewById<ImageView>(R.id.moreArrow)

        showMore.setOnClickListener {
            val targetRotation = if (aboutMeText.maxLines == 3) 180f else 0f
            val rotateAnimator = ObjectAnimator.ofFloat(moreArrow, "rotation", moreArrow.rotation, targetRotation)
            rotateAnimator.duration = 300
            rotateAnimator.start()

            if (aboutMeText.maxLines == 3) {
                aboutMeText.maxLines = Int.MAX_VALUE
                showMore.text = getString(R.string.less)
            } else {
                aboutMeText.maxLines = 3
                showMore.text = getString(R.string.more)
            }
        }
    }

    private fun observeData() {
        viewModel.professional.observe(viewLifecycleOwner) { professional ->
            professional?.let {
                populateProfessional(it)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                if (view?.findViewById<LinearLayout>(R.id.aboutMeContainer)?.visibility != View.GONE) {
                    return@observe
                }
                view?.findViewById<LinearLayout>(R.id.progressContainer)?.visibility = View.VISIBLE
            } else {
                view?.findViewById<LinearLayout>(R.id.progressContainer)?.visibility = View.GONE
            }
        }
    }
}