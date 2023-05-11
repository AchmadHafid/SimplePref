package io.github.achmadhafid.sample_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.achmadhafid.sample_app.databinding.ActivityChildBinding
import io.github.achmadhafid.simplepref.SimplePref
import io.github.achmadhafid.zpack.extension.getViewModel

class ChildActivity : AppCompatActivity(), SimplePref {

    //region View Binding

    private val binding by lazy {
        ActivityChildBinding.inflate(layoutInflater)
    }

    //endregion
    //region View Model

    private val viewModel by lazy {
        getViewModel<ChildActivityViewModel>(viewModelFactory)
    }
    private val viewModelFactory by lazy {
        ChildActivityViewModelFactory(application)
    }

    //endregion
    //region Lifecycle Callback

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getMyInt()
            .observe(this) {
                binding.content.tvLocalVar.text = "myInt (Local pref) : $it"
            }

        viewModel.getMyList()
            .observe(this) {
                binding.content.tvGlobalVar.text = "myList Size (Global pref): ${it.size}"
            }

        binding.content.btnAdd.setOnClickListener {
            viewModel.changeValue()
        }
        binding.content.btnClearLocal.setOnClickListener {
            viewModel.clearAllLocal()
        }
        binding.content.btnClearGlobal.setOnClickListener {
            viewModel.clearMyList()
        }
    }

    //endregion

}
