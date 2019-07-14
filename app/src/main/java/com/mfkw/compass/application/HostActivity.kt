package com.mfkw.compass.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mfkw.compass.R
import kotlinx.android.synthetic.main.activity_host.*

class HostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)
        setSupportActionBar(toolbar)

        toolbar.setupWithNavController(findNavController(R.id.navHostFragment))
    }
}

val Fragment.hostActivity: HostActivity
    get() = activity as? HostActivity ?: throw RuntimeException("Fragment outside host activity")
