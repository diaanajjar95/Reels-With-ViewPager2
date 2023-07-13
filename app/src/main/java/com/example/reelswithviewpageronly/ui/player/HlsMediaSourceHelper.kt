package com.example.reelswithviewpageronly.ui.player

import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.LoadErrorHandlingPolicy
import java.io.IOException

object HlsMediaSourceHelper {
    fun isEligibleForFallback(exception: IOException): Boolean {
        if (exception !is HttpDataSource.InvalidResponseCodeException) {
            return false
        }
        val invalidResponseCodeException: HttpDataSource.InvalidResponseCodeException =
            exception as HttpDataSource.InvalidResponseCodeException
        return invalidResponseCodeException.responseCode == 403 || invalidResponseCodeException.responseCode == 404 || invalidResponseCodeException.responseCode == 410 || invalidResponseCodeException.responseCode == 416 || invalidResponseCodeException.responseCode == 500 || invalidResponseCodeException.responseCode == 503 // HTTP 503 Service Unavailable.
    }

    fun setErrorHandlingPolicy() =
        object : LoadErrorHandlingPolicy {
            override fun getFallbackSelectionFor(
                fallbackOptions: LoadErrorHandlingPolicy.FallbackOptions,
                loadErrorInfo: LoadErrorHandlingPolicy.LoadErrorInfo
            ): LoadErrorHandlingPolicy.FallbackSelection? {
                if (!isEligibleForFallback(loadErrorInfo.exception)) {
                    return null
                }
                // Prefer location fallbacks to track fallbacks, when both are available.
                if (fallbackOptions.isFallbackAvailable(LoadErrorHandlingPolicy.FALLBACK_TYPE_LOCATION)) {
                    return LoadErrorHandlingPolicy.FallbackSelection(
                        LoadErrorHandlingPolicy.FALLBACK_TYPE_LOCATION,
                        3000L
                    )
                } else if (fallbackOptions.isFallbackAvailable(LoadErrorHandlingPolicy.FALLBACK_TYPE_TRACK)) {
                    return LoadErrorHandlingPolicy.FallbackSelection(
                        LoadErrorHandlingPolicy.FALLBACK_TYPE_TRACK,
                        3000L
                    )
                }
                return null
            }

            override fun getRetryDelayMsFor(loadErrorInfo: LoadErrorHandlingPolicy.LoadErrorInfo): Long {
                // checking if it is a connectivity issue
                return if (loadErrorInfo.exception is HttpDataSource.HttpDataSourceException) {
                    3000; // Retry every 3 seconds.
                } else {
                    C.TIME_UNSET; // Anything else is surfaced.
                }
            }

            override fun getMinimumLoadableRetryCount(dataType: Int): Int {
                return dataType
            }

        }
}