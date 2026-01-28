package com.lagradost.cloudstream3.utils

import android.content.Context
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.APIHolder.unixTimeMS
import com.lagradost.cloudstream3.CloudStreamApp.Companion.context
import com.lagradost.cloudstream3.CloudStreamApp.Companion.getKey
import com.lagradost.cloudstream3.CloudStreamApp.Companion.getKeyClass
import com.lagradost.cloudstream3.CloudStreamApp.Companion.getKeys
import com.lagradost.cloudstream3.CloudStreamApp.Companion.removeKey
import com.lagradost.cloudstream3.CloudStreamApp.Companion.removeKeys
import com.lagradost.cloudstream3.CloudStreamApp.Companion.setKey
import com.lagradost.cloudstream3.CloudStreamApp.Companion.setKeyClass
import com.lagradost.cloudstream3.CommonActivity.showToast
import com.lagradost.cloudstream3.DubStatus
import com.lagradost.cloudstream3.EpisodeResponse
import com.lagradost.cloudstream3.MainActivity
import com.lagradost.cloudstream3.R
import com.lagradost.cloudstream3.Score
import com.lagradost.cloudstream3.SearchQuality
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.syncproviders.AccountManager
import com.lagradost.cloudstream3.syncproviders.SyncAPI
import com.lagradost.cloudstream3.ui.WatchType
import com.lagradost.cloudstream3.ui.library.ListSorting
import com.lagradost.cloudstream3.ui.player.ExtractorUri
import com.lagradost.cloudstream3.ui.player.NEXT_WATCH_EPISODE_PERCENTAGE
import com.lagradost.cloudstream3.ui.result.EpisodeSortType
import com.lagradost.cloudstream3.ui.result.ResultEpisode
import com.lagradost.cloudstream3.ui.result.VideoWatchState
import com.lagradost.cloudstream3.utils.AppContextUtils.filterProviderByPreferredMedia
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class UserPreferenceDelegate<T : Any>(
    private val key: String,
    private val default: T
) {
    private val klass: KClass<out T> = default::class
    private val realKey get() = "${DataStoreHelper.currentAccount}/$key"

    operator fun getValue(self: Any?, property: KProperty<*>): T {
        return getKeyClass(realKey, klass.java) ?: default
    }

    // 🔧 FIX: Unit? → Unit
    operator fun setValue(self: Any?, property: KProperty<*>, t: T?) {
        val ctx = context ?: return
        if (t == null) {
            ctx.removeKey(realKey)
        } else {
            setKeyClass(realKey, t)
        }
    }
}

object DataStoreHelper {

    private var searchPreferenceProvidersStrings: List<String> by UserPreferenceDelegate(
        "search_pref_providers",
        List(0) { "" }
    )

    var searchPreferenceProviders: List<String>
        get() {
            val ret = searchPreferenceProvidersStrings
            if (ret.isNotEmpty()) return ret

            val ctx = context ?: return emptyList()
            return ctx.filterProviderByPreferredMedia().map { it.name }
        }
        set(value) {
            searchPreferenceProvidersStrings = value
        }

    private fun serializeTv(data: List<TvType>): List<String> = data.map { it.name }

    private fun deserializeTv(data: List<String>): List<TvType> {
        return data.mapNotNull { name ->
            TvType.values().firstOrNull { it.name == name }
        }
    }

    private var searchPreferenceTagsStrings: List<String> by UserPreferenceDelegate(
        "search_pref_tags",
        listOf(TvType.Movie, TvType.TvSeries).map { it.name }
    )

    var searchPreferenceTags: List<TvType>
        get() = deserializeTv(searchPreferenceTagsStrings)
        set(value) {
            searchPreferenceTagsStrings = serializeTv(value)
        }

    private var homePreferenceStrings: List<String> by UserPreferenceDelegate(
        "home_pref_homepage",
        listOf(TvType.Movie, TvType.TvSeries).map { it.name }
    )

    var homePreference: List<TvType>
        get() = deserializeTv(homePreferenceStrings)
        set(value) {
            homePreferenceStrings = serializeTv(value)
        }

    const val TAG = "data_store_helper"
    var selectedKeyIndex by PreferenceDelegate("$TAG/account_key_index", 0)
    val currentAccount: String get() = selectedKeyIndex.toString()

    fun deleteAllResumeStateIds() {
        val ctx = context ?: return
        ctx.removeKeys("$currentAccount/result_resume_watching_2")
    }

    fun removeLastWatched(parentId: Int?) {
        if (parentId == null) return
        val ctx = context ?: return
        ctx.removeKey("$currentAccount/result_resume_watching_2", parentId.toString())
    }

    fun setViewPos(id: Int?, pos: Long, dur: Long) {
        if (id == null || dur < 30_000) return
        setKey("$currentAccount/video_pos_dur", id.toString(), PosDur(pos, dur))
    }

    data class PosDur(
        @JsonProperty("position") val position: Long,
        @JsonProperty("duration") val duration: Long
    )
}
