package com.bloom.wellness.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bloom.wellness.data.model.learnSections
import com.bloom.wellness.ui.components.BloomCard
import com.bloom.wellness.ui.components.ChevronRow
import com.bloom.wellness.ui.components.ScreenHeader
import com.bloom.wellness.ui.theme.BloomCream
import com.bloom.wellness.ui.theme.BloomDimens
import com.bloom.wellness.ui.theme.BloomMaroon

@Composable
fun LearnScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(BloomCream),
        contentPadding = PaddingValues(BloomDimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(BloomDimens.SpaceLg)
    ) {
        item { ScreenHeader("Learn", "Understand PCOS, at your own pace") }

        learnSections.forEach { section ->
            item {
                Text(section.title, style = MaterialTheme.typography.titleLarge, color = BloomMaroon)
            }
            item {
                BloomCard {
                    section.topics.forEach { topic ->
                        ChevronRow(title = topic.question, onClick = {})
                    }
                }
            }
        }

        item { Spacer(Modifier.height(BloomDimens.SpaceLg)) }
    }
}
