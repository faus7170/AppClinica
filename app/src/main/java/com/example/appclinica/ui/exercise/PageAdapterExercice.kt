package com.example.appclinica.ui.exercise

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.appclinica.ui.exercise.fragment.ExerciseFree
import com.example.appclinica.ui.exercise.fragment.ExerciseToPay

class PageAdapterExercice(fm: FragmentManager): FragmentPagerAdapter(fm) {


    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {

        when(position){
            0 -> {
                return ExerciseFree()
            }
            else -> {
                return ExerciseToPay()
            }

        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> {
                "Gratis"
            }
            else -> {
                return "Pago"
            }

        }
    }
}