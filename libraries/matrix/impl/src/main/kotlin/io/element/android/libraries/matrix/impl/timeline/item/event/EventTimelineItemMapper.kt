/*
 * Copyright (c) 2023 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.element.android.libraries.matrix.impl.timeline.item.event

import io.element.android.libraries.matrix.api.core.EventId
import io.element.android.libraries.matrix.api.core.UserId
import io.element.android.libraries.matrix.api.timeline.item.TimelineItemDebugInfo
import io.element.android.libraries.matrix.api.timeline.item.event.TimelineItemEventOrigin
import io.element.android.libraries.matrix.api.timeline.item.event.EventReaction
import io.element.android.libraries.matrix.api.timeline.item.event.EventTimelineItem
import io.element.android.libraries.matrix.api.timeline.item.event.LocalEventSendState
import io.element.android.libraries.matrix.api.timeline.item.event.ProfileTimelineDetails
import org.matrix.rustcomponents.sdk.Reaction
import org.matrix.rustcomponents.sdk.EventItemOrigin as RustEventItemOrigin
import org.matrix.rustcomponents.sdk.EventSendState as RustEventSendState
import org.matrix.rustcomponents.sdk.EventTimelineItem as RustEventTimelineItem
import org.matrix.rustcomponents.sdk.EventTimelineItemDebugInfo as RustEventTimelineItemDebugInfo
import org.matrix.rustcomponents.sdk.ProfileDetails as RustProfileDetails

class EventTimelineItemMapper(private val contentMapper: TimelineEventContentMapper = TimelineEventContentMapper()) {

    fun map(eventTimelineItem: RustEventTimelineItem): EventTimelineItem = eventTimelineItem.use {
        EventTimelineItem(
            eventId = it.eventId()?.let(::EventId),
            transactionId = it.transactionId(),
            isEditable = it.isEditable(),
            isLocal = it.isLocal(),
            isOwn = it.isOwn(),
            isRemote = it.isRemote(),
            localSendState = it.localSendState()?.map(),
            reactions = it.reactions().map(),
            sender = UserId(it.sender()),
            senderProfile = it.senderProfile().map(),
            timestamp = it.timestamp().toLong(),
            content = contentMapper.map(it.content()),
            debugInfo = it.debugInfo().map(),
            origin = it.origin()?.map()
        )
    }
}

fun RustProfileDetails.map(): ProfileTimelineDetails {
    return when (this) {
        RustProfileDetails.Pending -> ProfileTimelineDetails.Pending
        RustProfileDetails.Unavailable -> ProfileTimelineDetails.Unavailable
        is RustProfileDetails.Error -> ProfileTimelineDetails.Error(message)
        is RustProfileDetails.Ready -> ProfileTimelineDetails.Ready(
            displayName = displayName,
            displayNameAmbiguous = displayNameAmbiguous,
            avatarUrl = avatarUrl
        )
    }
}

fun RustEventSendState?.map(): LocalEventSendState? {
    return when (this) {
        null -> null
        RustEventSendState.NotSentYet -> LocalEventSendState.NotSentYet
        is RustEventSendState.SendingFailed -> LocalEventSendState.SendingFailed(error)
        is RustEventSendState.Sent -> LocalEventSendState.Sent(EventId(eventId))
        RustEventSendState.Cancelled -> LocalEventSendState.Canceled
    }
}

private fun List<Reaction>?.map(): List<EventReaction> {
    return this?.map {
        EventReaction(
            key = it.key,
            count = it.count.toLong(),
            senderIds = it.senders.map { sender -> UserId(sender.senderId) }
        )
    } ?: emptyList()
}

private fun RustEventTimelineItemDebugInfo.map(): TimelineItemDebugInfo {
    return TimelineItemDebugInfo(
        model = model,
        originalJson = originalJson,
        latestEditedJson = latestEditJson,
    )
}

private fun RustEventItemOrigin.map(): TimelineItemEventOrigin {
    return when (this) {
        RustEventItemOrigin.LOCAL -> TimelineItemEventOrigin.LOCAL
        RustEventItemOrigin.SYNC -> TimelineItemEventOrigin.SYNC
        RustEventItemOrigin.PAGINATION -> TimelineItemEventOrigin.PAGINATION
    }
}
