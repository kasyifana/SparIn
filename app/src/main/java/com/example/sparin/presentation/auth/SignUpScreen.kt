package com.example.sparin.presentation.auth

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sparin.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Modern Sign Up Screen for SparIN
 * Perfectly synchronized with Sign In page: soft-neumorphism, floating 3D shapes, Gen-Z aesthetic
 */
@Composable
fun SignUpScreen(
    onNavigateToSignIn: () -> Unit,
    onNavigateToPersonalization: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(CascadingWhite, SoftLavender.copy(alpha = 0.3f))
                )
            )
    ) {
        // Animated floating blobs background
        FloatingBlobsBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // 3D Hero Illustration
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentAlignment = Alignment.Center
            ) {
                FloatingSportElements()
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ),
                color = Lead
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Join SparIN and find your sport partners",
                style = MaterialTheme.typography.bodyLarge,
                color = WarmHaze,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Name Input Field
            NeumorphicTextField(
                value = name,
                onValueChange = { name = it },
                label = "Full Name",
                leadingIcon = Icons.Rounded.Person,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Input Field
            NeumorphicTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = Icons.Rounded.Email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input Field
            NeumorphicTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                leadingIcon = Icons.Rounded.Lock,
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Rounded.Visibility
                            else
                                Icons.Rounded.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = WarmHaze
                        )
                    }
                },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Input Field
            NeumorphicTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm Password",
                leadingIcon = Icons.Rounded.LockReset,
                trailingIcon = {
                    IconButton(
                        onClick = { confirmPasswordVisible = !confirmPasswordVisible }
                    ) {
                        Icon(
                            imageVector = if (confirmPasswordVisible)
                                Icons.Rounded.Visibility
                            else
                                Icons.Rounded.VisibilityOff,
                            contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                            tint = WarmHaze
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Optional: Gender & Age Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Gender Selector
                Column(
                    modifier = Modifier.weight(1.5f)
                ) {
                    Text(
                        text = "Gender (optional)",
                        style = MaterialTheme.typography.labelMedium,
                        color = WarmHaze,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        GenderPill(
                            text = "M",
                            emoji = "👨",
                            isSelected = selectedGender == "Male",
                            onClick = { selectedGender = "Male" },
                            modifier = Modifier.weight(1f)
                        )
                        GenderPill(
                            text = "F",
                            emoji = "👩",
                            isSelected = selectedGender == "Female",
                            onClick = { selectedGender = "Female" },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Age Input
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Age (optional)",
                        style = MaterialTheme.typography.labelMedium,
                        color = WarmHaze,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )
                    SmallNeumorphicTextField(
                        value = age,
                        onValueChange = { if (it.length <= 2 && it.all { c -> c.isDigit() }) age = it },
                        placeholder = "Age",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Create Account Button
            Button(
                onClick = onNavigateToPersonalization,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(28.dp),
                        ambientColor = Crunch.copy(alpha = 0.4f),
                        spotColor = Crunch.copy(alpha = 0.4f)
                    ),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Crunch
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Create Account",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Lead
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        tint = Lead
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Divider with "or"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Dreamland.copy(alpha = 0.5f)
                )
                Text(
                    text = "or sign up with",
                    style = MaterialTheme.typography.bodySmall,
                    color = WarmHaze
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Dreamland.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Google Sign Up Button
            GoogleSignUpButton(
                onClick = onNavigateToPersonalization
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Sign In Link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmHaze
                )
                Text(
                    text = "Sign In",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = TextDecoration.Underline
                    ),
                    color = Crunch,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onNavigateToSignIn() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ==================== BACKGROUND COMPONENTS ====================

@Composable
private fun FloatingBlobsBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "blob_animation")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 180f,
        targetValue = 540f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offset2"
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(60.dp)
    ) {
        // Floating blob 1 - Soft lavender
        drawCircle(
            color = ChineseSilver.copy(alpha = 0.4f),
            radius = 180f,
            center = Offset(
                x = size.width * 0.15f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 40f,
                y = size.height * 0.1f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 30f
            )
        )

        // Floating blob 2 - Peach
        drawCircle(
            color = PeachGlow.copy(alpha = 0.3f),
            radius = 140f,
            center = Offset(
                x = size.width * 0.85f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 35f,
                y = size.height * 0.2f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 40f
            )
        )

        // Floating blob 3 - Mint
        drawCircle(
            color = MintBreeze.copy(alpha = 0.3f),
            radius = 160f,
            center = Offset(
                x = size.width * 0.3f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 50f,
                y = size.height * 0.65f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 35f
            )
        )

        // Floating blob 4 - Sky
        drawCircle(
            color = SkyMist.copy(alpha = 0.25f),
            radius = 120f,
            center = Offset(
                x = size.width * 0.8f + cos(Math.toRadians(offset2.toDouble())).toFloat() * 30f,
                y = size.height * 0.5f + sin(Math.toRadians(offset1.toDouble())).toFloat() * 40f
            )
        )

        // Floating blob 5 - Rose
        drawCircle(
            color = RoseDust.copy(alpha = 0.2f),
            radius = 100f,
            center = Offset(
                x = size.width * 0.5f + sin(Math.toRadians(offset2.toDouble())).toFloat() * 35f,
                y = size.height * 0.85f + cos(Math.toRadians(offset1.toDouble())).toFloat() * 25f
            )
        )
    }
}

// ==================== HERO ELEMENTS ====================

@Composable
private fun FloatingSportElements() {
    val infiniteTransition = rememberInfiniteTransition(label = "sport_elements")

    val float1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float1"
    )

    val float2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float2"
    )

    val float3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float3"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Central SparIN element - Person with plus (create account theme)
        NeumorphicCircle(
            modifier = Modifier
                .size(90.dp)
                .align(Alignment.Center)
                .offset(y = float1.dp),
            color = ChineseSilver.copy(alpha = 0.7f)
        ) {
            Icon(
                imageVector = Icons.Rounded.PersonAdd,
                contentDescription = null,
                modifier = Modifier.size(44.dp),
                tint = Crunch
            )
        }

        // Floating soccer ball
        NeumorphicCircle(
            modifier = Modifier
                .size(55.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-25).dp, y = (15 + float2).dp),
            color = PeachGlow.copy(alpha = 0.6f)
        ) {
            Icon(
                imageVector = Icons.Rounded.SportsSoccer,
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = SunsetOrange
            )
        }

        // Floating basketball
        NeumorphicCircle(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.BottomStart)
                .offset(x = 35.dp, y = (-15 + float3).dp),
            color = MintBreeze.copy(alpha = 0.6f)
        ) {
            Icon(
                imageVector = Icons.Rounded.SportsBasketball,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = WarmHaze
            )
        }

        // Floating tennis
        NeumorphicCircle(
            modifier = Modifier
                .size(42.dp)
                .align(Alignment.TopStart)
                .offset(x = 45.dp, y = (30 + float1).dp),
            color = SkyMist.copy(alpha = 0.6f)
        ) {
            Icon(
                imageVector = Icons.Rounded.SportsTennis,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Lead
            )
        }

        // Decorative rotating ring
        Box(
            modifier = Modifier
                .size(65.dp)
                .align(Alignment.CenterEnd)
                .offset(x = (-8).dp, y = (25 + float2).dp)
                .graphicsLayer { rotationZ = rotation }
                .border(
                    width = 3.dp,
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Crunch.copy(alpha = 0.6f),
                            ChineseSilver.copy(alpha = 0.3f),
                            PeachGlow.copy(alpha = 0.5f),
                            Crunch.copy(alpha = 0.6f)
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Small decorative dots
        FloatingDot(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(x = (-35).dp, y = (8 + float3).dp),
            color = Crunch.copy(alpha = 0.7f),
            size = 10.dp
        )

        FloatingDot(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-55).dp, y = (-25 + float1).dp),
            color = ChineseSilver,
            size = 14.dp
        )

        FloatingDot(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = 18.dp, y = (-15 + float2).dp),
            color = MintBreeze,
            size = 8.dp
        )

        FloatingDot(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(x = 40.dp, y = (-5 + float3).dp),
            color = PeachGlow.copy(alpha = 0.8f),
            size = 12.dp
        )
    }
}

// ==================== INPUT COMPONENTS ====================

@Composable
private fun NeumorphicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.15f),
                spotColor = NeumorphDark.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(20.dp),
        color = NeumorphLight
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            NeumorphLight,
                            ChineseSilver.copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = label,
                        color = WarmHaze.copy(alpha = 0.7f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = Dreamland
                    )
                },
                trailingIcon = trailingIcon,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Lead,
                    unfocusedTextColor = Lead,
                    cursorColor = Crunch,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Crunch,
                    unfocusedLabelColor = WarmHaze.copy(alpha = 0.7f)
                )
            )
        }
    }
}

@Composable
private fun SmallNeumorphicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.12f),
                spotColor = NeumorphDark.copy(alpha = 0.12f)
            ),
        shape = RoundedCornerShape(16.dp),
        color = NeumorphLight
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            NeumorphLight,
                            ChineseSilver.copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = placeholder,
                        color = WarmHaze.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Lead,
                    unfocusedTextColor = Lead,
                    cursorColor = Crunch,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = keyboardOptions
            )
        }
    }
}

@Composable
private fun GenderPill(
    text: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Crunch.copy(alpha = 0.2f) else CascadingWhite,
        animationSpec = tween(200),
        label = "gender_bg"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Crunch else ChineseSilver.copy(alpha = 0.5f),
        animationSpec = tween(200),
        label = "gender_border"
    )

    Surface(
        modifier = modifier
            .height(48.dp)
            .shadow(
                elevation = if (isSelected) 6.dp else 2.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = if (isSelected) Crunch.copy(alpha = 0.2f) else NeumorphDark.copy(alpha = 0.1f)
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                ),
                color = if (isSelected) Lead else WarmHaze
            )
        }
    }
}

// ==================== GOOGLE BUTTON ====================

@Composable
private fun GoogleSignUpButton(
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = NeumorphDark.copy(alpha = 0.1f),
                spotColor = NeumorphDark.copy(alpha = 0.1f)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        color = NeumorphLight
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            NeumorphLight,
                            ChineseSilver.copy(alpha = 0.4f)
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Google "G" icon representation
                Surface(
                    modifier = Modifier.size(24.dp),
                    shape = CircleShape,
                    color = Color.Transparent
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "G",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Lead
                        )
                    }
                }
                Text(
                    text = "Continue with Google",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Lead
                )
            }
        }
    }
}

// ==================== SHARED COMPONENTS ====================

@Composable
private fun NeumorphicCircle(
    modifier: Modifier = Modifier,
    color: Color,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = CircleShape,
                ambientColor = NeumorphDark.copy(alpha = 0.25f),
                spotColor = NeumorphDark.copy(alpha = 0.25f)
            )
            .background(
                color = color,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
private fun FloatingDot(
    modifier: Modifier = Modifier,
    color: Color,
    size: Dp
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(6.dp, CircleShape)
            .background(color, CircleShape)
    )
}
