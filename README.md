# BookLCalendar

An Android app for logging the books you've read. Each book goes on a shelf grouped by the day, week or year you read it. The app can fill in the title, author, description and cover for you by scanning the barcode, reading the cover photo or searching by title.

Everything is stored on the phone. There is no account and no backend.

## Requirements

- Android Studio (recent stable) or JDK 17 with the Android SDK, platform 36
- A phone or emulator running Android 8.0 (API 26) or newer
- Google Play services on that device, for the barcode scanner and cover text reading. Use an emulator image whose name includes "Google Play" or "Google APIs".

## Build and run

1. Clone the repository.
2. Open the folder in Android Studio and let Gradle sync. Android Studio creates `local.properties` with your SDK path.
   From the command line instead, create `local.properties` yourself:
   ```
   sdk.dir=C\:\\Users\\<you>\\AppData\\Local\\Android\\Sdk
   ```
3. Run the `app` configuration, or from a terminal:
   ```
   ./gradlew installDebug
   ```
   On Windows PowerShell use `.\gradlew.bat installDebug`.

Run the unit tests with:
```
./gradlew testDebugUnitTest
```

## Book lookup

On the "New book" screen you have three ways to fill in a book:

| Action | How it works |
|---|---|
| **Scan ISBN** | Opens Google's barcode scanner and reads the ISBN on the back cover, then looks up that exact edition |
| **Find by title** | Searches by the typed title (and author, if filled in) and shows up to 4 matches to pick from |
| **Read the cover** | Reads the text on your cover photo on the phone, then searches with it |

The best match is filled in right away. You can tap another match to switch, or tap Undo to go back to what you had. If the book has no photo of your own, the online cover is downloaded and saved with it.

The data comes from free public services:

| Service | Used for | Key |
|---|---|---|
| [Google Books](https://developers.google.com/books) | Title, author, description, ISBN, small cover | Optional, see below |
| [Open Library](https://openlibrary.org/developers/api) | Fallback search, large covers, descriptions | None |
| [Wikipedia](https://en.wikipedia.org/api/rest_v1/) | Short summary when no description is found | None |

The barcode scanner and cover text reading use Google ML Kit. Both run on the phone, are free and need no key. The first time you use one, Play services may take a moment to download it.

## Optional: Google Books API key

The app works without a key. Without one, Google Books shares a small free quota with every app that calls it anonymously, and it often answers "too many requests". When that happens the app falls back to Open Library, which works but usually has weaker descriptions.

A free personal key gives you your own daily quota.

### Get a key

1. Go to the [Google Cloud console](https://console.cloud.google.com/) and create a project (or pick an existing one).
2. Open **APIs & Services → Library**, search for **Books API** and click **Enable**.
3. Open **APIs & Services → Credentials → Create credentials → API key**.
4. Click the new key and, under **API restrictions**, choose **Restrict key** and tick only **Books API**. Save.
   Leave **Application restrictions** set to **None**. The app calls the API directly and does not send the Android package signature that an "Android apps" restriction checks.

The Books API has no charge and does not need billing enabled.

### Add it to the app

1. On the shelf, tap the key button in the top-right corner to open **Settings**.
2. Paste the key into **Google Books API key** and tap **Save & test**.
3. The status line under the buttons shows the result:
   - **Key saved and working**: all set.
   - **Google rejected this key, not saved**: check that you copied the whole key and that the Books API is enabled.
   - **Saved, couldn't test it offline**: the key is saved and will be tested on the next lookup.

Tap **Remove** to delete the key and go back to the shared quota.

### Where the key is kept

- Keys are never written in the source code or build files, so they are never in the APK.
- The key you enter is stored in the app's private storage (`shared_prefs/api_keys.xml`), which other apps can't read.
- That file is left out of Android cloud backups and device-to-device transfers. After reinstalling or moving to a new phone, enter the key again.

## Troubleshooting

- **"Couldn't reach the book databases"**: the phone is offline, or every service failed. Try again when you're connected.
- **"The scanner is getting ready"**: Play services is still downloading the scanner. Wait a few seconds and try again.
- **Scanner or cover reading does nothing on an emulator**: the emulator image has no Google Play services. Use a "Google Play" image or a real phone.
- **"That barcode isn't a book ISBN"**: you scanned a price or store barcode. Book ISBNs start with 978 or 979.
- **Lookups are slow or descriptions are thin without a key**: Google's shared quota is used up and the app is on Open Library. Add a key as described above.

## Contributing

- Read `Tasks.md` first. It holds progress, decisions and the next task.
- Enable the repository's git hooks once after cloning:
  ```
  git config core.hooksPath .githooks
  ```
  The `prepare-commit-msg` hook adds `[<issue>]` to commit messages based on the `feat/<issue>-<slug>` or `fix/<issue>-<slug>` branch name.
- Code style rules are in `CLAUDE.md`.

## Project layout

```
app/src/main/java/com/andreaszel/booklcalendar/
  data/       Room database, Book entity, cover file storage
  lookup/     Google Books, Open Library, Wikipedia, barcode scan, cover text reading
  settings/   API key storage
  ui/         Compose screens: shelf, add/edit, details, settings, shared components
```
