// DO NOT EDIT — generated
package tv.own.owntv.mobile.features.aisubtitle

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import org.koin.compose.koinInject
import tv.own.owntv.mobile.R
import tv.own.owntv.mobile.ui.components.MobileBottomSheet
import tv.own.owntv.mobile.ui.components.SheetScroll
import tv.own.owntv.mobile.ui.stage.LocalStagePalette
import tv.own.owntv.mobile.ui.stage.StageGroupLabel
import tv.own.owntv.mobile.ui.stage.StageMenuItem
import tv.own.owntv.mobile.ui.stage.StageSettingRow
import tv.own.owntv.mobile.ui.stage.StageSheetHeader
import tv.own.owntv.mobile.ui.theme.LocalAccentOnVideo
import tv.own.owntv.player.PlaybackEngine

/**
 * AI 实时字幕手机端屏幕叠加层 ([AiSubtitleOverlay])。
 */
@UnstableApi
@Composable
fun AiSubtitleOverlay(
    modifier: Modifier = Modifier,
    sizeScale: Float = 1f,
    inPip: Boolean = false,
    controller: AiSubtitleController = koinInject(),
) {
    val state by controller.state.collectAsStateWithLifecycle()
    val cue by controller.activeCue.collectAsStateWithLifecycle()
    val config by controller.configStore.config.collectAsStateWithLifecycle()

    if (state is AiSubtitleState.Off) return

    val scale = config.fontSizeScale * sizeScale * (if (inPip) 0.7f else 1.0f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = (if (inPip) 8.dp else 24.dp) * sizeScale,
                vertical = (if (inPip) 8.dp else 24.dp) * sizeScale,
            ),
    ) {
        // 1. 准备阶段倒计时横幅
        when (val s = state) {
            is AiSubtitleState.Preparing -> {
                if (!inPip) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 16.dp * sizeScale)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xD90D1821))
                            .padding(horizontal = 16.dp * sizeScale, vertical = 8.dp * sizeScale),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.player_ai_subtitle_preparing, s.secondsLeft),
                            style = TextStyle(
                                color = Color.White,
                                fontSize = (14 * sizeScale).sp,
                                fontWeight = FontWeight.SemiBold,
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
            is AiSubtitleState.Error -> {
                if (!inPip) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 16.dp * sizeScale)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xD93B1219))
                            .padding(horizontal = 16.dp * sizeScale, vertical = 8.dp * sizeScale),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.player_ai_subtitle_error_prefix, s.message),
                            style = TextStyle(
                                color = Color(0xFFFFB4AB),
                                fontSize = (13 * sizeScale).sp,
                                fontWeight = FontWeight.Medium,
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
            else -> Unit
        }

        // 2. 实时双语/中文翻译字幕卡片
        val active = cue
        if (active != null && (active.translatedText.isNotBlank() || active.originalText.isNotBlank())) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = (if (inPip) 4.dp else 16.dp) * sizeScale)
                    .widthIn(max = (if (inPip) 320.dp else 680.dp) * sizeScale)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black.copy(alpha = 0.75f))
                    .padding(
                        horizontal = (if (inPip) 8.dp else 16.dp) * sizeScale,
                        vertical = (if (inPip) 4.dp else 8.dp) * sizeScale,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (config.showOriginal && active.originalText.isNotBlank() && !inPip) {
                    Text(
                        text = active.originalText,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            color = Color(0xFF99F6E4),
                            fontSize = (14f * scale).sp,
                            lineHeight = (18f * scale).sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Normal,
                            shadow = Shadow(color = Color.Black, offset = Offset(0f, 1f), blurRadius = 3f),
                        ),
                    )
                    if (active.translatedText.isNotBlank()) {
                        Spacer(Modifier.height(2.dp * sizeScale))
                    }
                }
                if (active.translatedText.isNotBlank()) {
                    Text(
                        text = active.translatedText,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = (if (inPip) 13f else 18f * scale).sp,
                            lineHeight = (if (inPip) 16f else 24f * scale).sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.SemiBold,
                            shadow = Shadow(color = Color.Black, offset = Offset(0f, 1.5f), blurRadius = 4f),
                        ),
                    )
                }
            }
        }
    }
}

/**
 * 手机端 AI 实时字幕设置面板 ([AiSubtitleSettingsSheet])。
 */
@UnstableApi
@Composable
fun AiSubtitleSettingsSheet(
    player: PlaybackEngine,
    streamUrl: String? = null,
    onDismiss: () -> Unit,
    controller: AiSubtitleController = koinInject(),
) {
    val state by controller.state.collectAsStateWithLifecycle()
    val config by controller.configStore.config.collectAsStateWithLifecycle()
    val isRunning = state !is AiSubtitleState.Off
    val title = stringResource(R.string.player_ai_subtitle_sheet_title)
    val palette = LocalStagePalette.current
    val accent = LocalAccentOnVideo.current

    var editingApiKey by remember(config.apiKey) { mutableStateOf(config.apiKey) }
    var showApiKeyInput by remember { mutableStateOf(false) }

    MobileBottomSheet(onDismissRequest = onDismiss, title = title) {
        StageSheetHeader(title)
        SheetScroll {
            // 开关状态
            StageGroupLabel(stringResource(R.string.player_ai_subtitle_section))
            StageMenuItem(
                label = stringResource(if (isRunning) R.string.player_ai_subtitle_toggle_off else R.string.player_ai_subtitle_toggle_on),
                value = stringResource(if (isRunning) R.string.player_ai_subtitle_status_on else R.string.player_ai_subtitle_status_off),
                checked = isRunning,
                onClick = {
                    controller.toggle(player, fallbackStreamUrl = streamUrl)
                },
            )

            // 预设配置
            StageGroupLabel(stringResource(R.string.player_ai_subtitle_provider_preset))
            AiProviderPreset.entries.forEach { preset ->
                StageMenuItem(
                    label = preset.title,
                    value = preset.description,
                    checked = config.provider == preset.provider && config.modelName == preset.modelName && config.apiBaseUrl == preset.baseUrl,
                    onClick = {
                        controller.configStore.applyPreset(preset)
                    },
                )
            }

            // API Key 快捷填写
            StageGroupLabel(stringResource(R.string.player_ai_subtitle_api_key))
            StageSettingRow(
                title = stringResource(R.string.player_ai_subtitle_api_key),
                subtitle = if (config.apiKey.isBlank()) {
                    stringResource(R.string.player_ai_subtitle_api_key_hint)
                } else {
                    "${config.apiKey.take(6)}••••••••${config.apiKey.takeLast(4)}"
                },
                onClick = { showApiKeyInput = !showApiKeyInput },
            )

            if (showApiKeyInput) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(palette.cardSolid)
                        .padding(12.dp)
                ) {
                    BasicTextField(
                        value = editingApiKey,
                        onValueChange = { editingApiKey = it },
                        textStyle = TextStyle(color = palette.text, fontSize = 14.sp),
                        cursorBrush = SolidColor(accent),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            if (editingApiKey.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.player_ai_subtitle_api_key_hint),
                                    color = palette.muted,
                                    fontSize = 14.sp,
                                )
                            }
                            innerTextField()
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Text(
                            text = stringResource(R.string.common_save),
                            color = accent,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable {
                                    controller.configStore.update { it.copy(apiKey = editingApiKey.trim()) }
                                    showApiKeyInput = false
                                }
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                        )
                    }
                }
            }

            // 提前收听延迟直播秒数
            StageGroupLabel(stringResource(R.string.player_ai_subtitle_lead_seconds))
            listOf(3, 5, 8, 10).forEach { sec ->
                StageMenuItem(
                    label = stringResource(R.string.player_ai_subtitle_seconds_unit, sec),
                    checked = config.leadSeconds == sec,
                    onClick = {
                        controller.configStore.update { it.copy(leadSeconds = sec) }
                    },
                )
            }

            // 字幕时序微调 (同步校准：前进/延后)
            StageGroupLabel(stringResource(R.string.player_ai_subtitle_timing_calibration))
            AiSubtitleTimingCalibrationRow(controller = controller)

            // 显示选项
            StageGroupLabel(stringResource(R.string.player_subtitles))
            StageMenuItem(
                label = stringResource(R.string.player_ai_subtitle_show_original),
                checked = config.showOriginal,
                onClick = {
                    controller.configStore.update { it.copy(showOriginal = !it.showOriginal) }
                },
            )

            // 字号缩放
            listOf(0.85f to "85%", 1.0f to "100%", 1.2f to "120%", 1.4f to "140%").forEach { (scaleVal, label) ->
                StageMenuItem(
                    label = "$label ${stringResource(R.string.player_ai_subtitle_font_scale)}",
                    checked = (config.fontSizeScale - scaleVal) in -0.05f..0.05f,
                    onClick = {
                        controller.configStore.update { it.copy(fontSizeScale = scaleVal) }
                    },
                )
            }
        }
    }
}

/**
 * AI 实时字幕时序快慢微调组件 ([AiSubtitleTimingCalibrationRow])。
 */
@Composable
fun AiSubtitleTimingCalibrationRow(
    controller: AiSubtitleController,
    modifier: Modifier = Modifier,
) {
    val config by controller.configStore.config.collectAsStateWithLifecycle()
    val palette = LocalStagePalette.current
    val context = androidx.compose.ui.platform.LocalContext.current
    val offsetMs = config.syncOffsetMs
    val offsetSec = kotlin.math.abs(offsetMs) / 1000f

    val offsetText = when {
        offsetMs == 0 -> stringResource(R.string.player_ai_subtitle_offset_zero)
        offsetMs > 0 -> stringResource(R.string.player_ai_subtitle_offset_later, offsetSec)
        else -> stringResource(R.string.player_ai_subtitle_offset_earlier, offsetSec)
    }

    var lastToast: Toast? by remember { mutableStateOf(null) }
    val notifyChange: (Int) -> Unit = { newOffset ->
        val sec = kotlin.math.abs(newOffset) / 1000f
        val desc = when {
            newOffset == 0 -> context.getString(R.string.player_ai_subtitle_offset_zero)
            newOffset > 0 -> context.getString(R.string.player_ai_subtitle_offset_later, sec)
            else -> context.getString(R.string.player_ai_subtitle_offset_earlier, sec)
        }
        lastToast?.cancel()
        val t = Toast.makeText(
            context,
            context.getString(R.string.player_ai_subtitle_toast_offset, desc),
            Toast.LENGTH_SHORT,
        )
        t.show()
        lastToast = t
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(palette.cardSolid)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.player_ai_subtitle_timing_calibration),
                color = palette.muted,
                fontSize = 13.sp,
            )
            Text(
                text = offsetText,
                color = palette.text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.player_ai_subtitle_timing_hint),
            color = palette.muted,
            fontSize = 11.sp,
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            tv.own.owntv.mobile.ui.stage.StageButton(
                onClick = {
                    val next = controller.adjustSyncOffset(-1000)
                    notifyChange(next)
                },
                text = stringResource(R.string.player_ai_subtitle_advance_1s),
                small = true,
                modifier = Modifier.weight(1f),
            )
            tv.own.owntv.mobile.ui.stage.StageButton(
                onClick = {
                    val next = controller.adjustSyncOffset(-500)
                    notifyChange(next)
                },
                text = stringResource(R.string.player_ai_subtitle_advance_500ms),
                small = true,
                modifier = Modifier.weight(1f),
            )
            tv.own.owntv.mobile.ui.stage.StageButton(
                onClick = {
                    val next = controller.resetSyncOffset()
                    notifyChange(next)
                },
                text = stringResource(R.string.player_ai_subtitle_reset_0s),
                small = true,
                modifier = Modifier.weight(0.9f),
            )
            tv.own.owntv.mobile.ui.stage.StageButton(
                onClick = {
                    val next = controller.adjustSyncOffset(500)
                    notifyChange(next)
                },
                text = stringResource(R.string.player_ai_subtitle_delay_500ms),
                small = true,
                modifier = Modifier.weight(1f),
            )
            tv.own.owntv.mobile.ui.stage.StageButton(
                onClick = {
                    val next = controller.adjustSyncOffset(1000)
                    notifyChange(next)
                },
                text = stringResource(R.string.player_ai_subtitle_delay_1s),
                small = true,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
