package com.example.hydratrack.widget // CORRECTED: Package names are conventionally lowercase

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.updateAll
import com.example.hydratrack.data.DataStoreManager.dataStore

class AddGlassAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        // First, update the value in DataStore. The .edit function is a suspend function.
        context.dataStore.edit { prefs ->
            val current = prefs[GoalKeys.CURRENT] ?: 0.0
            // Add 250ml (0.25L) and make sure it doesn't exceed the goal.
            prefs[GoalKeys.CURRENT] = (current + 0.25).coerceAtMost(GoalKeys.GOAL)
        }

        // After the data is saved, tell the GoalWidget to update all its instances.
        // This forces the widget to re-run its provideGlance method, re-read the
        // data from the DataStore, and redraw the UI with the new progress.
        GoalWidget().updateAll(context)
    }
}
