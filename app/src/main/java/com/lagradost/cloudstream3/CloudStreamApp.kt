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
import com.lagradost.cloudstream3.utils.DataStore.getKey
import com.lagradost.cloudstream3.utils.DataStore.getKeys
import com.lagradost.cloudstream3.utils.DataStore.removeKey
import com.lagradost.cloudstream3.utils.DataStore.removeKeys
import com.lagradost.cloudstream3.utils.DataStore.setKey
import com.lagradost.cloudstream3.utils.ImageLoader.buildImageLoader
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintStream
import java.lang.ref.WeakReference
import kotlin.system.exitProcess

// =====================
// GLOBAL EXCEPTION HANDLER
// =====================
class ExceptionHandler(
    private val errorFile: File,
    private val onError: (() -> Unit)
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, error: Throwable) {
        try {
            val threadId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                thread.id
            } else {
                @Suppress("DEPRECATION")
                thread.id
            }

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

// =====================
// APPLICATION
// =====================
@Prerelease
class CloudStreamApp : Application(), SingletonImageLoader.Factory {

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

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (base != null) {
            context = base
            AcraApplication.context = base
        }
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

        // =====================
        // CONTEXT HELPERS
        // =====================
        tailrec fun Context.getActivity(): Activity? {
            return when (this) {
                is Activity -> this
                is ContextWrapper -> baseContext.getActivity()
                else -> null
            }
        }

        // =====================
        // DATASTORE WRAPPERS
        // =====================
        fun <T : Any> getKeyClass(path: String, valueType: Class<T>): T? {
            return context?.getKey(path, valueType)
        }

        fun <T : Any> setKeyClass(path: String, value: T) {
            context?.setKey(path, value)
        }

        fun removeKeys(folder: String): Int? {
            return context?.removeKeys(folder)
        }

        fun <T> setKey(path: String, value: T) {
            context?.setKey(path, value)
        }

        fun <T> setKey(folder: String, path: String, value: T) {
            context?.setKey(folder, path, value)
        }

        inline fun <reified T : Any> getKey(path: String, defVal: T?): T? {
            return context?.getKey(path, defVal)
        }

        inline fun <reified T : Any> getKey(path: String): T? {
            return context?.getKey(path)
        }

        inline fun <reified T : Any> getKey(folder: String, path: String): T? {
            return context?.getKey(folder, path)
        }

        inline fun <reified T : Any> getKey(
            folder: String,
            path: String,
            defVal: T?
        ): T? {
            return context?.getKey(folder, path, defVal)
        }

        fun getKeys(folder: String): List<String>? {
            return context?.getKeys(folder)
        }

        fun removeKey(folder: String, path: String) {
            context?.removeKey(folder, path)
        }

        fun removeKey(path: String) {
            context?.removeKey(path)
        }

        // =====================
        // BROWSER HELPERS
        // =====================
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
}                it.println("Currently loading extension: ${PluginManager.currentlyLoading ?: "none"}")
                it.println("Fatal exception on thread ${thread.name} ($id)")
                error.printStackTrace(it)
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

@Prerelease
class CloudStreamApp : Application(), SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()

        ExceptionHandler(filesDir.resolve("last_error")) {
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            startActivity(Intent.makeRestartActivityTask(intent!!.component))
        }.also {
            exceptionHandler = it
            Thread.setDefaultUncaughtExceptionHandler(it)
        }
    }

    // ✅ FIX UTAMA ADA DI SINI
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        base?.let {
            context = it
            AcraApplication.init(it) // <-- WAJIB, JANGAN DIHAPUS
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return buildImageLoader(applicationContext)
    }

    companion object {

        var exceptionHandler: ExceptionHandler? = null

        tailrec fun Context.getActivity(): Activity? =
            when (this) {
                is Activity -> this
                is ContextWrapper -> baseContext.getActivity()
                else -> null
            }

        private var _context: WeakReference<Context>? = null

        var context: Context?
            get() = _context?.get()
            private set(value) {
                _context = WeakReference(value)
                setContext(WeakReference(value))
            }

        fun <T : Any> getKeyClass(path: String, valueType: Class<T>): T? =
            context?.getKey(path, valueType)

        fun <T : Any> setKeyClass(path: String, value: T) =
            context?.setKey(path, value)

        fun removeKeys(folder: String): Int? =
            context?.removeKeys(folder)

        fun <T> setKey(path: String, value: T) =
            context?.setKey(path, value)

        fun <T> setKey(folder: String, path: String, value: T) =
            context?.setKey(folder, path, value)

        inline fun <reified T : Any> getKey(path: String, defVal: T?): T? =
            context?.getKey(path, defVal)

        inline fun <reified T : Any> getKey(path: String): T? =
            context?.getKey(path)

        inline fun <reified T : Any> getKey(folder: String, path: String): T? =
            context?.getKey(folder, path)

        inline fun <reified T : Any> getKey(
            folder: String,
            path: String,
            defVal: T?
        ): T? = context?.getKey(folder, path, defVal)

        fun getKeys(folder: String): List<String>? =
            context?.getKeys(folder)

        fun removeKey(folder: String, path: String) =
            context?.removeKey(folder, path)

        fun removeKey(path: String) =
            context?.removeKey(path)

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
}import kotlin.system.exitProcess

class ExceptionHandler(
    val errorFile: File,
    val onError: (() -> Unit)
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, error: Throwable) {
        try {
            val threadId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
                thread.threadId()
            } else {
                @Suppress("DEPRECATION")
                thread.id
            }

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

@Prerelease
class CloudStreamApp : Application(), SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()
        // If we want to initialize Coil as early as possible, maybe when
        // loading an image or GIF in a splash screen activity.
        // buildImageLoader(applicationContext)

        ExceptionHandler(filesDir.resolve("last_error")) {
            val intent = context!!.packageManager.getLaunchIntentForPackage(context!!.packageName)
            startActivity(Intent.makeRestartActivityTask(intent!!.component))
        }.also {
            exceptionHandler = it
            Thread.setDefaultUncaughtExceptionHandler(it)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        context = base
        // This can be removed without deprecation after next stable
        AcraApplication.context = context
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        // Coil module will be initialized globally when first loadImage() is invoked.
        return buildImageLoader(applicationContext)
    }

    companion object {
        var exceptionHandler: ExceptionHandler? = null

        /** Use to get Activity from Context. */
        tailrec fun Context.getActivity(): Activity? {
            return when (this) {
                is Activity -> this
                is ContextWrapper -> baseContext.getActivity()
                else -> null
            }
        }

        private var _context: WeakReference<Context>? = null
        var context
            get() = _context?.get()
            private set(value) {
                _context = WeakReference(value)
                setContext(WeakReference(value))
            }

        fun <T : Any> getKeyClass(path: String, valueType: Class<T>): T? {
            return context?.getKey(path, valueType)
        }

        fun <T : Any> setKeyClass(path: String, value: T) {
            context?.setKey(path, value)
        }

        fun removeKeys(folder: String): Int? {
            return context?.removeKeys(folder)
        }

        fun <T> setKey(path: String, value: T) {
            context?.setKey(path, value)
        }

        fun <T> setKey(folder: String, path: String, value: T) {
            context?.setKey(folder, path, value)
        }

        inline fun <reified T : Any> getKey(path: String, defVal: T?): T? {
            return context?.getKey(path, defVal)
        }

        inline fun <reified T : Any> getKey(path: String): T? {
            return context?.getKey(path)
        }

        inline fun <reified T : Any> getKey(folder: String, path: String): T? {
            return context?.getKey(folder, path)
        }

        inline fun <reified T : Any> getKey(folder: String, path: String, defVal: T?): T? {
            return context?.getKey(folder, path, defVal)
        }

        fun getKeys(folder: String): List<String>? {
            return context?.getKeys(folder)
        }

        fun removeKey(folder: String, path: String) {
            context?.removeKey(folder, path)
        }

        fun removeKey(path: String) {
            context?.removeKey(path)
        }

        /** If fallbackWebView is true and a fragment is supplied then it will open a WebView with the URL if the browser fails. */
        fun openBrowser(url: String, fallbackWebView: Boolean = false, fragment: Fragment? = null) {
            context?.openBrowser(url, fallbackWebView, fragment)
        }

        /** Will fall back to WebView if in TV or emulator layout. */
        fun openBrowser(url: String, activity: FragmentActivity?) {
            openBrowser(
                url,
                isLayout(TV or EMULATOR),
                activity?.supportFragmentManager?.fragments?.lastOrNull()
            )
        }
    }
}
