package com.lagradost.cloudstream3

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.lagradost.api.setContext
import com.lagradost.cloudstream3.plugins.PluginManager
import com.lagradost.cloudstream3.ui.settings.Globals.EMULATOR
import com.lagradost.cloudstream3.ui.settings.Globals.TV
import com.lagradost.cloudstream3.ui.settings.Globals.isLayout
import com.lagradost.cloudstream3.utils.AppContextUtils.openBrowser
import com.lagradost.cloudstream3.utils.DataStore
import com.lagradost.cloudstream3.utils.ImageLoader.buildImageLoader
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintStream
import java.lang.ref.WeakReference
import kotlin.system.exitProcess

/* =========================
   GLOBAL EXCEPTION HANDLER
   ========================= */

class ExceptionHandler(
    private val errorFile: File,
    private val onError: () -> Unit
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, error: Throwable) {
        try {
            val threadId =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA)
                    thread.threadId()
                else
                    @Suppress("DEPRECATION") thread.id

            PrintStream(errorFile).use { ps ->
                ps.println("Currently loading extension: ${PluginManager.currentlyLoading ?: "none"}")
                ps.println("Fatal exception on thread ${thread.name} ($threadId)")
                error.printStackTrace(ps)
            }
        } catch (_: FileNotFoundException) {
        }

        try {
            onError()
        } catch (_: Exception) {
        }

        exitProcess(1)
    }
}

/* =========================
   APPLICATION CLASS
   ========================= */

@Prerelease
class CloudStreamApp : Application(), SingletonImageLoader.Factory {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Companion.context = base
    }

    override fun onCreate() {
        super.onCreate()

        val handler = ExceptionHandler(filesDir.resolve("last_error")) {
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                startActivity(Intent.makeRestartActivityTask(intent.component))
            }
        }

        exceptionHandler = handler
        Thread.setDefaultUncaughtExceptionHandler(handler)
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return buildImageLoader(applicationContext)
    }

    companion object {

        var exceptionHandler: ExceptionHandler? = null

        private var _context: WeakReference<Context>? = null

        var context: Context?
            get() = _context?.get()
            private set(value) {
                if (value != null) {
                    _context = WeakReference(value)
                    setContext(WeakReference(value))
                }
            }

        /* ========= HELPERS ========= */

        tailrec fun Context.getActivity(): Activity? =
            when (this) {
                is Activity -> this
                is ContextWrapper -> baseContext.getActivity()
                else -> null
            }

        /* ========= DATASTORE WRAPPERS ========= */

        fun <T : Any> setKey(path: String, value: T) {
            context?.let { DataStore.setKey(it, path, value) }
        }

        fun <T : Any> setKey(folder: String, path: String, value: T) {
            context?.let { DataStore.setKey(it, folder, path, value) }
        }

        fun <T : Any> getKey(path: String, defVal: T? = null): T? =
            context?.let { DataStore.getKey(it, path, defVal) }

        fun <T : Any> getKey(folder: String, path: String, defVal: T? = null): T? =
            context?.let { DataStore.getKey(it, folder, path, defVal) }

        fun removeKey(path: String) {
            context?.let { DataStore.removeKey(it, path) }
        }

        fun removeKey(folder: String, path: String) {
            context?.let { DataStore.removeKey(it, folder, path) }
        }

        fun removeKeys(folder: String): Int? =
            context?.let { DataStore.removeKeys(it, folder) }

        /* ========= BROWSER ========= */

        fun openBrowser(
            url: String,
            fallbackWebView: Boolean = false,
            fragment: Fragment? = null
        ) {
            context?.openBrowser(url, fallbackWebView, fragment)
        }

        fun openBrowser(url: String, activity: FragmentActivity?) {
            openBrowser(
                url,
                isLayout(TV or EMULATOR),
                activity?.supportFragmentManager?.fragments?.lastOrNull()
            )
        }
    }
}
