package eu.kanade.presentation.reader.settings

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.kanade.tachiyomi.ui.reader.setting.ReaderSettingsScreenModel
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.CheckboxItem
import tachiyomi.presentation.core.components.HeadingItem
import tachiyomi.presentation.core.i18n.stringResource

@Composable
internal fun ColumnScope.OCRPage(screenModel: ReaderSettingsScreenModel) {
    HeadingItem(MR.strings.pref_category_ocr)

    CheckboxItem(
        label = stringResource(MR.strings.pref_ocr_mode),
        pref = screenModel.preferences.ocrEnabled,
    )

    Text(
        text = stringResource(MR.strings.pref_ocr_mode_summary),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
