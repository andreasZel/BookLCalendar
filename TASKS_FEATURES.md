# Feature Tasks

New functionality after #1–#7. Issues #15–#25, one per task.

## Decisions

### Services and cost
- Google Books API is free: no billing and no credits. Each Cloud project has a daily request quota (commonly 1,000/day). The anonymous shared quota is often used up (it returned 429 during research), so a personal key is still recommended
- Google Chirp (Cloud Speech-to-Text V2) is paid: 60 free minutes/month, then about $0.016/min. It needs a billing-enabled Cloud project and a key or backend, so it is not used
- Voice input uses Android's built-in speech recognizer (`RecognizerIntent`): free, no key, supports Greek. It runs on the phone on Android 12+ when the language pack is installed, otherwise Google's free system service handles it. No RECORD_AUDIO permission is needed because the system dialog records

### Reading calendar
- A day holds at most one `ReadLog` per shelf book: `id`, `bookId`, `date`, `pagesRead`, `note` (max 280 chars), `createdAt`
- Unique index on `bookId + date`. Logging the same book on the same day again edits the existing log. Deleting a book deletes its logs (foreign key cascade)
- `Book` gets an optional `totalPages` that drives a progress bar (sum of `pagesRead` / `totalPages`)
- Room auto-migration 2 → 3 adds the `read_logs` table and `books.totalPages`

### Streaks and achievements
- Streak: consecutive days with at least one log. Today with no log yet doesn't break it until the day ends
- Achievements are calculated from books and logs, not stored. Only the unlock date of each badge is saved (DataStore/prefs), so the "new badge" pop-up shows once
- Badges use the comic style (ink outline, offset shadow, palette colours). Locked badges are greyed with a dotted outline

### Greek books
- Why Greek lookups fail today:
  - `lookup/CoverReader.kt` uses ML Kit's Latin recognizer. ML Kit v2 has no Greek script, so Greek covers can't be read
  - Open Library has thin Greek coverage. "Ζορμπά" returns one result
  - `lookup/Wikipedia.kt` only queries `en.wikipedia.org`
  - Google Books searches with no language hint
- "Book languages" setting: default comes from the device locale (Greek on when the locale is `el`). It can be changed in Settings: English, Greek, or English + Greek. It controls Greek OCR, `langRestrict`, Wikipedia language and Biblionet
- Biblionet (biblionet.gr/webservice, `get_title` by ISBN) is the Greek books database. It needs credentials approved by Biblionet, entered in Settings like the Google key and kept out of backups

## Progress

### #15 Fix: "Find online" button cropped on the right — done
Branch `fix/15-find-online-crop`
- [x] Button row in `ui/add/AddBookScreen.kt:162` uses `padding(end = 2.dp, bottom = 2.dp)`, but `ComicButton` draws a 3dp shadow and the cream card clips it. Match the padding to the shadow
- [x] Stop "Scan ISBN" from squeezing the label: `ComicButton` labels are single-line with ellipsis, "Scan ISBN" keeps its width and the lookup button takes the rest. Tighter inner padding (10dp) and icon gap (6dp) in this row
- [ ] Check on a 360dp-wide screen with the longest label ("Find by title"), needs a device or emulator

### #16 Reading log data — done
Branch `feat/16-read-log-data`
- [x] `ReadLog` entity, `ReadLogDao` (by month, by day, by book, upsert, delete)
- [x] `totalPages` on `Book`, auto-migration 2 → 3, export schema
- [x] Repository methods + wiring in `AppContainer`
- [x] Unit tests for month aggregation (pages per day)

### #17 Calendar screen — done
Branch `feat/17-calendar` (from `master`, independent of #15)
- [x] Month grid, Monday first, heat dots by pages read, today ring, swipe or arrows between months
  - Dot colour by pages that day: under 20, 20–49, 50+ (a 0-page log still gets the lightest dot). Legend under the grid
  - Future days are muted and can't be tapped
- [x] Tap a day to open a sheet with that day's logs (cover, pages, note). A row opens the book's details
- [x] Calendar button in the shelf header, `calendar` route in `ui/AppNavHost.kt`. The shelf's book badge hides under 400dp width so the title fits next to 4 buttons
- [x] Empty state for days and months with no reading
- [x] Unit tests for month cells, heat levels and labels
- [ ] Visual check on a device or emulator

### #18 Log a read — done
Branch `feat/18-log-read`
- [x] Bottom sheet (`ui/log/LogReadSheet.kt`): pick a shelf book (searchable), pages stepper + number field (0–9999), note field with 280 counter
- [x] Opens prefilled when that book already has a log on that day, with a delete option (tap twice to confirm)
- [x] "Log reading" button on book details, prefilled with the book and today. Also in the calendar day sheet for that day, which reopens after saving
- [x] Total pages field in the add/edit form, reading card with progress bar and summary on details
- [x] Unit tests for the pages/note helpers and the reading summary
- [ ] Visual check on a device or emulator

### #19 Voice notes — done
Branch `feat/19-voice-notes`
- [x] Mic button on the note field launches `RecognizerIntent.ACTION_RECOGNIZE_SPEECH`
  - `ui/log/SpeechInput.kt` builds the intent and reads the first result. Sky mic button next to the note field
- [x] Language from the Book languages setting (`el-GR` / `en-US`), read from `LanguagePrefs` when the mic is tapped
- [x] Recognized text is appended to the note, then cut to 280 chars (`appendNote` in `ui/log/LogForm.kt`, one space between)
- [x] Mic hidden when no recognizer is available (`SpeechRecognizer.isRecognitionAvailable`), or after the launch fails
  - `<queries>` for `RecognitionService` and `RECOGNIZE_SPEECH` in the manifest, needed on Android 11+
- [x] Unit tests for `appendNote`
- [ ] Check Greek and English dictation on a device

### #20 Streaks — done
Branch `feat/20-streaks`
- [x] Pure functions for current and best streak from log dates + unit tests (gaps, today open, month edges)
  - `data/Streaks.kt` (`currentStreak`, `bestStreak`, `streakOf`), future dates ignored. `ReadLogRepository.observeStreak()` over a distinct-dates query
- [x] Flame pill with the current streak in the shelf and calendar headers
  - `ui/components/StreakPill.kt`, yellow pill with a coral flame, next to the subtitle so narrow headers keep their buttons. Hidden at 0, screen readers hear the best streak too
  - Streak is recomputed when logs change or the screen reopens, not exactly at midnight
- [ ] Visual check on a device or emulator

### #21 Achievements and badges — todo
Branch `feat/21-achievements`
- [ ] Badge rules: first book, 5 / 25 / 100 books, 7 / 30 / 100-day streaks, 1,000 / 10,000 pages, first finished book, first Greek book, a book every month for a year
- [ ] Achievements screen: unlocked badges with date, locked badges with progress ("12 / 25 books")
- [ ] "New badge" pop-up once when one unlocks, unlock dates saved
- [ ] Unit tests for each rule

### #22 Book languages setting — done
Branch `feat/22-book-languages`
- [x] Setting in Settings, default from `Locale.getDefault()`, stored in prefs
  - `settings/BookLanguages.kt` (English, Greek, Both, `speechTag`), `settings/LanguagePrefs.kt` in `app_settings` prefs, which is backed up
  - Greek device locale defaults to English + Greek, any other locale to English
  - "Book languages" card with chips at the top of Settings
- [x] Exposed from `AppContainer` (`languages`) to lookup and voice input. `OnlineBookLookup` receives it, used from #23
- [x] Unit tests for the locale default, keys and speech tag
- [ ] Visual check on a device or emulator

### #23 Greek search tuning — todo
Branch `feat/23-greek-search`
- [ ] Detect Greek script in the query, then add `langRestrict=el` in `lookup/GoogleBooks.kt`. Retry without it when there are no results
- [ ] Accent- and final-sigma-insensitive matching and dedupe in `lookup/OnlineBookLookup.kt`
- [ ] `lookup/Wikipedia.kt` uses `el.wikipedia.org` for Greek titles, with Greek "bookish" keywords (μυθιστόρημα, βιβλίο, ποίηση…)
- [ ] Unit tests with Greek fixtures

### #24 Greek cover OCR — todo
Branch `feat/24-greek-ocr`
- [ ] Tesseract4Android with `ell` + `eng` traineddata, downloaded on first use (not in the APK) to app storage
- [ ] `lookup/CoverReader.kt` uses Tesseract when Greek is on or ML Kit finds no usable text
- [ ] Progress and error messages for the model download
- [ ] Check that the APK size increase is acceptable

### #25 Biblionet ISBN lookup — todo
Branch `feat/25-biblionet`
- [ ] Biblionet username/password in Settings (`settings/ApiKeys.kt`), test and remove, kept out of backups
- [ ] Greek ISBNs (978-960, 978-618) go to Biblionet first, then the existing chain
- [ ] Parse `Title`, `WriterName`, `Summary`, `CoverImage` (relative path on biblionet.gr) into `BookMatch`
- [ ] Parse tests from a saved sample response
- [ ] README: how to request Biblionet access

## Next

- Order: #15 (quick fix) → #16 → #17 → #18 → #19 → #20 → #21. The Greek tasks #22 → #23 → #24 → #25 can run in parallel; #19 needs #22 for the voice language
