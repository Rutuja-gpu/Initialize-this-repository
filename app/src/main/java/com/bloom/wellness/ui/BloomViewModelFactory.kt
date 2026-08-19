package com.bloom.wellness.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.bloom.wellness.BloomApplication
import com.bloom.wellness.data.BloomPreferencesRepository

/** Small factory so ViewModels can take [BloomPreferencesRepository] via a constructor, no DI framework needed. */
class BloomViewModelFactory(
    private val repository: BloomPreferencesRepository,
    private val creator: (BloomPreferencesRepository) -> ViewModel
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = creator(repository) as T
}

@Composable
fun bloomRepository(): BloomPreferencesRepository {
    val app = LocalContext.current.applicationContext as BloomApplication
    return app.preferencesRepository
}

@Composable
inline fun <reified VM : ViewModel> bloomViewModel(
    noinline creator: (BloomPreferencesRepository) -> VM
): VM {
    val repository = bloomRepository()
    return viewModel(factory = BloomViewModelFactory(repository, creator))
}
