package com.gustavoas.nutri.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.gustavoas.nutri.MainActivity
import com.gustavoas.nutri.R
import com.gustavoas.nutri.Utils.getRatingString
import com.gustavoas.nutri.model.NutritionProfessional

private const val ITEM_TYPE_HEADER = 0
private const val ITEM_TYPE_PROFESSIONAL = 1
private const val ITEM_TYPE_LOADING = 2

class SearchAdapter(private val items: MutableList<NutritionProfessional>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var showLoadingFooter = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_header, parent, false)
                SpinnerViewHolder(view)
            }
            ITEM_TYPE_LOADING -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_footer, parent, false)
                LoadingViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_professional, parent, false)
                ProfessionalViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SpinnerViewHolder -> {
                holder.bind()
            }
            is ProfessionalViewHolder -> {
                holder.bind(items.getOrNull(position - 1) ?: return)
            }
            is LoadingViewHolder -> {}
        }
    }

    override fun getItemCount(): Int = items.size + if (showLoadingFooter) 2 else 1

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ITEM_TYPE_HEADER
            items.size + 1 -> ITEM_TYPE_LOADING
            else -> ITEM_TYPE_PROFESSIONAL
        }
    }

    fun addItems(newItems: List<NutritionProfessional>, startPosition: Int) {
        for (i in newItems.indices) {
            if (startPosition + i < items.size) {
                items[startPosition + i] = newItems[i]
            } else {
                items.addAll(newItems.takeLast(newItems.size - i))
            }
        }
        notifyItemRangeInserted(startPosition + 1, newItems.size)
    }

    fun showLoading() {
        showLoadingFooter = true
        notifyItemInserted(items.size + 1)
    }

    fun hideLoading() {
        showLoadingFooter = false
        notifyItemRemoved(items.size + 1)
    }

    fun clearItems() {
        items.clear()

        notifyDataSetChanged()
    }

    class SpinnerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val spinner: Spinner = view.findViewById(R.id.sortSpinner)

        fun bind() {
            val context = itemView.context
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val options = context.resources.getStringArray(R.array.sortOptions)
            val optionValues = context.resources.getStringArray(R.array.sortOptionsValues)

            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, options)
            spinner.adapter = adapter

            val savedSortOption = sharedPreferences.getString("sortOption", optionValues[0])
            spinner.setSelection(optionValues.indexOf(savedSortOption))

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedValue = optionValues.getOrNull(position)
                    sharedPreferences.edit().putString("sortOption", selectedValue).apply()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    class ProfessionalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.name)
        private val rating: TextView = view.findViewById(R.id.rating)
        private val profileImage: ImageView = view.findViewById(R.id.image)
        private val languages: TextView = view.findViewById(R.id.languages)
        private val expertises: ChipGroup = view.findViewById(R.id.expertises)

        fun bind(professional: NutritionProfessional) {
            name.text = professional.name
            rating.text = getRatingString(professional.rating, professional.rating_count)
            languages.text = professional.languages.joinToString(", ")
            expertises.removeAllViews()

            professional.expertise.forEach {
                val chip = Chip(itemView.context)
                chip.text = it
                chip.isClickable = false
                expertises.addView(chip)
            }

            Glide.with(itemView.context)
                .load(professional.profile_picture_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(profileImage)

            itemView.setOnClickListener {
//                val action = SearchFragmentDirections
//                    .actionSearchFragmentToDetailsFragment(professional)
//
//                it.findNavController().navigate(action)

                val bundle = Bundle()
                bundle.putParcelable("professional", professional)

                val detailsFragment = DetailsFragment()
                detailsFragment.arguments = bundle

                val fragmentManager = (itemView.context as MainActivity).supportFragmentManager

                val sharedView = itemView.findViewById<LinearLayout>(R.id.detailsContainer)
                sharedView.transitionName = "shared_element_transition"

                fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .addSharedElement(sharedView, "shared_element_transition")
                    .replace(R.id.fragmentContainer, detailsFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)
}