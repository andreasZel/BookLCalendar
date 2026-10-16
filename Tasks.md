# Tasks

## Decisions

- Android app, Kotlin + Jetpack Compose, minSdk 26, compileSdk 36
- Books stored on-device with Room (SQLite), covers copied to app storage
- Design: "Draft 1 — Cartoon" from the design canvas (Main, AddBook, BookDetail)
  - ink `#2B2140`, 3dp outlines, solid offset shadows (no blur), radius 6dp
  - background `#F1ECFF`, coral `#FF8A65`, yellow `#FFC94A`, sky `#7DD3E8`, lilac `#B9A6FF`
  - Fredoka (headings) + Nunito (body), bundled in `res/font`
- "Suggest" uses a stub `SuggestionService`, real AI later
- CLAUDE.md JSX rules applied in spirit: inline params, one composable per file, local types at file end
- No issue tracker script yet, so branches have no issue numbers

## Progress

### 1. Scaffold + comic theme — done
Branch `feat/scaffold-theme`
- [x] Gradle project, wrapper, .gitignore
- [x] Theme tokens, fonts, comic modifiers
- [x] Room database, Book entity, repository
- [x] Navigation shell (shelf, add/edit, detail)

### 2. Shelf grid — done
Branch `feat/shelf-grid`
- [x] Grouping by day / week / year with separators + counts
- [x] 2 / 3 / 5 column density, titles hidden in Years
- [x] Days/Weeks/Years segmented control + pinch zoom
- [x] Cover cards with dotted initial placeholder
- [x] Extended "Add book" FAB, card opens details
- [x] Unit tests for grouping

### 3. Add a book — done
Branch `feat/add-book`
- [x] Cover from Camera or Gallery (optional), change photo
- [x] Title required, red border + message on blocked save
- [x] Suggest (photo → title + description, title → description, disabled hint), re-suggest, Undo
- [x] Today / Yesterday chips + date picker + shelf group hint
- [x] "Add to shelf" saves, reused for editing

### 4. Book details — in progress
Branch `feat/book-detail`
- [ ] Tilted cover, title, read-date chip, photo-source chip, description card
- [ ] Edit opens the form, Delete asks for confirmation
- [ ] Back to shelf

## Next

- Create `scripts/github.mjs` and matching issues, then rename branches to `feat/<issue>-<slug>`
- Replace stub suggestions with a real vision/text model
- Search button on the shelf header (shown in the design, not wired yet)
