// Copyright 2019, Google LLC, Christopher Banes and the Tivi project contributors
// SPDX-License-Identifier: Apache-2.0

package app.tivi.settings.developer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.tivi.screens.DevLogScreen
import app.tivi.screens.DevSettingsScreen
import app.tivi.settings.TiviPreferences
import app.tivi.settings.toggle
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class DevSettingsUiPresenterFactory(
    private val presenterFactory: (Navigator) -> DevSettingsPresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? = when (screen) {
        is DevSettingsScreen -> presenterFactory(navigator)
        else -> null
    }
}

@Inject
class DevSettingsPresenter(
    @Assisted private val navigator: Navigator,
    private val preferences: TiviPreferences,
) : Presenter<DevSettingsUiState> {

    @Composable
    override fun present(): DevSettingsUiState {
        val hideArtwork by preferences.observeDeveloperHideArtwork().collectAsState(false)

        fun eventSink(event: DevSettingsUiEvent) {
            when (event) {
                DevSettingsUiEvent.NavigateUp -> navigator.pop()
                DevSettingsUiEvent.NavigateLog -> navigator.goTo(DevLogScreen)
                DevSettingsUiEvent.ToggleHideArtwork -> preferences::developerHideArtwork.toggle()
            }
        }

        return DevSettingsUiState(
            hideArtwork = hideArtwork,
            eventSink = ::eventSink,
        )
    }
}
