package io.github.seyud.weave.ui.module.state

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.content.edit
import io.github.seyud.weave.ui.module.ModuleUiState
import io.github.seyud.weave.ui.module.ModuleViewModel

private const val MODULE_SORT_ENABLED_FIRST_KEY = "module_sort_enabled_first"
private const val MODULE_SORT_UPDATE_FIRST_KEY = "module_sort_update_first"
private const val MODULE_SORT_EXECUTABLE_FIRST_KEY = "module_sort_executable_first"

internal class ModuleSortPreferences(
    private val preferences: SharedPreferences,
) {
    fun restore(viewModel: ModuleViewModel) {
        viewModel.restoreSortOptions(
            sortEnabledFirst = preferences.getBoolean(MODULE_SORT_ENABLED_FIRST_KEY, false),
            sortUpdateFirst = preferences.getBoolean(MODULE_SORT_UPDATE_FIRST_KEY, false),
            sortExecutableFirst = preferences.getBoolean(MODULE_SORT_EXECUTABLE_FIRST_KEY, false),
        )
    }

    fun persist(uiState: ModuleUiState) {
        preferences.edit {
            putBoolean(MODULE_SORT_ENABLED_FIRST_KEY, uiState.sortEnabledFirst)
            putBoolean(MODULE_SORT_UPDATE_FIRST_KEY, uiState.sortUpdateFirst)
            putBoolean(MODULE_SORT_EXECUTABLE_FIRST_KEY, uiState.sortExecutableFirst)
        }
    }
}

@Composable
internal fun rememberModuleSortPreferences(
    context: Context,
): ModuleSortPreferences {
    return remember(context) {
        ModuleSortPreferences(
            preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE),
        )
    }
}
