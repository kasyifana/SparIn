# 🎨 SparIN Complete Color Palette System

**Design Philosophy**: Modern • Playful • Sporty • Gen-Z • Vibrant • Energetic

Sistem warna SparIN dirancang untuk memberikan pengalaman visual yang menarik dan energik, dengan dukungan penuh untuk Light Mode dan Dark Mode. Palette ini terinspirasi dari desain modern yang playful dengan aksen neon yang bold.

---

## 📋 Table of Contents
1. [Brand Colors (Global)](#1-brand-colors-global)
2. [Light Mode Palette](#2-light-mode-palette)
3. [Dark Mode Palette](#3-dark-mode-palette)
4. [Component-Specific Colors](#4-component-specific-colors)
5. [Gradient Definitions](#5-gradient-definitions)
6. [Implementation Guidelines](#6-implementation-guidelines)

---

## 1. Brand Colors (Global)

Warna brand utama yang konsisten di kedua mode, dengan adaptasi kontras untuk readability.

### 1.1 Primary Brand Colors

| Token | Name | Hex | RGB | HSL | Usage |
|-------|------|-----|-----|-----|-------|
| `Brand/Primary` | **Crunch** | `#F3BA60` | `243, 186, 96` | `37°, 85%, 66%` | CTA utama, tombol penting, highlight aksi |
| `Brand/Secondary` | **Chinese Silver** | `#E0DBF3` | `224, 219, 243` | `253°, 48%, 91%` | Background playful, accent card, decorative |
| `Brand/Tertiary` | **Dreamland** | `#B6B1C0` | `182, 177, 192` | `260°, 11%, 72%` | Border, secondary text, badge outline |

### 1.2 Neutral Colors

| Token | Name | Hex | RGB | HSL | Usage |
|-------|------|-----|-----|-----|-------|
| `Brand/Neutral-Dark` | **Lead** | `#202022` | `32, 32, 34` | `240°, 3%, 13%` | Text & icon di light mode, BG di dark mode |
| `Brand/Neutral-Light` | **Cascading White** | `#F6F6F6` | `246, 246, 246` | `0°, 0%, 96%` | Background di light mode, surface |
| `Brand/Warm-Gray` | **Warm Haze** | `#736A6A` | `115, 106, 106` | `0°, 4%, 43%` | Subtitle, supporting info, disabled state |

### 1.3 Accent Colors (Inspired by Design)

| Token | Name | Hex | RGB | HSL | Usage |
|-------|------|-----|-----|-----|-------|
| `Accent/Cyan` | **Sporty Cyan** | `#6FEDD6` | `111, 237, 214` | `169°, 78%, 68%` | Cards, badges, playful elements |
| `Accent/Neon-Green` | **Neon Lime** | `#C8FF00` | `200, 255, 0` | `73°, 100%, 50%` | High-energy CTA, focus states |
| `Accent/Purple` | **Vibrant Purple** | `#8B5CF6` | `139, 92, 246` | `258°, 90%, 66%` | Premium features, highlights |
| `Accent/Peach` | **Soft Peach** | `#FFD4B8` | `255, 212, 184` | `24°, 100%, 86%` | Warm accents, secondary cards |

---

## 2. Light Mode Palette

### 2.1 Background Colors

| Token | Hex | RGB | HSL | Usage | Example |
|-------|-----|-----|-----|-------|---------|
| `BG/Primary` | `#FFFFFF` | `255, 255, 255` | `0°, 0%, 100%` | Main background, screen base | Entire screen background |
| `BG/Secondary` | `#F6F6F6` | `246, 246, 246` | `0°, 0%, 96%` | Card background, sections | Card containers |
| `BG/Tertiary` | `#E0DBF3` | `224, 219, 243` | `253°, 48%, 91%` | Playful blocks, tags | Category tags, decorative blocks |
| `BG/Accent-Cyan` | `#E5FAF6` | `229, 250, 246` | `169°, 68%, 94%` | Soft cyan backgrounds | Info cards, progress sections |
| `BG/Accent-Peach` | `#FFF5EE` | `255, 245, 238` | `25°, 100%, 97%` | Warm section backgrounds | Goal cards, warm sections |
| `BG/Overlay` | `rgba(0,0,0,0.05)` | `0, 0, 0, 5%` | - | Modal overlays, pressed states | Bottom sheets, dialogs |

### 2.2 Surface / Card Colors

| Token | Hex | RGB | HSL | Usage |
|-------|-----|-----|-----|-------|
| `Surface/Default` | `#FFFFFF` | `255, 255, 255` | `0°, 0%, 100%` | Default card background |
| `Surface/Elevated` | `#FFFFFF` | `255, 255, 255` | `0°, 0%, 100%` | Elevated cards (with shadow) |
| `Surface/Variant` | `#F7F4FF` | `247, 244, 255` | `256°, 100%, 98%` | Soft lilac variant for playful UI |
| `Surface/Accent` | `#E0DBF3` | `224, 219, 243` | `253°, 48%, 91%` | Category cards, decorative blocks |
| `Surface/Cyan` | `#6FEDD6` | `111, 237, 214` | `169°, 78%, 68%` | Sporty cyan cards (like in design) |
| `Surface/Neon` | `#C8FF00` | `200, 255, 0` | `73°, 100%, 50%` | High-energy focus cards |
| `Surface/Peach` | `#FFD4B8` | `255, 212, 184` | `24°, 100%, 86%` | Warm accent cards |

### 2.3 Text Colors

| Token | Hex | RGB | HSL | Usage | Example |
|-------|-----|-----|-----|-------|---------|
| `Text/Primary` | `#202022` | `32, 32, 34` | `240°, 3%, 13%` | Judul, teks utama | Headers, body text |
| `Text/Secondary` | `#736A6A` | `115, 106, 106` | `0°, 4%, 43%` | Subtitle, deskripsi | Descriptions, captions |
| `Text/Tertiary` | `#A8A4AD` | `168, 164, 173` | `267°, 6%, 66%` | Placeholder, disabled | Input placeholders |
| `Text/Inverse` | `#FFFFFF` | `255, 255, 255` | `0°, 0%, 100%` | Text di atas dark elements | Text on dark buttons |
| `Text/On-Cyan` | `#0A4A3F` | `10, 74, 63` | `170°, 76%, 16%` | Text on cyan backgrounds | Text on cyan cards |
| `Text/On-Neon` | `#2D3A00` | `45, 58, 0` | `73°, 100%, 11%` | Text on neon green | Text on neon buttons |
| `Text/Link` | `#8B5CF6` | `139, 92, 246` | `258°, 90%, 66%` | Hyperlinks, clickable text | Links, see more |

### 2.4 Action & CTA Colors

| Token | Hex | RGB | HSL | State | Usage |
|-------|-----|-----|-----|-------|-------|
| `CTA/Primary` | `#F3BA60` | `243, 186, 96` | `37°, 85%, 66%` | Default | Primary buttons, main actions |
| `CTA/Primary-Hover` | `#F5C678` | `245, 198, 120` | `37°, 85%, 72%` | Hover | Hover state |
| `CTA/Primary-Pressed` | `#D89F4B` | `216, 159, 75` | `36°, 64%, 57%` | Pressed | Active/pressed state |
| `CTA/Primary-Disabled` | `#F9E4C1` | `249, 228, 193` | `38°, 78%, 87%` | Disabled | Disabled state |
| `CTA/Secondary` | `#E0DBF3` | `224, 219, 243` | `253°, 48%, 91%` | Default | Secondary buttons |
| `CTA/Secondary-Hover` | `#E8E4F7` | `232, 228, 247` | `253°, 56%, 93%` | Hover | Hover state |
| `CTA/Secondary-Pressed` | `#C6C0DD` | `198, 192, 221` | `252°, 30%, 81%` | Pressed | Active/pressed state |
| `CTA/Neon` | `#C8FF00` | `200, 255, 0` | `73°, 100%, 50%` | Default | High-energy CTAs |
| `CTA/Neon-Pressed` | `#A3D400` | `163, 212, 0` | `74°, 100%, 42%` | Pressed | Pressed state |
| `CTA/Outline` | `transparent` | - | - | Default | Outline buttons (border only) |

### 2.5 Border & Divider Colors

| Token | Hex | RGB | HSL | Usage |
|-------|-----|-----|-----|-------|
| `Border/Light` | `#E4E4E4` | `228, 228, 228` | `0°, 0%, 89%` | Subtle borders, dividers |
| `Border/Medium` | `#C7C7C7` | `199, 199, 199` | `0°, 0%, 78%` | Standard borders |
| `Border/Dark` | `#A8A4AD` | `168, 164, 173` | `267°, 6%, 66%` | Emphasized borders |
| `Border/Accent` | `#B6B1C0` | `182, 177, 192` | `260°, 11%, 72%` | Dreamland accent borders |
| `Border/Cyan` | `#6FEDD6` | `111, 237, 214` | `169°, 78%, 68%` | Sporty cyan borders |
| `Border/Focus` | `#8B5CF6` | `139, 92, 246` | `258°, 90%, 66%` | Focus state borders |

### 2.6 Status & Feedback Colors

| Type | Token | Hex | RGB | HSL | Usage |
|------|-------|-----|-----|-----|-------|
| **Success** | `Status/Success` | `#45D27B` | `69, 210, 123` | `143°, 61%, 55%` | Match success, joined, completed |
| | `Status/Success-BG` | `#E8F8EF` | `232, 248, 239` | `146°, 57%, 94%` | Success background |
| | `Status/Success-Border` | `#A3E6C1` | `163, 230, 193` | `145°, 56%, 77%` | Success border |
| **Warning** | `Status/Warning` | `#F3BA60` | `243, 186, 96` | `37°, 85%, 66%` | Slot almost full, caution |
| | `Status/Warning-BG` | `#FEF5E7` | `254, 245, 231` | `37°, 88%, 95%` | Warning background |
| | `Status/Warning-Border` | `#F9DCA8` | `249, 220, 168` | `39°, 87%, 82%` | Warning border |
| **Danger** | `Status/Danger` | `#FF6B6B` | `255, 107, 107` | `0°, 100%, 71%` | Error, alert, critical |
| | `Status/Danger-BG` | `#FFEBEB` | `255, 235, 235` | `0°, 100%, 96%` | Error background |
| | `Status/Danger-Border` | `#FFB5B5` | `255, 181, 181` | `0°, 100%, 85%` | Error border |
| **Info** | `Status/Info` | `#7DC8F7` | `125, 200, 247` | `203°, 88%, 73%` | Announcements, info |
| | `Status/Info-BG` | `#EAF6FE` | `234, 246, 254` | `204°, 91%, 96%` | Info background |
| | `Status/Info-Border` | `#BEE3FB` | `190, 227, 251` | `204°, 88%, 86%` | Info border |

### 2.7 Icon Colors

| Token | Hex | RGB | HSL | Usage |
|-------|-----|-----|-----|-------|
| `Icon/Primary` | `#202022` | `32, 32, 34` | `240°, 3%, 13%` | Primary icons |
| `Icon/Secondary` | `#736A6A` | `115, 106, 106` | `0°, 4%, 43%` | Secondary icons |
| `Icon/Tertiary` | `#A8A4AD` | `168, 164, 173` | `267°, 6%, 66%` | Tertiary/disabled icons |
| `Icon/Inverse` | `#FFFFFF` | `255, 255, 255` | `0°, 0%, 100%` | Icons on dark backgrounds |
| `Icon/Accent` | `#F3BA60` | `243, 186, 96` | `37°, 85%, 66%` | Accent/active icons |
| `Icon/Cyan` | `#6FEDD6` | `111, 237, 214` | `169°, 78%, 68%` | Sporty cyan icons |

---

## 3. Dark Mode Palette

Dark mode mengikuti vibe modern dengan background gelap elegan dan accent neon yang vibrant untuk kontras maksimal.

### 3.1 Background Colors

| Token | Hex | RGB | HSL | Usage | Example |
|-------|-----|-----|-----|-------|---------|
| `BG/Primary` | `#202022` | `32, 32, 34` | `240°, 3%, 13%` | Main background | Entire screen background |
| `BG/Secondary` | `#2A2A2D` | `42, 42, 45` | `240°, 3%, 17%` | Section backgrounds | Card containers |
| `BG/Tertiary` | `#3A3A3D` | `58, 58, 61` | `240°, 3%, 23%` | Elevated surfaces | Elevated cards |
| `BG/Purple-Dark` | `#2D1B4E` | `45, 27, 78` | `261°, 49%, 21%` | Purple-tinted background | Special sections (like in design) |
| `BG/Accent-Cyan` | `#1A4A44` | `26, 74, 68` | `173°, 48%, 20%` | Dark cyan backgrounds | Cyan card backgrounds |
| `BG/Accent-Neon` | `#3A4500` | `58, 69, 0` | `70°, 100%, 14%` | Dark neon backgrounds | Neon card backgrounds |
| `BG/Overlay` | `rgba(0,0,0,0.6)` | `0, 0, 0, 60%` | - | Modal overlays | Dialogs, bottom sheets |

### 3.2 Surface / Card Colors

| Token | Hex | RGB | HSL | Usage |
|-------|-----|-----|-----|-------|
| `Surface/Default` | `#2A2A2D` | `42, 42, 45` | `240°, 3%, 17%` | Default card background |
| `Surface/Elevated` | `#3A3A3D` | `58, 58, 61` | `240°, 3%, 23%` | Elevated cards |
| `Surface/Variant` | `#393943` | `57, 57, 67` | `240°, 8%, 24%` | Variant cards with subtle tint |
| `Surface/Accent` | `#E0DBF3` | `224, 219, 243` | `253°, 48%, 91%` | Light accent (contrast pop) |
| `Surface/Purple` | `#5B3A8F` | `91, 58, 143` | `263°, 42%, 39%` | Purple accent cards |
| `Surface/Cyan` | `#6FEDD6` | `111, 237, 214` | `169°, 78%, 68%` | Bright cyan cards (high contrast) |
| `Surface/Neon` | `#C8FF00` | `200, 255, 0` | `73°, 100%, 50%` | Neon green cards (maximum energy) |
| `Surface/Peach` | `#FFD4B8` | `255, 212, 184` | `24°, 100%, 86%` | Warm peach cards |

### 3.3 Text Colors

| Token | Hex | RGB | HSL | Usage | Example |
|-------|-----|-----|-----|-------|---------|
| `Text/Primary` | `#FFFFFF` | `255, 255, 255` | `0°, 0%, 100%` | Primary text | Headers, body text |
| `Text/Secondary` | `#B6B1C0` | `182, 177, 192` | `260°, 11%, 72%` | Secondary text | Subtitles, descriptions |
| `Text/Tertiary` | `#928C9C` | `146, 140, 156` | `263°, 7%, 58%` | Tertiary text | Supporting info, placeholders |
| `Text/Disabled` | `#5A5A5F` | `90, 90, 95` | `240°, 3%, 36%` | Disabled text | Disabled states |
| `Text/Inverse` | `#202022` | `32, 32, 34` | `240°, 3%, 13%` | Text on bright cards | Text on neon/cyan cards |
| `Text/On-Cyan` | `#0A4A3F` | `10, 74, 63` | `170°, 76%, 16%` | Text on cyan | Dark text on cyan surface |
| `Text/On-Neon` | `#2D3A00` | `45, 58, 0` | `73°, 100%, 11%` | Text on neon | Dark text on neon surface |
| `Text/Link` | `#A78BFA` | `167, 139, 250` | `255°, 92%, 76%` | Links | Clickable links |

### 3.4 Action & CTA Colors

| Token | Hex | RGB | HSL | State | Usage |
|-------|-----|-----|-----|-------|-------|
| `CTA/Primary` | `#F3BA60` | `243, 186, 96` | `37°, 85%, 66%` | Default | Primary buttons |
| `CTA/Primary-Hover` | `#F5C678` | `245, 198, 120` | `37°, 85%, 72%` | Hover | Hover state |
| `CTA/Primary-Pressed` | `#D89F4B` | `216, 159, 75` | `36°, 64%, 57%` | Pressed | Pressed state |
| `CTA/Primary-Disabled` | `#6B5A3D` | `107, 90, 61` | `38°, 27%, 33%` | Disabled | Disabled state |
| `CTA/Secondary` | `#B6B1C0` | `182, 177, 192` | `260°, 11%, 72%` | Default | Secondary buttons |
| `CTA/Secondary-Hover` | `#C5C1CF` | `197, 193, 207` | `257°, 13%, 78%` | Hover | Hover state |
| `CTA/Secondary-Pressed` | `#9E99A7` | `158, 153, 167` | `261°, 7%, 63%` | Pressed | Pressed state |
| `CTA/Neon` | `#C8FF00` | `200, 255, 0` | `73°, 100%, 50%` | Default | High-energy CTAs |
| `CTA/Neon-Pressed` | `#A3D400` | `163, 212, 0` | `74°, 100%, 42%` | Pressed | Pressed state |
| `CTA/Purple` | `#8B5CF6` | `139, 92, 246` | `258°, 90%, 66%` | Default | Premium actions |

### 3.5 Border & Divider Colors

| Token | Hex | RGB | HSL | Usage |
|-------|-----|-----|-----|-------|
| `Border/Light` | `#3A3A3D` | `58, 58, 61` | `240°, 3%, 23%` | Subtle borders |
| `Border/Medium` | `#525257` | `82, 82, 87` | `240°, 3%, 33%` | Standard borders |
| `Border/Dark` | `#6A6A6F` | `106, 106, 111` | `240°, 2%, 43%` | Emphasized borders |
| `Border/Accent` | `#E0DBF3` | `224, 219, 243` | `253°, 48%, 91%` | Light accent borders |
| `Border/Cyan` | `#6FEDD6` | `111, 237, 214` | `169°, 78%, 68%` | Cyan borders |
| `Border/Neon` | `#C8FF00` | `200, 255, 0` | `73°, 100%, 50%` | Neon borders |
| `Border/Focus` | `#A78BFA` | `167, 139, 250` | `255°, 92%, 76%` | Focus state |

### 3.6 Status & Feedback Colors

| Type | Token | Hex | RGB | HSL | Usage |
|------|-------|-----|-----|-----|-------|
| **Success** | `Status/Success` | `#45D27B` | `69, 210, 123` | `143°, 61%, 55%` | Success states |
| | `Status/Success-BG` | `#1A3D2A` | `26, 61, 42` | `147°, 40%, 17%` | Success background |
| | `Status/Success-Border` | `#2D6B47` | `45, 107, 71` | `145°, 41%, 30%` | Success border |
| **Warning** | `Status/Warning` | `#F3BA60` | `243, 186, 96` | `37°, 85%, 66%` | Warning states |
| | `Status/Warning-BG` | `#3D2F1A` | `61, 47, 26` | `36°, 40%, 17%` | Warning background |
| | `Status/Warning-Border` | `#6B5234` | `107, 82, 52` | `33°, 35%, 31%` | Warning border |
| **Danger** | `Status/Danger` | `#FF6B6B` | `255, 107, 107` | `0°, 100%, 71%` | Error states |
| | `Status/Danger-BG` | `#3D1A1A` | `61, 26, 26` | `0°, 40%, 17%` | Error background |
| | `Status/Danger-Border` | `#6B2D2D` | `107, 45, 45` | `0°, 41%, 30%` | Error border |
| **Info** | `Status/Info` | `#7DC8F7` | `125, 200, 247` | `203°, 88%, 73%` | Info states |
| | `Status/Info-BG` | `#1A2F3D` | `26, 47, 61` | `204°, 40%, 17%` | Info background |
| | `Status/Info-Border` | `#2D526B` | `45, 82, 107` | `204°, 41%, 30%` | Info border |

### 3.7 Icon Colors

| Token | Hex | RGB | HSL | Usage |
|-------|-----|-----|-----|-------|
| `Icon/Primary` | `#FFFFFF` | `255, 255, 255` | `0°, 0%, 100%` | Primary icons |
| `Icon/Secondary` | `#B6B1C0` | `182, 177, 192` | `260°, 11%, 72%` | Secondary icons |
| `Icon/Tertiary` | `#928C9C` | `146, 140, 156` | `263°, 7%, 58%` | Tertiary icons |
| `Icon/Inverse` | `#202022` | `32, 32, 34` | `240°, 3%, 13%` | Icons on bright backgrounds |
| `Icon/Accent` | `#F3BA60` | `243, 186, 96` | `37°, 85%, 66%` | Accent icons |
| `Icon/Cyan` | `#6FEDD6` | `111, 237, 214` | `169°, 78%, 68%` | Cyan icons |
| `Icon/Neon` | `#C8FF00` | `200, 255, 0` | `73°, 100%, 50%` | Neon icons |

---

## 4. Component-Specific Colors

### 4.1 Navigation Bar

#### Light Mode
| Element | Token | Hex | Usage |
|---------|-------|-----|-------|
| Background | `BG/Primary` | `#FFFFFF` | Navbar background |
| Active Icon | `Icon/Accent` | `#F3BA60` | Selected tab icon |
| Inactive Icon | `Icon/Tertiary` | `#A8A4AD` | Unselected tab icons |
| Indicator | `CTA/Primary` | `#F3BA60` | Active tab indicator |
| Border | `Border/Light` | `#E4E4E4` | Top border |

#### Dark Mode
| Element | Token | Hex | Usage |
|---------|-------|-----|-------|
| Background | `BG/Primary` | `#202022` | Navbar background |
| Active Icon | `Icon/Accent` | `#F3BA60` | Selected tab icon |
| Inactive Icon | `Icon/Tertiary` | `#928C9C` | Unselected tab icons |
| Indicator | `CTA/Primary` | `#F3BA60` | Active tab indicator |
| Border | `Border/Light` | `#3A3A3D` | Top border |

### 4.2 Cards & Containers

#### Light Mode
| Card Type | Background | Border | Text | Icon |
|-----------|------------|--------|------|------|
| Default | `#FFFFFF` | `#E4E4E4` | `#202022` | `#202022` |
| Elevated | `#FFFFFF` | None (shadow) | `#202022` | `#202022` |
| Cyan Accent | `#6FEDD6` | `#6FEDD6` | `#0A4A3F` | `#0A4A3F` |
| Neon Accent | `#C8FF00` | `#C8FF00` | `#2D3A00` | `#2D3A00` |
| Peach Accent | `#FFD4B8` | `#FFD4B8` | `#5A3A1A` | `#5A3A1A` |
| Purple Accent | `#E0DBF3` | `#B6B1C0` | `#202022` | `#202022` |

#### Dark Mode
| Card Type | Background | Border | Text | Icon |
|-----------|------------|--------|------|------|
| Default | `#2A2A2D` | `#3A3A3D` | `#FFFFFF` | `#FFFFFF` |
| Elevated | `#3A3A3D` | None (shadow) | `#FFFFFF` | `#FFFFFF` |
| Cyan Accent | `#6FEDD6` | `#6FEDD6` | `#0A4A3F` | `#0A4A3F` |
| Neon Accent | `#C8FF00` | `#C8FF00` | `#2D3A00` | `#2D3A00` |
| Peach Accent | `#FFD4B8` | `#FFD4B8` | `#5A3A1A` | `#5A3A1A` |
| Purple Accent | `#5B3A8F` | `#8B5CF6` | `#FFFFFF` | `#FFFFFF` |

### 4.3 Buttons

#### Light Mode
| Button Type | Background | Text | Border | Hover BG | Pressed BG |
|-------------|------------|------|--------|----------|------------|
| Primary | `#F3BA60` | `#FFFFFF` | None | `#F5C678` | `#D89F4B` |
| Secondary | `#E0DBF3` | `#202022` | None | `#E8E4F7` | `#C6C0DD` |
| Outline | `transparent` | `#202022` | `#B6B1C0` | `#F7F4FF` | `#E0DBF3` |
| Neon | `#C8FF00` | `#2D3A00` | None | `#D4FF33` | `#A3D400` |
| Danger | `#FF6B6B` | `#FFFFFF` | None | `#FF8585` | `#E65555` |

#### Dark Mode
| Button Type | Background | Text | Border | Hover BG | Pressed BG |
|-------------|------------|------|--------|----------|------------|
| Primary | `#F3BA60` | `#FFFFFF` | None | `#F5C678` | `#D89F4B` |
| Secondary | `#B6B1C0` | `#202022` | None | `#C5C1CF` | `#9E99A7` |
| Outline | `transparent` | `#FFFFFF` | `#B6B1C0` | `#393943` | `#525257` |
| Neon | `#C8FF00` | `#2D3A00` | None | `#D4FF33` | `#A3D400` |
| Danger | `#FF6B6B` | `#FFFFFF` | None | `#FF8585` | `#E65555` |

### 4.4 Input Fields

#### Light Mode
| State | Background | Border | Text | Placeholder | Icon |
|-------|------------|--------|------|-------------|------|
| Default | `#FFFFFF` | `#C7C7C7` | `#202022` | `#A8A4AD` | `#736A6A` |
| Focus | `#FFFFFF` | `#8B5CF6` | `#202022` | `#A8A4AD` | `#8B5CF6` |
| Error | `#FFFFFF` | `#FF6B6B` | `#202022` | `#A8A4AD` | `#FF6B6B` |
| Disabled | `#F6F6F6` | `#E4E4E4` | `#A8A4AD` | `#C7C7C7` | `#A8A4AD` |

#### Dark Mode
| State | Background | Border | Text | Placeholder | Icon |
|-------|------------|--------|------|-------------|------|
| Default | `#2A2A2D` | `#525257` | `#FFFFFF` | `#928C9C` | `#B6B1C0` |
| Focus | `#2A2A2D` | `#A78BFA` | `#FFFFFF` | `#928C9C` | `#A78BFA` |
| Error | `#2A2A2D` | `#FF6B6B` | `#FFFFFF` | `#928C9C` | `#FF6B6B` |
| Disabled | `#3A3A3D` | `#3A3A3D` | `#928C9C` | `#6A6A6F` | `#928C9C` |

### 4.5 Badges & Tags

#### Light Mode
| Badge Type | Background | Text | Border |
|------------|------------|------|--------|
| Default | `#E0DBF3` | `#202022` | None |
| Success | `#E8F8EF` | `#1A6B3D` | `#45D27B` |
| Warning | `#FEF5E7` | `#6B4A1A` | `#F3BA60` |
| Danger | `#FFEBEB` | `#8B1A1A` | `#FF6B6B` |
| Info | `#EAF6FE` | `#1A4A6B` | `#7DC8F7` |
| Cyan | `#6FEDD6` | `#0A4A3F` | None |
| Neon | `#C8FF00` | `#2D3A00` | None |

#### Dark Mode
| Badge Type | Background | Text | Border |
|------------|------------|------|--------|
| Default | `#393943` | `#FFFFFF` | None |
| Success | `#1A3D2A` | `#45D27B` | `#2D6B47` |
| Warning | `#3D2F1A` | `#F3BA60` | `#6B5234` |
| Danger | `#3D1A1A` | `#FF6B6B` | `#6B2D2D` |
| Info | `#1A2F3D` | `#7DC8F7` | `#2D526B` |
| Cyan | `#6FEDD6` | `#0A4A3F` | None |
| Neon | `#C8FF00` | `#2D3A00` | None |

### 4.6 Progress Bars

#### Light Mode
| Element | Color | Hex |
|---------|-------|-----|
| Track | Light Gray | `#E4E4E4` |
| Fill (Primary) | Crunch | `#F3BA60` |
| Fill (Success) | Success Green | `#45D27B` |
| Fill (Cyan) | Sporty Cyan | `#6FEDD6` |
| Fill (Neon) | Neon Lime | `#C8FF00` |
| Fill (Purple) | Vibrant Purple | `#8B5CF6` |

#### Dark Mode
| Element | Color | Hex |
|---------|-------|-----|
| Track | Dark Gray | `#3A3A3D` |
| Fill (Primary) | Crunch | `#F3BA60` |
| Fill (Success) | Success Green | `#45D27B` |
| Fill (Cyan) | Sporty Cyan | `#6FEDD6` |
| Fill (Neon) | Neon Lime | `#C8FF00` |
| Fill (Purple) | Vibrant Purple | `#A78BFA` |

### 4.7 Modals & Dialogs

#### Light Mode
| Element | Color | Hex |
|---------|-------|-----|
| Background | White | `#FFFFFF` |
| Overlay | Black 5% | `rgba(0,0,0,0.05)` |
| Title | Lead | `#202022` |
| Body Text | Lead | `#202022` |
| Close Icon | Warm Haze | `#736A6A` |

#### Dark Mode
| Element | Color | Hex |
|---------|-------|-----|
| Background | Dark Surface | `#2A2A2D` |
| Overlay | Black 60% | `rgba(0,0,0,0.6)` |
| Title | White | `#FFFFFF` |
| Body Text | White | `#FFFFFF` |
| Close Icon | Dreamland | `#B6B1C0` |

---

## 5. Gradient Definitions

### 5.1 Light Mode Gradients

| Gradient Name | CSS | Usage |
|---------------|-----|-------|
| **Cyan Soft** | `linear-gradient(135deg, #E5FAF6 0%, #FFFFFF 100%)` | Soft backgrounds |
| **Purple Soft** | `linear-gradient(135deg, #F7F4FF 0%, #FFFFFF 100%)` | Playful sections |
| **Warm Glow** | `linear-gradient(135deg, #FFF5EE 0%, #FFFFFF 100%)` | Warm accents |
| **Crunch Gradient** | `linear-gradient(135deg, #F3BA60 0%, #F5C678 100%)` | Primary buttons |
| **Cyan Bold** | `linear-gradient(135deg, #6FEDD6 0%, #5DD4BD 100%)` | Sporty cards |
| **Neon Burst** | `linear-gradient(135deg, #C8FF00 0%, #A3D400 100%)` | High-energy elements |
| **Multi Playful** | `linear-gradient(135deg, #6FEDD6 0%, #E0DBF3 50%, #FFD4B8 100%)` | Hero sections |

### 5.2 Dark Mode Gradients

| Gradient Name | CSS | Usage |
|---------------|-----|-------|
| **Dark Purple** | `linear-gradient(135deg, #2D1B4E 0%, #202022 100%)` | Background sections |
| **Dark Cyan** | `linear-gradient(135deg, #1A4A44 0%, #2A2A2D 100%)` | Cyan-tinted sections |
| **Neon Glow** | `linear-gradient(135deg, #C8FF00 0%, #A3D400 100%)` | High-energy CTAs |
| **Purple Vibrant** | `linear-gradient(135deg, #8B5CF6 0%, #5B3A8F 100%)` | Premium features |
| **Cyan Neon** | `linear-gradient(135deg, #6FEDD6 0%, #C8FF00 100%)` | Playful sporty cards |
| **Dark Overlay** | `linear-gradient(180deg, rgba(0,0,0,0) 0%, rgba(0,0,0,0.4) 100%)` | Image overlays |

---

## 6. Implementation Guidelines

### 6.1 Jetpack Compose Implementation

```kotlin
// Color.kt
package com.example.sparin.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================
// BRAND COLORS (Global)
// ============================================
val Crunch = Color(0xFFF3BA60)
val ChineseSilver = Color(0xFFE0DBF3)
val Dreamland = Color(0xFFB6B1C0)
val Lead = Color(0xFF202022)
val CascadingWhite = Color(0xFFF6F6F6)
val WarmHaze = Color(0xFF736A6A)

// Accent Colors
val SportyCyan = Color(0xFF6FEDD6)
val NeonLime = Color(0xFFC8FF00)
val VibrantPurple = Color(0xFF8B5CF6)
val SoftPeach = Color(0xFFFFD4B8)

// ============================================
// LIGHT MODE COLORS
// ============================================
object LightColors {
    // Backgrounds
    val BgPrimary = Color(0xFFFFFFFF)
    val BgSecondary = Color(0xFFF6F6F6)
    val BgTertiary = Color(0xFFE0DBF3)
    val BgAccentCyan = Color(0xFFE5FAF6)
    val BgAccentPeach = Color(0xFFFFF5EE)
    
    // Surfaces
    val SurfaceDefault = Color(0xFFFFFFFF)
    val SurfaceVariant = Color(0xFFF7F4FF)
    val SurfaceAccent = Color(0xFFE0DBF3)
    val SurfaceCyan = Color(0xFF6FEDD6)
    val SurfaceNeon = Color(0xFFC8FF00)
    val SurfacePeach = Color(0xFFFFD4B8)
    
    // Text
    val TextPrimary = Color(0xFF202022)
    val TextSecondary = Color(0xFF736A6A)
    val TextTertiary = Color(0xFFA8A4AD)
    val TextInverse = Color(0xFFFFFFFF)
    val TextOnCyan = Color(0xFF0A4A3F)
    val TextOnNeon = Color(0xFF2D3A00)
    val TextLink = Color(0xFF8B5CF6)
    
    // CTA
    val CtaPrimary = Color(0xFFF3BA60)
    val CtaPrimaryHover = Color(0xFFF5C678)
    val CtaPrimaryPressed = Color(0xFFD89F4B)
    val CtaPrimaryDisabled = Color(0xFFF9E4C1)
    val CtaSecondary = Color(0xFFE0DBF3)
    val CtaSecondaryHover = Color(0xFFE8E4F7)
    val CtaSecondaryPressed = Color(0xFFC6C0DD)
    val CtaNeon = Color(0xFFC8FF00)
    val CtaNeonPressed = Color(0xFFA3D400)
    
    // Borders
    val BorderLight = Color(0xFFE4E4E4)
    val BorderMedium = Color(0xFFC7C7C7)
    val BorderDark = Color(0xFFA8A4AD)
    val BorderAccent = Color(0xFFB6B1C0)
    val BorderCyan = Color(0xFF6FEDD6)
    val BorderFocus = Color(0xFF8B5CF6)
    
    // Status
    val StatusSuccess = Color(0xFF45D27B)
    val StatusSuccessBg = Color(0xFFE8F8EF)
    val StatusSuccessBorder = Color(0xFFA3E6C1)
    val StatusWarning = Color(0xFFF3BA60)
    val StatusWarningBg = Color(0xFFFEF5E7)
    val StatusWarningBorder = Color(0xFFF9DCA8)
    val StatusDanger = Color(0xFFFF6B6B)
    val StatusDangerBg = Color(0xFFFFEBEB)
    val StatusDangerBorder = Color(0xFFFFB5B5)
    val StatusInfo = Color(0xFF7DC8F7)
    val StatusInfoBg = Color(0xFFEAF6FE)
    val StatusInfoBorder = Color(0xFFBEE3FB)
    
    // Icons
    val IconPrimary = Color(0xFF202022)
    val IconSecondary = Color(0xFF736A6A)
    val IconTertiary = Color(0xFFA8A4AD)
    val IconInverse = Color(0xFFFFFFFF)
    val IconAccent = Color(0xFFF3BA60)
    val IconCyan = Color(0xFF6FEDD6)
}

// ============================================
// DARK MODE COLORS
// ============================================
object DarkColors {
    // Backgrounds
    val BgPrimary = Color(0xFF202022)
    val BgSecondary = Color(0xFF2A2A2D)
    val BgTertiary = Color(0xFF3A3A3D)
    val BgPurpleDark = Color(0xFF2D1B4E)
    val BgAccentCyan = Color(0xFF1A4A44)
    val BgAccentNeon = Color(0xFF3A4500)
    
    // Surfaces
    val SurfaceDefault = Color(0xFF2A2A2D)
    val SurfaceElevated = Color(0xFF3A3A3D)
    val SurfaceVariant = Color(0xFF393943)
    val SurfaceAccent = Color(0xFFE0DBF3)
    val SurfacePurple = Color(0xFF5B3A8F)
    val SurfaceCyan = Color(0xFF6FEDD6)
    val SurfaceNeon = Color(0xFFC8FF00)
    val SurfacePeach = Color(0xFFFFD4B8)
    
    // Text
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFFB6B1C0)
    val TextTertiary = Color(0xFF928C9C)
    val TextDisabled = Color(0xFF5A5A5F)
    val TextInverse = Color(0xFF202022)
    val TextOnCyan = Color(0xFF0A4A3F)
    val TextOnNeon = Color(0xFF2D3A00)
    val TextLink = Color(0xFFA78BFA)
    
    // CTA
    val CtaPrimary = Color(0xFFF3BA60)
    val CtaPrimaryHover = Color(0xFFF5C678)
    val CtaPrimaryPressed = Color(0xFFD89F4B)
    val CtaPrimaryDisabled = Color(0xFF6B5A3D)
    val CtaSecondary = Color(0xFFB6B1C0)
    val CtaSecondaryHover = Color(0xFFC5C1CF)
    val CtaSecondaryPressed = Color(0xFF9E99A7)
    val CtaNeon = Color(0xFFC8FF00)
    val CtaNeonPressed = Color(0xFFA3D400)
    val CtaPurple = Color(0xFF8B5CF6)
    
    // Borders
    val BorderLight = Color(0xFF3A3A3D)
    val BorderMedium = Color(0xFF525257)
    val BorderDark = Color(0xFF6A6A6F)
    val BorderAccent = Color(0xFFE0DBF3)
    val BorderCyan = Color(0xFF6FEDD6)
    val BorderNeon = Color(0xFFC8FF00)
    val BorderFocus = Color(0xFFA78BFA)
    
    // Status
    val StatusSuccess = Color(0xFF45D27B)
    val StatusSuccessBg = Color(0xFF1A3D2A)
    val StatusSuccessBorder = Color(0xFF2D6B47)
    val StatusWarning = Color(0xFFF3BA60)
    val StatusWarningBg = Color(0xFF3D2F1A)
    val StatusWarningBorder = Color(0xFF6B5234)
    val StatusDanger = Color(0xFFFF6B6B)
    val StatusDangerBg = Color(0xFF3D1A1A)
    val StatusDangerBorder = Color(0xFF6B2D2D)
    val StatusInfo = Color(0xFF7DC8F7)
    val StatusInfoBg = Color(0xFF1A2F3D)
    val StatusInfoBorder = Color(0xFF2D526B)
    
    // Icons
    val IconPrimary = Color(0xFFFFFFFF)
    val IconSecondary = Color(0xFFB6B1C0)
    val IconTertiary = Color(0xFF928C9C)
    val IconInverse = Color(0xFF202022)
    val IconAccent = Color(0xFFF3BA60)
    val IconCyan = Color(0xFF6FEDD6)
    val IconNeon = Color(0xFFC8FF00)
}
```

### 6.2 Color Scheme Setup

```kotlin
// Theme.kt
package com.example.sparin.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Crunch,
    onPrimary = Color.White,
    primaryContainer = LightColors.CtaPrimaryHover,
    onPrimaryContainer = LightColors.TextPrimary,
    
    secondary = ChineseSilver,
    onSecondary = LightColors.TextPrimary,
    secondaryContainer = LightColors.SurfaceVariant,
    onSecondaryContainer = LightColors.TextPrimary,
    
    tertiary = VibrantPurple,
    onTertiary = Color.White,
    tertiaryContainer = LightColors.SurfaceVariant,
    onTertiaryContainer = LightColors.TextPrimary,
    
    background = LightColors.BgPrimary,
    onBackground = LightColors.TextPrimary,
    
    surface = LightColors.SurfaceDefault,
    onSurface = LightColors.TextPrimary,
    surfaceVariant = LightColors.SurfaceVariant,
    onSurfaceVariant = LightColors.TextSecondary,
    
    error = LightColors.StatusDanger,
    onError = Color.White,
    errorContainer = LightColors.StatusDangerBg,
    onErrorContainer = LightColors.StatusDanger,
    
    outline = LightColors.BorderMedium,
    outlineVariant = LightColors.BorderLight,
    
    scrim = Color.Black.copy(alpha = 0.05f)
)

private val DarkColorScheme = darkColorScheme(
    primary = Crunch,
    onPrimary = Color.White,
    primaryContainer = DarkColors.CtaPrimaryPressed,
    onPrimaryContainer = Color.White,
    
    secondary = DarkColors.CtaSecondary,
    onSecondary = DarkColors.TextInverse,
    secondaryContainer = DarkColors.SurfaceVariant,
    onSecondaryContainer = DarkColors.TextPrimary,
    
    tertiary = DarkColors.CtaPurple,
    onTertiary = Color.White,
    tertiaryContainer = DarkColors.SurfacePurple,
    onTertiaryContainer = Color.White,
    
    background = DarkColors.BgPrimary,
    onBackground = DarkColors.TextPrimary,
    
    surface = DarkColors.SurfaceDefault,
    onSurface = DarkColors.TextPrimary,
    surfaceVariant = DarkColors.SurfaceVariant,
    onSurfaceVariant = DarkColors.TextSecondary,
    
    error = DarkColors.StatusDanger,
    onError = Color.White,
    errorContainer = DarkColors.StatusDangerBg,
    onErrorContainer = DarkColors.StatusDanger,
    
    outline = DarkColors.BorderMedium,
    outlineVariant = DarkColors.BorderLight,
    
    scrim = Color.Black.copy(alpha = 0.6f)
)

@Composable
fun SparINTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

### 6.3 Usage Examples

```kotlin
// Example 1: Using brand colors directly
@Composable
fun PrimaryButton() {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(
            containerColor = Crunch,
            contentColor = Color.White
        )
    ) {
        Text("Get Started")
    }
}

// Example 2: Using theme-aware colors
@Composable
fun ThemedCard() {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = colors.SurfaceDefault,
            contentColor = colors.TextPrimary
        )
    ) {
        Text(
            text = "Card Content",
            color = colors.TextPrimary
        )
    }
}

// Example 3: Neon accent card
@Composable
fun NeonCard() {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = colors.SurfaceNeon,
            contentColor = colors.TextOnNeon
        )
    ) {
        Text(
            text = "Focus Block - Code Review",
            color = colors.TextOnNeon
        )
    }
}

// Example 4: Status badge
@Composable
fun SuccessBadge() {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    
    Surface(
        color = colors.StatusSuccessBg,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, colors.StatusSuccessBorder)
    ) {
        Text(
            text = "Completed",
            color = colors.StatusSuccess,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
```

### 6.4 Best Practices

#### ✅ DO:
- **Always use semantic tokens** instead of hardcoded hex values
- **Test both light and dark modes** for every component
- **Maintain contrast ratios** (minimum 4.5:1 for text, 3:1 for UI elements)
- **Use accent colors sparingly** for maximum impact
- **Apply gradients** to enhance visual hierarchy
- **Leverage neon colors** for high-energy CTAs and focus states

#### ❌ DON'T:
- Don't use hardcoded colors like `Color(0xFF...)` directly in components
- Don't mix light and dark mode colors
- Don't overuse neon colors (they should be accents, not primary)
- Don't ignore accessibility guidelines
- Don't create new colors without documenting them

### 6.5 Accessibility Guidelines

| Element Type | Minimum Contrast Ratio | Example |
|--------------|------------------------|---------|
| Large Text (18pt+) | 3:1 | Headers on backgrounds |
| Normal Text | 4.5:1 | Body text, descriptions |
| UI Components | 3:1 | Buttons, borders, icons |
| Active States | 3:1 | Focus indicators, selections |

**Note**: Neon colors (`#C8FF00`) have excellent contrast on dark backgrounds but may need adjustment on light backgrounds for accessibility.

### 6.6 Animation & Transitions

When transitioning between colors (e.g., button states, theme switching):

```kotlin
val animatedColor by animateColorAsState(
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Crunch,
    onPrimary = Color.White,
    primaryContainer = LightColors.CtaPrimaryHover,
    onPrimaryContainer = LightColors.TextPrimary,
    
    secondary = ChineseSilver,
    onSecondary = LightColors.TextPrimary,
    secondaryContainer = LightColors.SurfaceVariant,
    onSecondaryContainer = LightColors.TextPrimary,
    
    tertiary = VibrantPurple,
    onTertiary = Color.White,
    tertiaryContainer = LightColors.SurfaceVariant,
    onTertiaryContainer = LightColors.TextPrimary,
    
    background = LightColors.BgPrimary,
    onBackground = LightColors.TextPrimary,
    
    surface = LightColors.SurfaceDefault,
    onSurface = LightColors.TextPrimary,
    surfaceVariant = LightColors.SurfaceVariant,
    onSurfaceVariant = LightColors.TextSecondary,
    
    error = LightColors.StatusDanger,
    onError = Color.White,
    errorContainer = LightColors.StatusDangerBg,
    onErrorContainer = LightColors.StatusDanger,
    
    outline = LightColors.BorderMedium,
    outlineVariant = LightColors.BorderLight,
    
    scrim = Color.Black.copy(alpha = 0.05f)
)

private val DarkColorScheme = darkColorScheme(
    primary = Crunch,
    onPrimary = Color.White,
    primaryContainer = DarkColors.CtaPrimaryPressed,
    onPrimaryContainer = Color.White,
    
    secondary = DarkColors.CtaSecondary,
    onSecondary = DarkColors.TextInverse,
    secondaryContainer = DarkColors.SurfaceVariant,
    onSecondaryContainer = DarkColors.TextPrimary,
    
    tertiary = DarkColors.CtaPurple,
    onTertiary = Color.White,
    tertiaryContainer = DarkColors.SurfacePurple,
    onTertiaryContainer = Color.White,
    
    background = DarkColors.BgPrimary,
    onBackground = DarkColors.TextPrimary,
    
    surface = DarkColors.SurfaceDefault,
    onSurface = DarkColors.TextPrimary,
    surfaceVariant = DarkColors.SurfaceVariant,
    onSurfaceVariant = DarkColors.TextSecondary,
    
    error = DarkColors.StatusDanger,
    onError = Color.White,
    errorContainer = DarkColors.StatusDangerBg,
    onErrorContainer = DarkColors.StatusDanger,
    
    outline = DarkColors.BorderMedium,
    outlineVariant = DarkColors.BorderLight,
    
    scrim = Color.Black.copy(alpha = 0.6f)
)

@Composable
fun SparINTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

### 6.3 Usage Examples

```kotlin
// Example 1: Using brand colors directly
@Composable
fun PrimaryButton() {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(
            containerColor = Crunch,
            contentColor = Color.White
        )
    ) {
        Text("Get Started")
    }
}

// Example 2: Using theme-aware colors
@Composable
fun ThemedCard() {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = colors.SurfaceDefault,
            contentColor = colors.TextPrimary
        )
    ) {
        Text(
            text = "Card Content",
            color = colors.TextPrimary
        )
    }
}

// Example 3: Neon accent card
@Composable
fun NeonCard() {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = colors.SurfaceNeon,
            contentColor = colors.TextOnNeon
        )
    ) {
        Text(
            text = "Focus Block - Code Review",
            color = colors.TextOnNeon
        )
    }
}

// Example 4: Status badge
@Composable
fun SuccessBadge() {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    
    Surface(
        color = colors.StatusSuccessBg,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, colors.StatusSuccessBorder)
    ) {
        Text(
            text = "Completed",
            color = colors.StatusSuccess,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
```

### 6.4 Best Practices

#### ✅ DO:
- **Always use semantic tokens** instead of hardcoded hex values
- **Test both light and dark modes** for every component
- **Maintain contrast ratios** (minimum 4.5:1 for text, 3:1 for UI elements)
- **Use accent colors sparingly** for maximum impact
- **Apply gradients** to enhance visual hierarchy
- **Leverage neon colors** for high-energy CTAs and focus states

#### ❌ DON'T:
- Don't use hardcoded colors like `Color(0xFF...)` directly in components
- Don't mix light and dark mode colors
- Don't overuse neon colors (they should be accents, not primary)
- Don't ignore accessibility guidelines
- Don't create new colors without documenting them

### 6.5 Accessibility Guidelines

| Element Type | Minimum Contrast Ratio | Example |
|--------------|------------------------|---------|
| Large Text (18pt+) | 3:1 | Headers on backgrounds |
| Normal Text | 4.5:1 | Body text, descriptions |
| UI Components | 3:1 | Buttons, borders, icons |
| Active States | 3:1 | Focus indicators, selections |

**Note**: Neon colors (`#C8FF00`) have excellent contrast on dark backgrounds but may need adjustment on light backgrounds for accessibility.

### 6.6 Animation & Transitions

When transitioning between colors (e.g., button states, theme switching):

```kotlin
val animatedColor by animateColorAsState(
    targetValue = if (isPressed) colors.CtaPrimaryPressed else colors.CtaPrimary,
    animationSpec = tween(durationMillis = 150)
)
```

---

## 7. Quick Reference

### Light Mode Summary
- **Primary BG**: `#FFFFFF`
- **Primary Text**: `#202022`
- **Primary CTA**: `#F3BA60`
- **Accent Cyan**: `#6FEDD6`
- **Accent Neon**: `#C8FF00`

### Dark Mode Summary
- **Primary BG**: `#202022`
- **Primary Text**: `#FFFFFF`
- **Primary CTA**: `#F3BA60`
- **Accent Cyan**: `#6FEDD6`
- **Accent Neon**: `#C8FF00`

---

## 8. Colors Currently Used in Theme.kt

> 📝 **Note**: This section tracks which colors from this palette are currently implemented in the `Theme.kt` file for the Material3 ColorScheme.

### Light Theme Implementation (LightColorScheme)

#### Brand Colors Used:
| Color Token | Hex | Used As | Material3 Slot |
|-------------|-----|---------|----------------|
| `Crunch` | `#F3BA60` | Primary brand color | `primary`, `surfaceTint` |
| `ChineseSilver` | `#E0DBF3` | Secondary accent | `secondary` |
| `VibrantPurple` | `#8B5CF6` | Premium features | `tertiary` |
| `Lead` | `#202022` | Inverse surface | `inverseSurface` |

#### Light Mode Colors Used:
| Color Token | Hex | Material3 Slot | Purpose |
|-------------|-----|----------------|---------|
| `LightColors.BgPrimary` | `#FFFFFF` | `background` | Main screen background |
| `LightColors.SurfaceDefault` | `#FFFFFF` | `surface` | Card surfaces |
| `LightColors.SurfaceVariant` | `#F7F4FF` | `surfaceVariant`, `secondaryContainer`, `tertiaryContainer` | Playful containers |
| `LightColors.TextPrimary` | `#202022` | `onPrimary`, `onSecondary`, `onBackground`, `onSurface` | Primary text |
| `LightColors.TextSecondary` | `#736A6A` | `onSurfaceVariant` | Secondary text |
| `LightColors.CtaPrimaryHover` | `#F5C678` | `primaryContainer`, `inversePrimary` | Primary button hover |
| `LightColors.StatusDanger` | `#FF6B6B` | `error`, `onErrorContainer` | Error states |
| `LightColors.StatusDangerBg` | `#FFEBEB` | `errorContainer` | Error background |
| `LightColors.BorderMedium` | `#C7C7C7` | `outline` | Standard borders |
| `LightColors.BorderLight` | `#E4E4E4` | `outlineVariant` | Subtle borders |

**Scrim**: Black with 5% opacity (`rgba(0,0,0,0.05)`)

### Dark Theme Implementation (DarkColorScheme)

#### Brand Colors Used:
| Color Token | Hex | Used As | Material3 Slot |
|-------------|-----|---------|----------------|
| `Crunch` | `#F3BA60` | Primary brand color | `primary`, `surfaceTint` |
| `Dreamland` | `#B6B1C0` | Secondary neutral | `secondary` |
| `CascadingWhite` | `#F6F6F6` | Inverse surface | `inverseSurface` |
| `Lead` | `#202022` | Inverse text | `inverseOnSurface` |

#### Dark Mode Colors Used:
| Color Token | Hex | Material3 Slot | Purpose |
|-------------|-----|----------------|---------|
| `DarkColors.BgPrimary` | `#202022` | `background` | Main screen background |
| `DarkColors.SurfaceDefault` | `#2A2A2D` | `surface` | Card surfaces |
| `DarkColors.SurfaceVariant` | `#393943` | `surfaceVariant`, `secondaryContainer` | Variant containers |
| `DarkColors.SurfacePurple` | `#5B3A8F` | `tertiaryContainer` | Premium containers |
| `DarkColors.TextPrimary` | `#FFFFFF` | `onBackground`, `onSurface`, `onSecondaryContainer` | Primary text |
| `DarkColors.TextSecondary` | `#B6B1C0` | `onSurfaceVariant` | Secondary text |
| `DarkColors.TextInverse` | `#202022` | `onSecondary` | Text on bright surfaces |
| `DarkColors.CtaPrimary` | `#F3BA60` | Same as Crunch | Primary CTA |
| `DarkColors.CtaPrimaryPressed` | `#D89F4B` | `primaryContainer`, `inversePrimary` | Pressed state |
| `DarkColors.CtaPurple` | `#8B5CF6` | `tertiary` | Premium actions |
| `DarkColors.StatusDanger` | `#FF6B6B` | `error`, `onErrorContainer` | Error states |
| `DarkColors.StatusDangerBg` | `#3D1A1A` | `errorContainer` | Error background |
| `DarkColors.BorderMedium` | `#525257` | `outline` | Standard borders |
| `DarkColors.BorderLight` | `#3A3A3D` | `outlineVariant` | Subtle borders |

**Scrim**: Black with 60% opacity (`rgba(0,0,0,0.6)`)

### Colors Available But Not Yet Used in Theme

These colors are defined in `Color.kt` but not yet mapped to Material3 ColorScheme. They are available for direct use in components:

#### Accent Colors:
- `SportyCyan` (`#6FEDD6`) - For cards, badges, playful elements
- `NeonLime` (`#C8FF00`) - For high-energy CTAs and focus states
- `SoftPeach` (`#FFD4B8`) - For warm accents and secondary cards

#### Status Colors:
- Success: `StatusSuccess`, `StatusSuccessBg`, `StatusSuccessBorder`
- Warning: `StatusWarning`, `StatusWarningBg`, `StatusWarningBorder`
- Info: `StatusInfo`, `StatusInfoBg`, `StatusInfoBorder`

#### Special Surfaces:
- `LightColors.SurfaceCyan`, `SurfaceNeon`, `SurfacePeach` (Light Mode)
- `DarkColors.SurfaceCyan`, `SurfaceNeon`, `SurfacePeach` (Dark Mode)
- `DarkColors.BgPurpleDark` - For special dark sections

#### Legacy Colors:
Sport category colors, navigation bar colors, neumorphism shadows, and other specialty colors remain available for component-specific usage.

### Usage Recommendations

✅ **Recommended:**
```kotlin
// Using theme colors (best practice)
Text(
    text = "Hello",
    color = MaterialTheme.colorScheme.onSurface
)

// Using theme-aware colors
val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
Card(
    colors = CardDefaults.cardColors(
        containerColor = colors.SurfaceDefault
    )
)
```

❌ **Not Recommended:**
```kotlin
// Avoid hardcoding colors
Text(
    text = "Hello",
    color = Color(0xFF202022) // Don't do this!
)
```

---

## 9. Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2025-12-01 | Initial color palette system with full light/dark mode support |
| 1.1.0 | 2025-12-03 | Added complete Theme.kt implementation with Material3 ColorScheme mapping |

---

**Maintained by**: SparIN Design Team  
**Last Updated**: December 3, 2025  
**Status**: ✅ Production Ready