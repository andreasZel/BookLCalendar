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

### 2. Shelf grid — in progress
Branch `feat/shelf-grid`
- [ ] Grouping by day / week / year with separators + counts
- [ ] 2 / 3 / 5 column density, titles hidden in Years
- [ ] Days/Weeks/Years segmented control + pinch zoom
- [ ] Cover cards with dotted initial placeholder
- [ ] Extended "Add book" FAB, card opens details
- [ ] Unit tests for grouping

### 3. Add a book — todo
Branch `feat/add-book`
- [ ] Cover from Camera or Gallery (optional), change photo
- [ ] Title required, red border + message on blocked save
- [ ] Suggest (photo → title + description, title → description, disabled hint), re-suggest, Undo
- [ ] Today / Yesterday chips + date picker + shelf group hint
- [ ] "Add to shelf" saves, reused for editing

### 4. Book details — todo
Branch `feat/book-detail`
- [ ] Tilted cover, title, read-date chip, photo-source chip, description card
- [ ] Edit opens the form, Delete asks for confirmation
- [ ] Back to shelf

## Next

- Create `scripts/github.mjs` and matching issues, then rename branches to `feat/<issue>-<slug>`
- Replace stub suggestions with a real vision/text model
- Search button on the shelf header (shown in the design, not wired yet)
