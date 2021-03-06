package com.example.appclinica.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter


class IntroViewPagerAdapter(var mContext: Context,var mListScreen: List<ScreenItem> ): PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutScreen: View = inflater.inflate(com.example.appclinica.R.layout.layout_screen, null)

        val imgSlide = layoutScreen.findViewById<ImageView>(com.example.appclinica.R.id.intro_img)
        val title = layoutScreen.findViewById<TextView>(com.example.appclinica.R.id.intro_title)
        val description = layoutScreen.findViewById<TextView>(com.example.appclinica.R.id.intro_description)

        title.setText(mListScreen.get(position).titulo)
        description.setText(mListScreen.get(position).descripcion)
        imgSlide.setImageResource(mListScreen.get(position).screenImg)
        container.addView(layoutScreen)

        return layoutScreen
    }

    override fun getCount(): Int = mListScreen.size

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)

    }


}