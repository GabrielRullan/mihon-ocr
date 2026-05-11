package eu.kanade.presentation.more.settings.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import eu.kanade.presentation.more.settings.Preference
import eu.kanade.tachiyomi.data.dictionary.CEDICTManager
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.i18n.stringResource
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

object SettingsOCRScreen : SearchableSettings {

    @ReadOnlyComposable
    @Composable
    override fun getTitleRes() = MR.strings.pref_category_ocr

    @Composable
    override fun getPreferences(): List<Preference> {
        val scope = rememberCoroutineScope()
        val dictionaryRepository =
            remember { Injekt.get<tachiyomi.domain.dictionary.repository.DictionaryRepository>() }
        val okHttpClient = remember { Injekt.get<okhttp3.OkHttpClient>() }
        val context = androidx.compose.ui.platform.LocalContext.current

        val manager = remember { CEDICTManager(context, dictionaryRepository, okHttpClient) }

        var isInstalled by remember { mutableStateOf(false) }
        var statusMessage by remember { mutableStateOf("") }
        var isDownloading by remember { mutableStateOf(false) }

        androidx.compose.runtime.LaunchedEffect(Unit) {
            isInstalled = manager.isDictionaryInstalled()
            statusMessage = if (isInstalled) {
                context.getString(tachiyomi.i18n.MR.strings.pref_dictionary_installed.resourceId)
            } else {
                context.getString(tachiyomi.i18n.MR.strings.pref_dictionary_not_installed.resourceId)
            }
        }

        return listOf(
            Preference.PreferenceGroup(
                title = stringResource(MR.strings.pref_dictionary_title),
                preferenceItems = persistentListOf(
                    Preference.PreferenceItem.TextPreference(
                        title = stringResource(MR.strings.pref_dictionary_title),
                        subtitle = if (isDownloading) {
                            statusMessage
                        } else {
                            stringResource(
                                MR.strings.pref_dictionary_summary,
                            )
                        },
                        onClick = {
                            if (!isDownloading) {
                                scope.launch {
                                    isDownloading = true
                                    try {
                                        manager.installDictionary { progress ->
                                            statusMessage = progress
                                        }
                                        isInstalled = true
                                        statusMessage =
                                            context.getString(
                                                tachiyomi.i18n.MR.strings.pref_dictionary_installed.resourceId,
                                            )
                                    } catch (e: Exception) {
                                        statusMessage = "Error: ${e.message}"
                                    } finally {
                                        isDownloading = false
                                    }
                                }
                            }
                        },
                    ),
                    Preference.PreferenceItem.TextPreference(
                        title = "Status",
                        subtitle = statusMessage,
                    ),
                ),
            ),
        )
    }
}
