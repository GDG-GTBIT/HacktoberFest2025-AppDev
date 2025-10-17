package com.example.hydratrack.widget

import android.R
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.layout.*
import androidx.glance.material3.*
import androidx.glance.text.Text
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.Button
import androidx.glance.appwidget.action.actionRunCallback
import com.example.hydratrack.widget.AddGlassAction
import com.example.hydratrack.data.DataStoreManager.dataStore
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import kotlinx.coroutines.flow.first

object GoalKeys {
    val CURRENT = doublePreferencesKey("current_liters")
    const val GOAL = 5.0
}

class GoalWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Read DataStore value
        val currentLiters = context.dataStore.data.first()[GoalKeys.CURRENT] ?: 0.0

        // Update widget state as well
        updateAppWidgetState(context, id) {
            prefs -> prefs[GoalKeys.CURRENT] = currentLiters
        }

        provideContent {
            GoalWidgetContent(currentLiters)
        }
    }
}

@Composable
fun GoalWidgetContent(currentLiters: Double) {
    val progress = (currentLiters / GoalKeys.GOAL).coerceIn(0.0, 1.0)

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.Vertical.CenterVertically,
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
        Text(text = "Hydration Goal 💧",
            style = TextStyle(color = ColorProvider(R.color.white))
        )

        Spacer(GlanceModifier.height(8.dp))

        Text(text = "${"%.2f".format(currentLiters)}L / ${GoalKeys.GOAL}L",
            style = TextStyle(color = ColorProvider(R.color.white))
        )

        Spacer(GlanceModifier.height(8.dp))

        LinearProgressIndicator(progress = progress.toFloat())

        Spacer(GlanceModifier.height(12.dp))

        Button(
            text = "+ Add Glass (250ml)",
            onClick = actionRunCallback<AddGlassAction>()
        )
    }
}

