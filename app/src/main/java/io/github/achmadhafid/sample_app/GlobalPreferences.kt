package io.github.achmadhafid.sample_app

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleService
import io.github.achmadhafid.simplepref.SimplePref
import io.github.achmadhafid.simplepref.simplePref

fun <A> A.globalPrefMyList()
        where A : SimplePref, A : FragmentActivity =
    simplePref<A, MutableList<String>>("my_list") { mutableListOf() }
fun <F> F.globalPrefMyList()
        where F : SimplePref, F : Fragment =
    simplePref<F, MutableList<String>>("my_list") { mutableListOf() }
fun <S> S.globalPrefMyList()
        where S : SimplePref, S : LifecycleService =
    simplePref<S, MutableList<String>>("my_list") { mutableListOf() }

fun <A> A.globalPrefIsServiceRunning()
        where A : SimplePref, A : FragmentActivity =
    simplePref("is_service_running") { false }
fun <F> F.globalPrefIsServiceRunning()
        where F : SimplePref, F : Fragment =
    simplePref("is_service_running") { false }
fun <S> S.globalPrefIsServiceRunning()
        where S : SimplePref, S : LifecycleService =
    simplePref("is_service_running") { false }
