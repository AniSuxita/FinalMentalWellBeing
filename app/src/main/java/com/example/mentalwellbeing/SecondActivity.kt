package com.example.mentalwellbeing

import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SecondActivity : AppCompatActivity() {

    private lateinit var tablayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        tablayout = findViewById(R.id.tablayout)
        viewPager = findViewById(R.id.viewpager)
        viewPager.adapter = PagerAdapter(this)

        TabLayoutMediator(tablayout,viewPager){tab,index ->
            tab.text = when(index){
                0 -> {"Home"}
                1 -> {"Checklist"}
                2 -> {"Profile"}
                else -> {throw Resources.NotFoundException("Position not found")}
            }


        }.attach()

        tablayout.getTabAt(0)!!.setIcon(R.drawable.homeicon)
        tablayout.getTabAt(1)!!.setIcon(R.drawable.checklisticon)
        tablayout.getTabAt(2)!!.setIcon(R.drawable.profileicon)

    }
}