package com.lagradost.cloudstream3

import android.content.Context
import java.lang.ref.WeakReference

/**
 * Legacy wrapper for backward compatibility with old plugins.
 * DO NOT add logic here.
 */
class AcraApplication {
    companion object {
        private var _context: WeakReference<Context>? = null

        var context: Context?
            get() = _context?.get()
            internal set(value) {
                if (value != null) {
                    _context = WeakReference(value)
                }
            }
    }
}
