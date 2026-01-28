package com.lagradost.cloudstream3

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import com.lagradost.api.setContext
import com.lagradost.cloudstream3.utils.ImageLoader.buildImageLoader
import java.lang.ref.WeakReference

@Prerelease
class CloudStreamApp : Application(), SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
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
        private var _context: WeakReference<Context>? = null

        var context: Context?
            get() = _context?.get()
            private set(value) {
                if (value != null) {
                    _context = WeakReference(value)
                    setContext(WeakReference(value))
                }
            }
    }
}
