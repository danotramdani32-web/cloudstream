package com.lagradost.cloudstream3

import android.content.Context
import com.lagradost.api.setContext
import com.lagradost.cloudstream3.utils.DataStore
import java.lang.ref.WeakReference

/**
 * Backwards compatibility wrapper for old plugins.
 * DO NOT modify unless you know exactly what you're doing.
 */
class AcraApplication {

    companion object {

        private var contextRef: WeakReference<Context>? = null

        internal fun setAppContext(context: Context) {
            contextRef = WeakReference(context)
            setContext(contextRef)
        }

        fun removeKeys(folder: String): Int? {
            return contextRef?.get()?.let {
                DataStore.removeKeys(it, folder)
            }
        }

        fun <T> setKey(path: String, value: T) {
            contextRef?.get()?.let {
                DataStore.setKey(it, path, value)
            }
        }

        fun <T> setKey(folder: String, path: String, value: T) {
            contextRef?.get()?.let {
                DataStore.setKey(it, folder, path, value)
            }
        }

        fun <T : Any> getKey(path: String, defVal: T?): T? {
            return contextRef?.get()?.let {
                DataStore.getKey(it, path, defVal)
            }
        }

        fun <T : Any> getKey(path: String): T? {
            return contextRef?.get()?.let {
                DataStore.getKey(it, path)
            }
        }

        fun <T : Any> getKey(folder: String, path: String): T? {
            return contextRef?.get()?.let {
                DataStore.getKey(it, folder, path)
            }
        }

        fun <T : Any> getKey(folder: String, path: String, defVal: T?): T? {
            return contextRef?.get()?.let {
                DataStore.getKey(it, folder, path, defVal)
            }
        }
    }
}        }

        inline fun <reified T : Any> getKey(path: String, defVal: T?): T? {
            return context?.getKey(path, defVal)
        }

        inline fun <reified T : Any> getKey(path: String): T? {
            return context?.getKey(path)
        }

        inline fun <reified T : Any> getKey(folder: String, path: String):                setContext(WeakReference(appContext))
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
