SimplePref
==========

[![Release](https://jitpack.io/v/AchmadHafid/SimplePref.svg)](https://jitpack.io/#AchmadHafid/SimplePref)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)

**Assalamu'alaikum brothers and sisters, peace be upon you!**

Use Android Shared Preference like never before!<br />
Your quest for ultimate android shared preferences solution ends here!

![image](https://drive.google.com/uc?export=download&id=1hTpbapjA51gZrrfuhGFvDXwMQmJPSAcq)
<br />
[**Download Demo App Here**](https://github.com/AchmadHafid/SimplePref/releases/download/v2.2.0/SimplePref.v2.2.0.apk)


Key Features
--------
* Flexible by design, no such thing like "Preference Model" that needs to be defined upfront!
* Create local shared preferences (scope by Activity, Fragment, etc..) in addition to regular global shared preferences!
* Full support for Non-null & Nullable preferences with single API!
* Full support for non-native data types via converters
* Observe preference as a live data!
* Leak-proof (Demo app include LeakCanary to proof it), support lifecycle aware components out of the box.<br />
  (e.g. `FragmentActivity`, `Fragment`, `LifecycleService`)
* Optionally can be used inside `Application`, `AndroidViewModel` or other non-lifecycle aware component <br />


Compatibility
-------------

This library is compatible from API 21 (Android 5.0 Lollipop) & AndroidX.


Download
--------

Add jitpack repository into your root build.gradle

```groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
    ...
  }
}
```

Add the dependency

```groovy
dependencies {
  ...
  implementation "com.github.AchmadHafid:SimplePref:2.2.3"
  ...
}
```


Quick Usage
-----------

<details>
  <summary>Application</summary>
  <br />

```kotlin

// 1. Enable API by making App class extend SimplePrefLifecycleOwner with its delegate like below
class MyApp : Application(), SimplePrefLifecycleOwner by SimplePrefApplication() {

    // 2. defined your shared preferences
    private var appTheme: Int? by simplePref("global_key_app_theme") // nullable global shared preference

    override fun onCreate() {
        super.onCreate()

        // 3. Attach context using below function
        attachSimplePrefContext(this)

        // 4. Use it like normal var/val
        appTheme?.let { applyTheme(it) }

        // 5.  Or create live data via extension function below
        simplePrefLiveData(appTheme, ::appTheme) {
            it?.let { applyTheme(it) }
        }
    }
}

```

</details>
<details>
  <summary>Fragment Activity / Fragment / LifecycleService </summary>
  <br />

```kotlin

// 1. Enable API by making class extend SimplePref interface
class MainActivity : AppCompatActivity(R.layout.activity_main), SimplePref {

    // 2. Defined your shared preferences
    private var showNotification by simplePref { false }             // non-null local shared preference with default value
    private var appTheme: Int? by simplePref("global_key_app_theme") // nullable global shared preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 3. Observe it as live data if you want
        simplePrefLiveData(appTheme, ::appTheme) { theme ->
          theme?.let { changeTheme(it) }
        }
    }

    private fun changeTheme(newTheme: Int) {
      // 4. Or Use it like normal var/val
      appTheme = toggleTheme(newTheme)
    }

}
```

</details>
<details>
  <summary>AndroidViewModel</summary>
  <br />

```kotlin

// 1. Enable API by making view model class extend SimplePrefLifecycleOwner with its delegate like below
class HomeViewModel(application: Application) : AndroidViewModel(application),
    SimplePrefLifecycleOwner by SimplePrefViewModel(application) {

    // 2. Defined your shared preferences
    private var showNotification by simplePref { false }             // non-null local shared preference with default value
    private val appTheme: Int? by simplePref("global_key_app_theme") // nullable global shared preference

    // 3. Expose it as LiveData like below
    fun getAppTheme() = simplePrefLiveData(appTheme, ::appTheme)
}
```

</details>


Non-native Data Types
---------------------

In order to use a shared preference with non native data types (types other than `Boolean`, `String`, `Int`, `Long` & `Float`),
you must do 2 things, provide converters before accessing any SimplePref API & call save API to persist the value immediately after you do some operation on it.

```kotlin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Add converter for data type `MutableList<String>`
        simplePrefAddConverter<MutableList<String>> {
            onSerialize {
                it.joinToString("::")
            }
            onDeserialize {
                if (it.isEmpty()) mutableListOf()
                else it.split("::").toMutableList()
            }
        }
    }
}

class MainActivity : AppCompatActivity(R.layout.activity_main), SimplePref {

  // now you can create a preference like below
  private val myList by simplePref { mutableListOf("default", "values") }

  // and every time you do some update, call save API immediately
  fun updateList(newItem: String) {
    myList.add(newItem)
    simplePrefSave(::myList)
  }

  // This one will not work
  fun addTwoItems(item1: String, item2: String) {
    myList.add(item1)
    myList.add(item2)
    simplePrefSave(::myList)
  }

  // This one will do just fine
  fun addTwoItems(item1: String, item2: String) {
    myList.add(item1)
    simplePrefSave(::myList)
    myList.add(item2)
    simplePrefSave(::myList)
  }

}

```


Clear Preference
---------------------

Please use provide API like below

```kotlin

  // clear preference (local or global)
  simplePrefClear(::myPref, ::otherPref, ::andSoOnPref)

  // clear all local preferences (e.g. if you call this inside an Activity, all its local preference will be cleared)
  simplePrefClearAllLocal()

  // clear all global preferences defined anywhere
  simplePrefClearAllGlobal()

  // clear ALL (local & global) preferences (e.g. your preference files will be empty)
  simplePrefClearAll()


```


__That's it! May this library ease your Android development task__


License
-------

    Copyright 2019 Achmad Hafid

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

