package io.github.achmadhafid.sample_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.achmadhafid.zpack.ktx.setMaterialToolbar

class ChildActivityWithFragment : AppCompatActivity(R.layout.activity_child_with_fragment) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMaterialToolbar(R.id.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
