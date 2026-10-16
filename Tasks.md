# Tasks

## Decisions

- Android app, Kotlin + Jetpack Compose, minSdk 26, compileSdk 36
- Books stored on-device with Room (SQLite), covers copied to app storage
- Design: "Draft 1 — Cartoon" from the design canvas (Main, AddBook, BookDetail)
  - ink `#2B2140`, 3dp outlines, solid offset shadows (no blur), radius 6dp
  - background `#F1ECFF`, coral `#FF8A65`, yellow `#FFC94A`, sky `#7DD3E8`, lilac `#B9A6FF`
  - Fredoka (headings) + Nunito (body), bundled in `res/font`
- Book info comes from free APIs, no AI and no backend (#6)
  - Google Books (title, author, description, ISBN), Open Library (covers, fallback), Wikipedia (summary fallback)
  - ISBN barcode via Google code scanner, cover text via ML Kit OCR, both on-device and free
  - API keys are entered in the app's Settings screen, never in the source or APK, and excluded from backups
- CLAUDE.md JSX rules applied in spirit: inline params, one composable per file, local types at file end
- Issues live on GitHub, one per task, branch `feat/<issue>-<slug>`
- Branches #2 → #7 → #6 are stacked, each starts from the previous one, merge in order
- Commits get `[n]` from the `prepare-commit-msg` hook (`core.hooksPath = .githooks`)

## Progress

### #1 GitHub issues and PR script — done
Branch `feat/1-github-script`
- [x] `issue list | view | create | edit | close | comment`
- [x] `pr list | create`

### #2 Scaffold + comic theme — done
Branch `feat/2-scaffold-theme`
- [x] Gradle project, wrapper, .gitignore
- [x] Theme tokens, fonts, comic modifiers
- [x] Room database, Book entity, repository
- [x] Navigation shell (shelf, add/edit, detail)

### #3 Shelf grid — done
Branch `feat/3-shelf-grid`
- [x] Grouping by day / week / year with separators + counts
- [x] 2 / 3 / 5 column density, titles hidden in Years
- [x] Days/Weeks/Years segmented control + pinch zoom
- [x] Cover cards with dotted initial placeholder
- [x] Extended "Add book" FAB, card opens details
- [x] Unit tests for grouping

### #4 Add a book — done
Branch `feat/4-add-book`
- [x] Cover from Camera or Gallery (optional), change photo
- [x] Title required, red border + message on blocked save
- [x] Suggest (photo → title + description, title → description, disabled hint), re-suggest, Undo
- [x] Today / Yesterday chips + date picker + shelf group hint
- [x] "Add to shelf" saves, reused for editing

### #5 Book details — done
Branch `feat/5-book-detail`
- [x] Tilted cover, title, read-date chip, photo-source chip, description card
- [x] Edit opens the form, Delete asks for confirmation
- [x] Back to shelf
- [ ] Visual check on a device or emulator (headless emulator failed to boot)

### #7 Shelf search — done
Branch `feat/7-shelf-search` (cut from `feat/5-book-detail`)
- [x] Search by title and description (case and accent insensitive, every word must match)
- [x] Results keep the comic card look and date grouping
- [x] Search button in header, close button and system back clear the search
- [x] Unit tests for filtering
- [ ] Visual check on a device or emulator

### #6 Online book lookup — done
Branch `feat/6-book-lookup` (cut from `feat/7-shelf-search`, replaces the stub "Suggest")
- [x] Data: `author`, `isbn`, `Online` cover source, Room auto-migration 1 → 2
- [x] Lookup: Google Books → Open Library fallback, description from Open Library work or Wikipedia
- [x] Description cleanup (HTML, entities, length cut at a sentence)
- [x] Scan ISBN barcode → exact lookup
- [x] Typed title → top matches to pick from, best one applied, Undo
- [x] Cover photo → OCR → search
- [x] Online cover downloaded when the book has no photo
- [x] Author field in the form, author on details, search includes author
- [x] Settings screen: optional Google Books API key, test and remove, kept out of backups
- [x] Friendly errors when offline or nothing found
- [x] Unit tests for parsing and text helpers
- [x] README with setup steps
- [ ] Visual check on a device or emulator

## Next

- Visual check of #5, #7 and #6 on a device with Google Play services
- Push branches #1–#7, open PRs (stacked: #3 on #2, #4 on #3, #5 on #4, #7 on #5, #6 on #7)
