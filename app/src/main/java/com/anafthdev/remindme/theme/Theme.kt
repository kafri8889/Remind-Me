package com.anafthdev.remindme.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val BrownLightColors = lightColorScheme(
	primary = md_theme_brown_light_primary,
	onPrimary = md_theme_brown_light_onPrimary,
	primaryContainer = md_theme_brown_light_primaryContainer,
	onPrimaryContainer = md_theme_brown_light_onPrimaryContainer,
	secondary = md_theme_brown_light_secondary,
	onSecondary = md_theme_brown_light_onSecondary,
	secondaryContainer = md_theme_brown_light_secondaryContainer,
	onSecondaryContainer = md_theme_brown_light_onSecondaryContainer,
	tertiary = md_theme_brown_light_tertiary,
	onTertiary = md_theme_brown_light_onTertiary,
	tertiaryContainer = md_theme_brown_light_tertiaryContainer,
	onTertiaryContainer = md_theme_brown_light_onTertiaryContainer,
	error = md_theme_brown_light_error,
	errorContainer = md_theme_brown_light_errorContainer,
	onError = md_theme_brown_light_onError,
	onErrorContainer = md_theme_brown_light_onErrorContainer,
	background = md_theme_brown_light_background,
	onBackground = md_theme_brown_light_onBackground,
	surface = md_theme_brown_light_surface,
	onSurface = md_theme_brown_light_onSurface,
	surfaceVariant = md_theme_brown_light_surfaceVariant,
	onSurfaceVariant = md_theme_brown_light_onSurfaceVariant,
	outline = md_theme_brown_light_outline,
	inverseOnSurface = md_theme_brown_light_inverseOnSurface,
	inverseSurface = md_theme_brown_light_inverseSurface,
	inversePrimary = md_theme_brown_light_inversePrimary,
	surfaceTint = md_theme_brown_light_surfaceTint,
)


val BrownDarkColors = darkColorScheme(
	primary = md_theme_brown_dark_primary,
	onPrimary = md_theme_brown_dark_onPrimary,
	primaryContainer = md_theme_brown_dark_primaryContainer,
	onPrimaryContainer = md_theme_brown_dark_onPrimaryContainer,
	secondary = md_theme_brown_dark_secondary,
	onSecondary = md_theme_brown_dark_onSecondary,
	secondaryContainer = md_theme_brown_dark_secondaryContainer,
	onSecondaryContainer = md_theme_brown_dark_onSecondaryContainer,
	tertiary = md_theme_brown_dark_tertiary,
	onTertiary = md_theme_brown_dark_onTertiary,
	tertiaryContainer = md_theme_brown_dark_tertiaryContainer,
	onTertiaryContainer = md_theme_brown_dark_onTertiaryContainer,
	error = md_theme_brown_dark_error,
	errorContainer = md_theme_brown_dark_errorContainer,
	onError = md_theme_brown_dark_onError,
	onErrorContainer = md_theme_brown_dark_onErrorContainer,
	background = md_theme_brown_dark_background,
	onBackground = md_theme_brown_dark_onBackground,
	surface = md_theme_brown_dark_surface,
	onSurface = md_theme_brown_dark_onSurface,
	surfaceVariant = md_theme_brown_dark_surfaceVariant,
	onSurfaceVariant = md_theme_brown_dark_onSurfaceVariant,
	outline = md_theme_brown_dark_outline,
	inverseOnSurface = md_theme_brown_dark_inverseOnSurface,
	inverseSurface = md_theme_brown_dark_inverseSurface,
	inversePrimary = md_theme_brown_dark_inversePrimary,
	surfaceTint = md_theme_brown_dark_surfaceTint,
)

@Composable
fun RemindMeTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	content: @Composable () -> Unit
) {
	
	val colorScheme = if (darkTheme) BrownDarkColors else BrownLightColors

//	val view = LocalView.current
//	if (!view.isInEditMode) {
//		SideEffect {
//			(view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
//			ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
//		}
//	}
	
	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		content = content
	)
}