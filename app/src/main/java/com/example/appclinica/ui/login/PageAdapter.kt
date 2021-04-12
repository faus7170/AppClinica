package com.example.appclinica.ui.login

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.appclinica.ui.login.fragmentos.LoginFragment
import com.example.appclinica.ui.login.fragmentos.RegisterFragment

class PageAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {


    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {

        when(position){
            0 -> {
                return LoginFragment()
            }
            else -> {
                return RegisterFragment()
            }

        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> {
                "Iniciar sesión"
            }
            else -> {
                return "Registrar"
            }

        }
    }
}