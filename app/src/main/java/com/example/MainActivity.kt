package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.Screen
import com.example.model.ThemeMode
import com.example.ui.components.AuthModal
import com.example.ui.components.SellAiTopBar
import com.example.ui.components.UpgradeModal
import com.example.ui.screens.AdsScreen
import com.example.ui.screens.AiWriterScreen
import com.example.ui.screens.BrandVoiceScreen
import com.example.ui.screens.CampaignGeneratorScreen
import com.example.ui.screens.CreateHubScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EmailScreen
import com.example.ui.screens.ForgotPasswordScreen
import com.example.ui.screens.HashtagsScreen
import com.example.ui.screens.LandingPageScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProductDescriptionScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.SavedContentScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SocialMediaScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SellAiGradients
import com.example.ui.theme.SellAiTheme
import com.example.ui.viewmodel.SellAiViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: SellAiViewModel = viewModel()
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
            val systemDark = isSystemInDarkTheme()

            val isDarkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> systemDark
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

            SellAiTheme(darkTheme = isDarkTheme) {
                SellAiApp(
                    viewModel = viewModel,
                    themeMode = themeMode,
                    onSelectThemeMode = { viewModel.themeMode.value = it }
                )
            }
        }
    }
}

data class NavItem(
    val screen: Screen,
    val title: String,
    val icon: ImageVector,
    val isPrimaryBottomNav: Boolean = false
)

@Composable
fun SellAiApp(
    viewModel: SellAiViewModel,
    themeMode: ThemeMode,
    onSelectThemeMode: (ThemeMode) -> Unit
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val userAccount by viewModel.userAccount.collectAsStateWithLifecycle()
    val brandVoice by viewModel.brandVoice.collectAsStateWithLifecycle()
    val showUpgradeDialog by viewModel.showUpgradeDialog.collectAsStateWithLifecycle()
    val showAuthDialog by viewModel.showAuthDialog.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Intercept back button for deep sub-screen navigation
    val isAuthFlow = currentScreen in setOf(
        Screen.SPLASH,
        Screen.ONBOARDING,
        Screen.LOGIN,
        Screen.REGISTER,
        Screen.FORGOT_PASSWORD
    )

    BackHandler(enabled = !isAuthFlow && currentScreen != Screen.DASHBOARD) {
        if (!viewModel.handleBack()) {
            viewModel.navigateTo(Screen.DASHBOARD)
        }
    }

    BackHandler(enabled = currentScreen in setOf(Screen.LOGIN, Screen.REGISTER, Screen.FORGOT_PASSWORD)) {
        viewModel.navigateTo(Screen.ONBOARDING)
    }

    // Global toast listener
    LaunchedEffect(Unit) {
        viewModel.toastEvent.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    // 5 Primary Bottom Navigation Destinations requested: Home, Create, Campaigns, Saved, Profile
    val bottomNavItems = listOf(
        NavItem(Screen.DASHBOARD, "Home", Icons.Default.Dashboard, isPrimaryBottomNav = true),
        NavItem(Screen.CREATE_HUB, "Create", Icons.Default.AutoAwesome, isPrimaryBottomNav = true),
        NavItem(Screen.CAMPAIGN_GENERATOR, "Campaigns", Icons.Default.Campaign, isPrimaryBottomNav = true),
        NavItem(Screen.SAVED_CONTENT, "Saved", Icons.Default.Bookmark, isPrimaryBottomNav = true),
        NavItem(Screen.PROFILE, "Profile", Icons.Default.Person, isPrimaryBottomNav = true)
    )

    // Full Drawer Items for deep navigation
    val drawerNavItems = listOf(
        NavItem(Screen.DASHBOARD, "Home Dashboard", Icons.Default.Dashboard),
        NavItem(Screen.CREATE_HUB, "Creation Studio Hub", Icons.Default.AutoAwesome),
        NavItem(Screen.CAMPAIGN_GENERATOR, "14-in-1 Campaign Generator", Icons.Default.Campaign),
        NavItem(Screen.AI_WRITER, "AI Copywriter & Editor", Icons.Default.AutoAwesome),
        NavItem(Screen.PRODUCT_DESCRIPTION, "Product Description", Icons.Default.Description),
        NavItem(Screen.SOCIAL_MEDIA, "Social Media & TikTok", Icons.Default.Share),
        NavItem(Screen.ADS, "Multi-Platform Ads", Icons.Default.Campaign),
        NavItem(Screen.EMAIL, "Marketing Emails", Icons.Default.Email),
        NavItem(Screen.HASHTAGS, "Hashtag Engine", Icons.Default.Tag),
        NavItem(Screen.SAVED_CONTENT, "Saved Content Library", Icons.Default.Bookmark),
        NavItem(Screen.BRAND_VOICE, "Brand Voice & Guidelines", Icons.Default.RecordVoiceOver),
        NavItem(Screen.PROFILE, "Account Profile & Tier", Icons.Default.Person),
        NavItem(Screen.SETTINGS, "Settings & Preferences", Icons.Default.Settings),
        NavItem(Screen.LANDING, "Product Overview Showcase", Icons.Default.Public)
    )

    val configuration = LocalConfiguration.current
    val isExpandedScreen = configuration.screenWidthDp >= 720

    // Full screen onboarding/auth flow
    if (isAuthFlow) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (currentScreen) {
                Screen.SPLASH -> SplashScreen(viewModel = viewModel)
                Screen.ONBOARDING -> OnboardingScreen(viewModel = viewModel)
                Screen.LOGIN -> LoginScreen(viewModel = viewModel)
                Screen.REGISTER -> RegisterScreen(viewModel = viewModel)
                Screen.FORGOT_PASSWORD -> ForgotPasswordScreen(viewModel = viewModel)
                else -> SplashScreen(viewModel = viewModel)
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
        return
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight(),
                drawerContainerColor = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(16.dp)
                ) {
                    // Drawer Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SellAiGradients.brandGradient),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "S",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "SellAI Studio",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Turn ideas into content that sells",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "MARKETING TOOLS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )

                    // Navigation items
                    drawerNavItems.forEach { item ->
                        val isSelected = currentScreen == item.screen
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 13.sp
                                )
                            },
                            selected = isSelected,
                            onClick = {
                                viewModel.navigateTo(item.screen)
                                scope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                unselectedContainerColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("drawer_item_${item.screen.name.lowercase()}")
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(10.dp))

                    // User Profile quick display
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                viewModel.navigateTo(Screen.PROFILE)
                                scope.launch { drawerState.close() }
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(SellAiGradients.brandGradient),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userAccount.name.take(1).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(userAccount.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            Text(userAccount.planTier.displayName, style = MaterialTheme.typography.labelSmall, color = CyanAccent)
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                SellAiTopBar(
                    userAccount = userAccount,
                    brandVoice = brandVoice,
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    },
                    onUpgradeClick = { viewModel.showUpgradeDialog.value = true },
                    onProfileClick = { viewModel.navigateTo(Screen.PROFILE) },
                    modifier = Modifier.statusBarsPadding()
                )
            },
            bottomBar = {
                if (!isExpandedScreen) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 2.dp
                    ) {
                        bottomNavItems.forEach { item ->
                            val isSelected = currentScreen == item.screen
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.navigateTo(item.screen) },
                                icon = { Icon(item.icon, contentDescription = item.title) },
                                label = { Text(item.title, fontSize = 11.sp) },
                                modifier = Modifier.testTag("bottom_nav_${item.title.lowercase()}")
                            )
                        }
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Persistent Side Navigation Rail on Expanded Tablet / Desktop screens
                if (isExpandedScreen) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        bottomNavItems.forEach { item ->
                            val isSelected = currentScreen == item.screen
                            NavigationRailItem(
                                selected = isSelected,
                                onClick = { viewModel.navigateTo(item.screen) },
                                icon = { Icon(item.icon, contentDescription = item.title) },
                                label = { Text(item.title, fontSize = 10.sp) }
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    when (currentScreen) {
                        Screen.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                        Screen.CREATE_HUB -> CreateHubScreen(viewModel = viewModel)
                        Screen.CAMPAIGN_GENERATOR -> CampaignGeneratorScreen(viewModel = viewModel)
                        Screen.SAVED_CONTENT -> SavedContentScreen(viewModel = viewModel)
                        Screen.PROFILE -> ProfileScreen(
                            viewModel = viewModel,
                            themeMode = themeMode,
                            onSelectThemeMode = onSelectThemeMode
                        )
                        Screen.AI_WRITER -> AiWriterScreen(viewModel = viewModel)
                        Screen.PRODUCT_DESCRIPTION -> ProductDescriptionScreen(viewModel = viewModel)
                        Screen.SOCIAL_MEDIA -> SocialMediaScreen(viewModel = viewModel)
                        Screen.ADS -> AdsScreen(viewModel = viewModel)
                        Screen.EMAIL -> EmailScreen(viewModel = viewModel)
                        Screen.HASHTAGS -> HashtagsScreen(viewModel = viewModel)
                        Screen.BRAND_VOICE -> BrandVoiceScreen(viewModel = viewModel)
                        Screen.SETTINGS -> SettingsScreen(
                            viewModel = viewModel,
                            isDarkTheme = themeMode == ThemeMode.DARK,
                            onToggleDarkTheme = { dark ->
                                onSelectThemeMode(if (dark) ThemeMode.DARK else ThemeMode.LIGHT)
                            }
                        )
                        Screen.LANDING -> LandingPageScreen(viewModel = viewModel)
                        else -> DashboardScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }

    // Upgrade Modal Dialog
    if (showUpgradeDialog) {
        UpgradeModal(
            onDismiss = { viewModel.showUpgradeDialog.value = false },
            onUpgrade = { viewModel.upgradeToPro() }
        )
    }

    // Auth Modal Dialog
    if (showAuthDialog) {
        AuthModal(
            onDismiss = { viewModel.showAuthDialog.value = false },
            onLoginSuccess = { name, email ->
                viewModel.updateProfile(name, email)
                viewModel.login()
            }
        )
    }
}
