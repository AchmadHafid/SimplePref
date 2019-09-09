package io.github.achmadhafid.sample_app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import io.github.achmadhafid.simplepref.SimplePref
import io.github.achmadhafid.zpack.ktx.bindView
import io.github.achmadhafid.zpack.ktx.getViewModel
import io.github.achmadhafid.zpack.ktx.onSingleClick
import io.github.achmadhafid.zpack.ktx.setMaterialToolbar

class ChildActivity : AppCompatActivity(R.layout.activity_child), SimplePref {

    //region View Binding

    private val tvLocalVar: TextView by bindView(R.id.tvLocalVar)
    private val tvGlobalVar: TextView by bindView(R.id.tvGlobalVar)
    private val btnAdd: MaterialButton by bindView(R.id.btnAdd)
    private val btnClearLocal: MaterialButton by bindView(R.id.btnClearLocal)
    private val btnClearGlobal: MaterialButton by bindView(R.id.btnClearGlobal)

    //endregion
    //region View Model

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .getViewModel<ChildActivityViewModel>()
    }
    private val viewModelFactory by lazy {
        ChildActivityViewModelFactory(application)
    }

    //endregion

    //region Lifecycle Callback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMaterialToolbar(R.id.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getMyInt()
            .observe(this, Observer {
                tvLocalVar.text  = "myInt (Local pref) : $it"
            })

        viewModel.getMyList()
            .observe(this, Observer {
                tvGlobalVar.text = "myList Size (Global pref): ${it.size}"
            })

        btnAdd.onSingleClick(true) {
            viewModel.changeValue()
        }
        btnClearLocal.onSingleClick(true) {
            viewModel.clearAllLocal()
        }
        btnClearGlobal.onSingleClick(true) {
            viewModel.clearMyList()
        }
    }

    //endregion

}
