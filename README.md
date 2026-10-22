<div align="center">

# 📦 Trackie

**Smart parcel management for residential communities.**

An Android app that helps hostel, PG, and apartment administrators log incoming parcels, and lets residents track their deliveries, raise complaints, and get reminders for uncollected packages.

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Language-Java-ED8B00?logo=openjdk&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSdk-24-blue)
![Target SDK](https://img.shields.io/badge/targetSdk-34-blue)
![Status](https://img.shields.io/badge/status-prototype-orange)

</div>

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [App Flow](#app-flow)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Current Limitations](#current-limitations)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

In shared residences, parcels pile up at the front desk, residents don't know when something has arrived, and nobody follows up on packages that sit uncollected for weeks. Trackie digitizes this workflow with two roles:

- **Admin (warden / security):** logs arriving parcels, browses them by category, and sees which ones need follow-up.
- **Resident:** sees their own parcels, looks up orders by name or tracking ID, and files complaints.

The app is currently set up around a multi-building housing network (the "Serenity" buildings, such as New Boys, New Girls, Empire, Homes, and Tulip), and is easy to adapt to other communities.

## Features

### For Admins
- **Parcel logging:** add a parcel manually (resident name, phone, building, room, category, brand, arrival date) with name autocomplete.
- **Camera capture:** photograph a parcel at the desk and save its details in one flow.
- **Pending packages dashboard:** a list of all parcels with a sort menu.
- **Category browsing:** e-commerce, food items, instants, and handle-with-care items.
- **Reminders:** parcels uncollected for 10 or more days are flagged automatically, with an "Arrived Today" tab for new entries.
- **Notifications** center for resident messages and warden updates.

### For Residents
- **My Parcels:** a personalized list that matches parcels to the logged-in resident.
- **Track Order:** search by resident name or tracking ID.
- **File a Complaint:** categories such as Parcel Not Received, Damaged Parcel, Wrong Parcel Delivered, Delayed Delivery, Maintenance, Noise, Cleanliness, and Staff Behaviour.
- **Return Items** and **E-commerce shortcuts** (Amazon, Flipkart, Myntra, Nykaa, Ajio, and more).

### General
- Animated splash screen and sliding banner carousel
- Role-based login (Resident / Admin) with building selection
- Sign-up with email and password validation
- Phone OTP dialog, plus Google and Apple sign-in entry points
- Forgot password flow
- Profile and Settings screens (privacy policy, cancellation policy, delete account)
- Light and dark theme resources

## App Flow

```
Splash (MainActivity)
   └── LoginActivity ── choose role
          └── StudentWardenLoginActivity (Resident / Admin)
                 ├── Resident → ResidentDashboardActivity
                 │      ├── My Parcels · Track Order · Return Items
                 │      ├── File Complaint
                 │      └── Profile · Settings · Notifications
                 └── Admin → AdminDashboardActivity
                        ├── Add Parcel · Camera Parcel
                        ├── Categories · Reminders
                        └── Profile · Settings · Notifications
```

## Tech Stack

| Area | Technology |
| --- | --- |
| Language | Java (Android), with Kotlin Gradle plugin configured |
| Build system | Gradle (Kotlin DSL) |
| UI | XML layouts, Material Components, ConstraintLayout |
| Lists and paging | RecyclerView, ViewPager2 (banner slider) |
| Auth services | Google Play Services Auth |
| Data layer | In-memory singleton repository (`ParcelRepository`) |
| Testing | JUnit 4, AndroidX Test, Espresso |

**Android configuration:** `compileSdk 34` · `minSdk 24` (Android 7.0+) · `targetSdk 34` · package `com.example.myapplication`

## Project Structure

```
Trackie/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/example/myapplication/
│       │   │   ├── MainActivity.java                # Splash screen
│       │   │   ├── LoginActivity.java               # Role selection
│       │   │   ├── StudentWardenLoginActivity.java  # Resident / Admin login
│       │   │   ├── SignupActivity.java
│       │   │   ├── ForgotPasswordActivity.java
│       │   │   ├── AdminDashboardActivity.java
│       │   │   ├── ResidentDashboardActivity.java
│       │   │   ├── AddParcelActivity.java
│       │   │   ├── CameraParcelActivity.java
│       │   │   ├── ParcelRepository.java            # Parcel data + reminder logic
│       │   │   ├── RemindersActivity.java
│       │   │   ├── TrackOrderActivity.java
│       │   │   ├── FileComplaintActivity.java
│       │   │   ├── NotificationsActivity.java
│       │   │   ├── ProfileActivity.java
│       │   │   ├── SettingsActivity.java
│       │   │   └── ...                              # Categories, E-commerce, Returns
│       │   └── res/                                 # Layouts, drawables, themes, animations
│       ├── test/                                    # Unit tests
│       └── androidTest/                             # Instrumented tests
├── gradle/
├── build.gradle.kts
└── settings.gradle.kts
```

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (Giraffe or newer recommended)
- JDK 8 or higher
- An Android device or emulator running **Android 7.0 (API 24)** or above

### Installation

```bash
# Clone the repository
git clone https://github.com/dg-dew/Trackie.git
cd Trackie
```

1. Open the project in **Android Studio**.
2. Let Gradle sync finish.
3. Select a device or emulator.
4. Click **Run ▶** (or press `Shift + F10`).

### Command-line build

```bash
# Build a debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test
```

The APK is generated in `app/build/outputs/apk/debug/`.

## Usage

1. Launch the app and wait for the splash screen.
2. Choose **Resident** or **Admin**.
3. Enter a username, select a building, and enter a password.
   - Use a name from the preloaded data (for example **Rahul Sharma**) to see sample parcels on the resident dashboard.
4. **As Admin:** tap **+** to add a parcel, or the camera button to capture one. Use the bottom bar to open Reminders.
5. **As Resident:** view your parcels, use **Track** to search, or **Complaint** to report an issue.

## Current Limitations

Trackie is an early-stage prototype. Be aware of the following:

- **No backend:** parcels live in memory and reset when the app closes.
- **Login is not authenticated:** any non-empty username, building, and password lets you in. Google and Apple buttons open the provider's website, and the OTP flow is simulated.
- **Notifications are static** sample data.
- **Camera captures** store only a thumbnail and don't persist it.
- **Return Items** and the **Parcels** screen are placeholders.
- Seed data uses sample names and phone numbers.
- The project still uses the default package name (`com.example.myapplication`) and app module name ("My Application").

## Roadmap

- [ ] Persistent storage (Room) or a cloud backend (e.g. Firebase)
- [ ] Real authentication with role-based access
- [ ] Push notifications when a parcel arrives
- [ ] Full-resolution parcel photos
- [ ] QR / barcode scanning for tracking IDs
- [ ] Complete the Return Items flow
- [ ] Admin analytics (parcels per building, average pickup time)
- [ ] Rename the package and application module
- [ ] Add unit and UI tests for core flows

## Contributing

Contributions are welcome.

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m "Add your feature"`
4. Push the branch: `git push origin feature/your-feature`
5. Open a Pull Request.

## License

No license has been specified yet. Add a `LICENSE` file (for example, [MIT](https://choosealicense.com/licenses/mit/)) to define how others may use this project.

---

<div align="center">

Built with ☕ for smoother parcel pickups.

</div>
