# Tasks

## Decisions

- Android app, Kotlin + Jetpack Compose, minSdk 26, compileSdk 36
- Books stored on-device with Room (SQLite), covers copied to app storage
- Design: "Draft 1 — Cartoon" from the design canvas (Main, AddBook, BookDetail)
  - ink `#2B2140`, 3dp outlines, solid offset shadows (no blur), radius 6dp
  - background `#F1ECFF`, coral `#FF8A65`, yellow `#FFC94A`, sky `#7DD3E8`, lilac `#B9A6FF`
  - Fredoka (headings) + Nunito (body), bundled in `res/font`
- "Suggest" uses a stub `SuggestionService`, real AI later (#6)
- CLAUDE.md JSX rules applied in spirit: inline params, one composable per file, local types at file end
- Issues live on GitHub, one per task, branch `feat/<issue>-<slug>`
- Branches #2 → #5 are stacked, each starts from the previous one, merge in order
- Commits made before the issues existed have no `[n]` prefix, their branch name carries the issue

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

### #6 Real AI suggestions — todo
Branch `feat/6-ai-suggestions`
- [ ] Cover photo → title + description (vision)
- [ ] Title → short description
- [ ] Keep the API key off the device (backend or proxy)

### #7 Shelf search — todo
Branch `feat/7-shelf-search`
- [ ] Search by title and description
- [ ] Results keep the comic card look

## Next

- Push branches #1–#5 and open PRs (stacked: #3 on #2, #4 on #3, #5 on #4)
- Start #6 or #7
