package io.github.achmadhafid.sample_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ChildActivityWithFragment : AppCompatActivity(R.layout.activity_child_with_fragment) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
