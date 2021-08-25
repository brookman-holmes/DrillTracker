package com.brookmanholmes.drilltracker.presentation.view.util

import android.view.View


fun View.setVisibleOrGone(visible: Boolean) {
    if (visible)
        this.visibility = View.VISIBLE
    else
        this.visibility = View.GONE
}