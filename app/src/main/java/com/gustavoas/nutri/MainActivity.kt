package com.gustavoas.nutri

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.appbar.MaterialToolbar
import com.gustavoas.nutri.Utils.dpToPx
import com.gustavoas.nutri.view.SearchFragment

class MainActivity: AppCompatActivity() {
    private val topAppBar by lazy { findViewById<MaterialToolbar>(R.id.topAppBar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashScreen)
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SearchFragment())
                .commit()
        }

        updateUpNavigationVisibility()
    }

    override fun onStart() {
        super.onStart()

        supportFragmentManager.addOnBackStackChangedListener {
            updateUpNavigationVisibility()
        }

        topAppBar.setNavigationOnClickListener {
            supportFragmentManager.popBackStack()
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bug_report -> {
                    val sendEmail = Intent(Intent.ACTION_SENDTO).apply {
                        data =
                            Uri.parse("mailto:gustavoamorimsantos10+nutri@gmail.com"
                                    + "?subject=" + Uri.encode("Nutri"))
                    }
                    sendEmail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(sendEmail)
                    true
                }

                else -> false
            }
        }
    }

    private fun updateUpNavigationVisibility() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            topAppBar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            topAppBar.setNavigationIconTint(ContextCompat.getColor(this, R.color.text))
            topAppBar.setTitleMargin(0, 0, 0, 0)
        } else {
            topAppBar.navigationIcon = null
            topAppBar.setTitleMargin(dpToPx(this, 40), 0, 0, 0)
        }
    }
}