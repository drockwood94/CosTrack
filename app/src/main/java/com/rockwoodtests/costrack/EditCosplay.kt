package com.rockwoodtests.costrack

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_edit_cosplay.*
import kotlinx.android.synthetic.main.content_edit_cosplay.*

private const val NUM_PAGES = 4

class EditCosplay : AppCompatActivity(), CosplayView.OnFragmentInteractionListener, ReferenceView.OnFragmentInteractionListener, ToolView.OnFragmentInteractionListener, StatView.OnFragmentInteractionListener {

    private var cosplayID: String? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_components -> {
                pagerManager.setCurrentItem(0, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_references -> {
                pagerManager.setCurrentItem(1, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_tools -> {
                pagerManager.setCurrentItem(2, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_stats -> {
                pagerManager.setCurrentItem(3, true)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private lateinit var pagerManager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cosplay)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        cosplayID = intent.extras?.getString("cosplayID")   // TODO: Change to ID and use flag to signify if it is for a cosplay or a component
        val data = Bundle()
        data.putString("cosplayID", intent.extras?.getString("cosplayID"))

        pagerManager = mainCosplayContainer
        val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager, data)
        val pageChangeListener = ScreenSlidePageChangeListener(cosplay_navigation)
        pagerManager.adapter = pagerAdapter
        pagerManager.offscreenPageLimit = 3
        pagerManager.addOnPageChangeListener(pageChangeListener)

        cosplay_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    fun showSelectedComponent(v: View) {
        val intent = Intent(this, EditComponent::class.java)
        intent.putExtra("cosplayID", cosplayID)
        startActivity(intent)
    }

    fun createNewComponent(v: View) {
        startActivity(Intent(this, NewComponent::class.java))
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager, private val data: Bundle) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = NUM_PAGES

        override fun getItem(position: Int): Fragment {
            when (position) {
                0  -> {
                    val cv = CosplayView()
                    cv.arguments = data
                    return cv
                }
                1 -> {
                    val rv = ReferenceView()
                    val specialData = data
                    specialData.putString("type", "cosplay")
                    rv.arguments = specialData
                    return rv
                }
                2 -> {
                    val tv = ToolView()
                    tv.arguments = data
                    return tv
                }
                3 -> {
                    val sv = StatView()
                    sv.arguments = data
                    return sv
                }
            }
            val cv = CosplayView()
            cv.arguments = data

            return cv
        }
    }

    private inner class ScreenSlidePageChangeListener(bottomNavigationView: BottomNavigationView) : ViewPager.OnPageChangeListener {
        var bnv = bottomNavigationView

        override fun onPageScrollStateChanged(p0: Int) {
        }

        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
        }

        override fun onPageSelected(p0: Int) {
            bnv.menu.getItem(p0).isChecked = true
        }
    }

}
