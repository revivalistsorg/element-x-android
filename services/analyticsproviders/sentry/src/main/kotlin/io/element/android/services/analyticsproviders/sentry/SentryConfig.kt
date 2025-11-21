/*
 * Copyright (c) 2025 Element Creations Ltd.
 * Copyright 2023-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial.
 * Please see LICENSE files in the repository root for full details.
 */

package io.element.android.services.analyticsproviders.sentry

object SentryConfig {
    const val NAME = "Sentry"    
    const val DSN = "https://61bb06f3ba116a7205d604976b97a3b6@o4508071889076224.ingest.de.sentry.io/4508071897530448"
    // todo: const val DSN = BuildConfig.SENTRY_DSN
    const val ENV_DEBUG = "DEBUG"
    const val ENV_NIGHTLY = "NIGHTLY"
    const val ENV_RELEASE = "RELEASE"
}
