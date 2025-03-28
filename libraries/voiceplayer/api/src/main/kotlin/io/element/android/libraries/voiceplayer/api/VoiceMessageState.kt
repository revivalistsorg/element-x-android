/*
 * Copyright 2023, 2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.libraries.voiceplayer.api

data class VoiceMessageState(
    val button: Button,
    val progress: Float,
    val time: String,
    val showCursor: Boolean,
    val eventSink: (event: VoiceMessageEvents) -> Unit,
) {
    enum class Button {
        Play,
        Pause,
        Downloading,
        Retry,
        Disabled,
    }
}
