# 001 Repo setup

This task created the MealIn git history and the agent rules. It did not copy the food catalog and it did not add nutrition tests.

## Committed

First commit `1c8723a1f58d82065f1c00742cfd1cc5f698cab6`:

```
Add the native-UI Kotlin Multiplatform skeleton.
Include the existing source, Gradle wrapper, README, and .gitignore.
Exclude build output, IDE files, and local.properties.
```

That commit is the wizard tree: `androidApp`, `iosApp`, `sharedLogic`, `sharedUI`, the Gradle wrapper (`gradlew`, `gradlew.bat`, `gradle/wrapper/`), `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties`, `gradle/libs.versions.toml`, `README.md`, and `.gitignore`. 53 files. `gradlew` is mode `100755`.

`.gitignore` keeps the wizard rules. Under that block it adds `*.apk`, `*.aab`, `*.ap_`, `*.ipa`, `*.dSYM/`, `*.jks`, `*.keystore`, `*.hprof`, `.env`, `.env.*`, and `DerivedData/`.

The second commit uses the message "Record how architectural decisions are logged." It adds this file, `AGENTS.md`, the decision files under `docs/decisions/`, the generated `docs/decisions/INDEX.md`, and `scripts/regenerate-decision-index.sh`. It does not change files under `sharedLogic`, `sharedUI`, `androidApp`, or `iosApp`.

## Git status check

Before the first commit, `git status --short` listed only:

- `.gitignore`
- `README.md`
- `androidApp/`
- `build.gradle.kts`
- `gradle.properties`
- `gradle/`
- `gradlew`
- `gradlew.bat`
- `iosApp/`
- `settings.gradle.kts`
- `sharedLogic/`
- `sharedUI/`

It did not list `.gradle`, `.idea`, `build/`, `local.properties`, or `xcuserdata`.

`git check-ignore -v` matched the paths that exist on disk:

- `.gradle` matched `.gradle`
- `.idea` matched `.idea`
- `build`, `androidApp/build`, `sharedLogic/build`, and `sharedUI/build` matched `**/build/`
- `local.properties` matched `local.properties`
- `xcuserdata`, `iosApp/iosApp.xcodeproj/xcuserdata`, and `iosApp/iosApp.xcodeproj/project.xcworkspace/xcuserdata` matched `xcuserdata`

The same check matched the added patterns: `app.apk`, `app.aab`, `app.ap_`, `app.ipa`, `Foo.dSYM/`, `release.jks`, `release.keystore`, `heap.hprof`, `.env`, `.env.local`, and `DerivedData/`.

`git check-ignore` did not match `catalog.db`, `food.db`, `docs/decisions/INDEX.md`, or `docs/work/001-repo-setup.md`. `*.db` and `docs/` stay tracked.

The staged list for the first commit contained none of the ignored paths. After that commit, `git ls-files -s gradlew` was mode `100755`.

## Local choices

### Decision files use headings plus a title

Why. The required shape is Decision, Why, Other ways, Stood on, Supersedes. Each file uses those names as `##` headings, in that order, under one `#` title. `scripts/regenerate-decision-index.sh` reads the Decision section by the heading `## Decision` and uses the title as the link text.

Other ways. Plain labels with no heading marks. The script would then have to guess where a section ends. YAML front matter for the title and the decision sentence. That would add a second format beside the five sections.

### The index script is POSIX sh

Why. `scripts/regenerate-decision-index.sh` rewrites `docs/decisions/INDEX.md` from the dated decision files. The commands in `README.md` are Gradle and Xcode. A shell script runs without adding a Python or Node step to that list. `LC_ALL=C` makes the filename sort byte order, independent of the machine locale.

Other ways. A Gradle task. Regenerating a markdown index would then depend on a build. A Python script. The README does not ask for a Python install.

### Same-day files sort alphabetically

Why. Newest first uses the `YYYY-MM-DD` prefix, which stays the same after a clone. These seven files share `2026-11-27` and the filename has no time, so the script lists them alphabetically by filename.

Other ways. Sort by file modification time. A checkout changes those times, so the index would change without a decision change. Reverse the whole filename. Same-day slugs would then be reverse alphabetical.

### An index line is the date, the title, and the Decision paragraph

Why. `INDEX.md` is the list an agent reads first. Each entry shows the date, a link whose text is the file title, and the Decision paragraph, so the rule is visible without opening the file.

Other ways. Link the slug only. The reader then opens every file to learn the decision. Copy every section into the index. The list would stop being a list.

### `.gitignore` has no extra comment

Why. The task said to keep the wizard rules and add only the listed patterns. Those patterns are appended under the wizard block. The rule that `*.db` and `docs/` stay tracked is recorded in this log and in `AGENTS.md`, and `git check-ignore` confirmed it.

Other ways. A comment in `.gitignore` beside the new patterns, saying the food catalog and `docs/` are committed. A later edit of `.gitignore` would see that comment without opening this log. The comment was left out so the ignore file in the first commit is the wizard text plus the listed patterns.
