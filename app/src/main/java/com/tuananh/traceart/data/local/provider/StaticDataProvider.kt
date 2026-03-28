package com.tuananh.traceart.data.local.provider

import com.tuananh.traceart.BuildConfig
import com.tuananh.traceart.R
import com.tuananh.traceart.domain.model.Language

object StaticDataProvider {
    fun getLanguages(): List<Language> = listOf(
        Language(R.drawable.img_flag_en_us, "English", "en-US"),
        Language(R.drawable.img_flag_es, "Español", "es"),
        Language(R.drawable.img_flag_pt_pt, "Português", "pt-PT"),
        Language(R.drawable.img_flag_hi, "हिंदी", "hi"),
        Language(R.drawable.img_flag_de, "Deutsch", "de"),
        Language(R.drawable.img_flag_ja, "日本語", "ja"),
        Language(R.drawable.img_flag_vi, "Tiếng Việt", "vi"),
        Language(R.drawable.img_flag_fr, "Français", "fr"),
        Language(R.drawable.img_flag_zh, "中文", "zh"),
        Language(R.drawable.img_flag_ru, "Русский", "ru"),
        Language(R.drawable.img_flag_in, "Bahasa Indonesia", "in"),
        Language(R.drawable.img_flag_en_ph, "English (Philippines)", "en-PH"),
        Language(R.drawable.img_flag_bn, "বাংলা", "bn"),
        Language(R.drawable.img_flag_pt_br, "Português (Brasil)", "pt-BR"),
        Language(R.drawable.img_flag_af_za, "Afrikaans", "af-ZA"),
        Language(R.drawable.img_flag_en_ca, "English (Canada)", "en-CA"),
        Language(R.drawable.img_flag_en_gb, "English (UK)", "en-GB"),
        Language(R.drawable.img_flag_ko, "한국어", "ko"),
        Language(R.drawable.img_flag_nl, "Nederlands", "nl"),
    )

    object AdmobIds {
        val APP_ID = "ca-app-pub-5478191690154275~3758471780"
        val NATIVE_HOME = "ca-app-pub-5478191690154275/4082950930"
        val Native_Onboarding_1 = "ca-app-pub-5478191690154275/5723335669"
        val Native_Onboarding_2 = "ca-app-pub-5478191690154275/8102086998"
        val Native_Onboarding_3 = "ca-app-pub-5478191690154275/3811488194"
        val Native_Onboarding_Full = "ca-app-pub-5478191690154275/7060468066"
        val INTER_HOME = "ca-app-pub-5478191690154275/5747386397"
        val BANNER_BOOK_DETAIL = "ca-app-pub-5478191690154275/2268689278"

        val TEST_NATIVE = "ca-app-pub-3940256099942544/2247696110"
        val TEST_INTER = "ca-app-pub-3940256099942544/1033173712"
        val TEST_BANNER = "ca-app-pub-3940256099942544/9214589741"

        fun getNativeHome() = if (BuildConfig.DEBUG) TEST_NATIVE else NATIVE_HOME
        fun getInterHome() = if (BuildConfig.DEBUG) TEST_INTER else INTER_HOME
        fun getBannerHome() = if (BuildConfig.DEBUG) TEST_BANNER else BANNER_BOOK_DETAIL
        fun getNativeOnBoarding1() =  if (BuildConfig.DEBUG) TEST_NATIVE else Native_Onboarding_1
        fun getNativeOnBoarding2() =  if (BuildConfig.DEBUG) TEST_NATIVE else Native_Onboarding_2
        fun getNativeOnBoarding3() =  if (BuildConfig.DEBUG) TEST_NATIVE else Native_Onboarding_3

    }
}