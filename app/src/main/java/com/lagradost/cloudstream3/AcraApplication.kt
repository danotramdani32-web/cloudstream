package com.lagradost.cloudstream3

import android.content.Context
import android.os.Build
import com.lagradost.api.setContext
import com.lagradost.cloudstream3.utils.DataStore.getKey
import com.lagradost.cloudstream3.utils.DataStore.removeKeys
import com.lagradost.cloudstream3.utils.DataStore.setKey
import java.lang.ref.WeakReference

/**
 * Compatibility wrapper for old plugins.
 * MUST stay lightweight (Android 6 safe).
 */
class AcraApplication {

    companion object {

        @Volatile
        private var contextRef: WeakReference<Context>? = null

        /**
         * Safe context getter (can be null)
         */
        val context: Context?
            get() = contextRef?.get()

        /**
         * MUST be called from CloudStreamApp.onCreate()
         * Do NOT add heavy logic here
         */
        internal fun init(appContext: Context) {
            contextRef = WeakReference(appContext)

            // Guard Android 6 and below
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                setContext(WeakReference(appContext))
            }
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
    }
}			setContext(WeakReference(value))
		}

		/*@Deprecated(
		    message = "AcraApplication is deprecated, use CloudStreamApp instead",
		    replaceWith = ReplaceWith("com.lagradost.cloudstream3.CloudStreamApp.removeKeys(folder)"),
		    level = DeprecationLevel.WARNING
		)*/
		fun removeKeys(folder: String): Int? {
            return context?.removeKeys(folder)
        }

		/*@Deprecated(
		    message = "AcraApplication is deprecated, use CloudStreamApp instead",
		    replaceWith = ReplaceWith("com.lagradost.cloudstream3.CloudStreamApp.setKey(path, value)"),
		    level = DeprecationLevel.WARNING
		)*/
		fun <T> setKey(path: String, value: T) {
			context?.setKey(path, value)
		}

		/*@Deprecated(
		    message = "AcraApplication is deprecated, use CloudStreamApp instead",
		    replaceWith = ReplaceWith("com.lagradost.cloudstream3.CloudStreamApp.setKey(folder, path, value)"),
		    level = DeprecationLevel.WARNING
		)*/
		fun <T> setKey(folder: String, path: String, value: T) {
			context?.setKey(folder, path, value)
		}

		/*@Deprecated(
		    message = "AcraApplication is deprecated, use CloudStreamApp instead",
		    replaceWith = ReplaceWith("com.lagradost.cloudstream3.CloudStreamApp.getKey(path, defVal)"),
		    level = DeprecationLevel.WARNING
		)*/
		inline fun <reified T : Any> getKey(path: String, defVal: T?): T? {
			return context?.getKey(path, defVal)
		}

		/*@Deprecated(
		    message = "AcraApplication is deprecated, use CloudStreamApp instead",
		    replaceWith = ReplaceWith("com.lagradost.cloudstream3.CloudStreamApp.getKey(path)"),
		    level = DeprecationLevel.WARNING
		)*/
		inline fun <reified T : Any> getKey(path: String): T? {
			return context?.getKey(path)
		}

		/*@Deprecated(
		    message = "AcraApplication is deprecated, use CloudStreamApp instead",
		    replaceWith = ReplaceWith("com.lagradost.cloudstream3.CloudStreamApp.getKey(folder, path)"),
		    level = DeprecationLevel.WARNING
		)*/
		inline fun <reified T : Any> getKey(folder: String, path: String): T? {
			return context?.getKey(folder, path)
		}

		/*@Deprecated(
		    message = "AcraApplication is deprecated, use CloudStreamApp instead",
		    replaceWith = ReplaceWith("com.lagradost.cloudstream3.CloudStreamApp.getKey(folder, path, defVal)"),
		    level = DeprecationLevel.WARNING
		)*/
		inline fun <reified T : Any> getKey(folder: String, path: String, defVal: T?): T? {
			return context?.getKey(folder, path, defVal)
		}
	}
}
