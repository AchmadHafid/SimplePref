package io.github.achmadhafid.sample_app

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleService
import io.github.achmadhafid.simplepref.SimplePref
import io.github.achmadhafid.simplepref.lifecycle.SimplePrefLifecycleOwner
import io.github.achmadhafid.simplepref.simplePref

fun <ACTIVITY> ACTIVITY.globalPrefMyList()
        where ACTIVITY : SimplePref, ACTIVITY : FragmentActivity =
    simplePref<ACTIVITY, MutableList<String>>("my_list") { mutableListOf() }
fun <FRAGMENT> FRAGMENT.globalPrefMyList()
        where FRAGMENT : SimplePref, FRAGMENT : Fragment =
    simplePref<FRAGMENT, MutableList<String>>("my_list") { mutableListOf() }
fun <SERVICE> SERVICE.globalPrefMyList()
        where SERVICE : SimplePref, SERVICE : LifecycleService =
    simplePref<SERVICE, MutableList<String>>("my_list") { mutableListOf() }
fun <LIFECYCLE : SimplePrefLifecycleOwner> LIFECYCLE.globalPrefMyList() =
    simplePref<LIFECYCLE, MutableList<String>>("my_list") { mutableListOf() }

fun <ACTIVITY> ACTIVITY.globalPrefIsServiceRunning()
        where ACTIVITY : SimplePref, ACTIVITY : FragmentActivity =
    simplePref("is_service_running") { false }
fun <FRAGMENT> FRAGMENT.globalPrefIsServiceRunning()
        where FRAGMENT : SimplePref, FRAGMENT : Fragment =
    simplePref("is_service_running") { false }
fun <SERVICE> SERVICE.globalPrefIsServiceRunning()
        where SERVICE : SimplePref, SERVICE : LifecycleService =
    simplePref("is_service_running") { false }
fun <LIFECYCLE : SimplePrefLifecycleOwner> LIFECYCLE.globalPrefIsServiceRunning() =
    simplePref("is_service_running") { false }
