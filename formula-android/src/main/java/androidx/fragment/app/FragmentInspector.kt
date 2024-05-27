package androidx.fragment.app

import android.view.View

fun Fragment.isHeadless(): Boolean =
    mContainerId in intArrayOf(View.NO_ID, 0)
