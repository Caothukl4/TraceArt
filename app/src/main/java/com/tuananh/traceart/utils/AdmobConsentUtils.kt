package com.tuananh.traceart.utils

import android.app.Activity
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun Activity.requestUserConsent(): ConsentInformation {
    // Tạo debug + request parameters nếu cần
    val debugSettings = ConsentDebugSettings.Builder(this)
        .addTestDeviceHashedId("87FD086546B6B815A020819AC08D6D5C")
        .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
        .build()

    val params = ConsentRequestParameters.Builder()
        .setConsentDebugSettings(debugSettings)
        .build()

    val consentInformation = UserMessagingPlatform.getConsentInformation(this)

    // Đầu tiên request thông tin consent info
    // suspend cho đến khi callback hoàn thành
    return suspendCancellableCoroutine { cont ->
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            {
                if (consentInformation.isConsentFormAvailable) {
                    // Nếu form có thể hiển thị, load form tiếp
                    UserMessagingPlatform.loadConsentForm(
                        this,
                        { consentForm ->
                            // Khi load form thành công, show form
                            consentForm.show(this) { showError ->
                                if (showError != null) {
                                    // nếu showError có lỗi hay người dùng đóng form
                                    cont.resume(consentInformation)  // hoặc xử lý lỗi tùy bạn
                                } else {
                                    // Người dùng lựa chọn xong — trả về form hoặc null
                                    cont.resume(consentInformation)
                                }
                            }
                        },
                        { loadError ->
                            // load form lỗi
                            cont.resume(consentInformation)
                        }
                    )
                } else {
                    // Form không cần hiển thị
                    cont.resume(consentInformation)
                }
            },
            { requestError ->
                // request consent info lỗi
                cont.resume(consentInformation)
            }
        )

        cont.invokeOnCancellation {
        }
    }
}
