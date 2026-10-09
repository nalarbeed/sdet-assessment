# SDET Assessment

A Maven multi-module test-automation project covering **9 mobile scenarios**, **7 web cases** and **2 API cases**, all written as Cucumber features driven by TestNG runners with Allure reporting.

## Overview & coverage

| Suite | Module | Feature | Scenarios |
|---|---|---|---|
| Mobile | `mobile-api-tests` | `features/mobile/selendroid.feature` | 9 (`@s1`–`@s9`; `@s8`/`@s9` are expected failures tagged `@fail-case`) |
| API | `mobile-api-tests` | `features/api/users.feature` | 2 (`@api`) |
| Web | `web-tests` | `features/web/jqueryui.feature` | 7 (`@case1`–`@case7`) |

- **Mobile (Appium / UiAutomator2)** — Selendroid test app (`io.selendroid.testapp`): home screen, localization dialog, WebView "Say Hello" demo, user-registration flow, progress loader, toast, popup window, and two crash (fail) cases.
- **API (REST Assured)** — reqres.in: GET users (find by id) and a chained POST create-user validated against a JSON schema.
- **Web (Playwright for Java)** — jqueryui.com: Droppable, Selectable, Controlgroup, Datepicker, Resizable, Sortable, Widget Factory.

## Tech stack (exact versions from the POM)

| Component | Version |
|---|---|
| Java | 17 |
| Maven (wrapper) | 3.9.9 |
| Appium `java-client` | 10.1.1 |
| Selenium (`selenium-api`, `selenium-remote-driver`, `selenium-support`) | 4.43.0 |
| Playwright for Java | 1.63.0 |
| REST Assured (`rest-assured`, `json-schema-validator`) | 6.0.1 |
| TestNG | 7.12.0 |
| Cucumber (`cucumber-java`, `cucumber-testng`) | 7.34.9 |
| Allure (`allure-cucumber7-jvm`) | 3.0.0 |
| `allure-maven` plugin | 3.1.0 |
| Logback (`logback-classic`) | 1.6.5 |
| `maven-compiler-plugin` | 3.16.0 |
| `maven-surefire-plugin` | 3.6.0 |

## Project structure

```
sdet-assessment/
├── pom.xml                        # parent POM (packaging=pom): versions + dependencyManagement
├── mvnw / mvnw.cmd                # Maven wrapper (executable)
├── .gitignore
├── .github/workflows/ci.yml       # GitHub Actions (API + web jobs)
├── README.md
├── mobile-api-tests/
│   ├── pom.xml
│   └── src/test/
│       ├── java/com/sdet/assessment/mobile/
│       │   ├── api/               UserClient
│       │   ├── config/            ConfigReader
│       │   ├── driver/            AppiumDriverManager
│       │   ├── hooks/             MobileHooks
│       │   ├── pages/             BasePage, HomePage, LocalizationPage, PopupWindowPage,
│       │   │                      RegisterUserPage, ToastPage, VerifyUserPage, WebViewPage
│       │   ├── runners/           RunCucumberTest
│       │   └── steps/             ApiSteps, MobileSteps
│       └── resources/
│           ├── apps/selendroid-test-app.apk
│           ├── features/api/users.feature
│           ├── features/mobile/selendroid.feature
│           ├── schemas/create-user-schema.json
│           ├── allure.properties
│           ├── config.properties
│           └── logback-test.xml
└── web-tests/
    ├── pom.xml
    └── src/test/
        ├── java/com/sdet/assessment/web/
        │   ├── config/            ConfigReader
        │   ├── driver/            PlaywrightFactory
        │   ├── hooks/             Hooks
        │   ├── pages/             JQueryUiHomePage, DroppablePage, SelectablePage, ControlgroupPage,
        │   │                      DatepickerPage, ResizablePage, SortablePage, WidgetFactoryPage
        │   ├── runners/           RunCucumberTest
        │   └── steps/             WebSteps
        └── resources/
            ├── features/web/jqueryui.feature
            ├── allure.properties
            ├── config.properties
            └── logback-test.xml
```

## Architecture decisions

- **Two Maven modules** (`mobile-api-tests`, `web-tests`) under a **parent POM** (packaging `pom`) that centralises all dependency versions in `dependencyManagement` and plugin versions in `pluginManagement`. Modules declare dependencies without versions.
- **Thin Cucumber steps**: step definitions only orchestrate; **all assertions live in the step definitions**. Page objects return values/state and never assert.
- **ThreadLocal drivers**: `AppiumDriverManager` and `PlaywrightFactory` hold the driver/`Page` in `ThreadLocal`s, created in `@Before` and torn down in `@After`.
- **Explicit waits only**: `WebDriverWait`/`ExpectedConditions` on mobile and Playwright's web-first auto-waiting on web. No `Thread.sleep` anywhere.
- **Config with override**: each module has a `ConfigReader` that reads `config.properties` from the classpath, with **system-property then environment-variable override** (mobile also uses `REQRES_API_KEY`).
- **Allure reporting**: the Cucumber plugin `io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm` writes results to `target/allure-results`; the hooks attach a **screenshot on every mobile and web scenario** (pass and fail).
- **Why Playwright for Java**: it gives one unified Maven + TestNG + Cucumber + Allure toolchain for browser automation (fast, auto-downloading browsers, web-first waiting) without introducing a second runtime (Node/JS) into the build.

## Prerequisites (Windows)

- **JDK 17** (Temurin recommended) on `PATH`.
- **Android SDK** with **platform-tools** (`adb`), an **emulator running Android API 33** (here named `Appium_API33`).
- **Node.js** (to install Appium) and **Appium 3** with the **UiAutomator2** driver.
- **Android build-tools 34** (`aapt2` and `apksigner`) available to Appium — install via `sdkmanager "build-tools;34.0.0"` and ensure they are on the SDK path Appium searches (`ANDROID_HOME`/`ANDROID_SDK_ROOT`).
- Start Appium with the insecure flag that lets chromedriver auto-download:
  ```
  appium --allow-insecure=uiautomator2:chromedriver_autodownload
  ```
  Default server URL: `http://127.0.0.1:4723` (see `mobile-api-tests/src/test/resources/config.properties`).

## Run commands

Run from the repository root with the wrapper.

**API**
```powershell
.\mvnw.cmd test -pl mobile-api-tests "-Dcucumber.filter.tags=@api"
```

**Web** (Chromium downloads on first run; `headless=false` by default)
```powershell
.\mvnw.cmd test -pl web-tests "-Dcucumber.filter.tags=@web"
# headless
.\mvnw.cmd test -pl web-tests "-Dcucumber.filter.tags=@web" "-Dheadless=true"
```

**Mobile — normal suite** (excludes the expected-failure crash cases)
```powershell
.\mvnw.cmd test -pl mobile-api-tests "-Dcucumber.filter.tags=@mobile and not @fail-case"
```

**Mobile — fail cases** (expected to fail; ignore makes the build green)
```powershell
.\mvnw.cmd test -pl mobile-api-tests "-Dcucumber.filter.tags=@fail-case" "-Dmaven.test.failure.ignore=true"
```

**Single tag**
```powershell
.\mvnw.cmd test -pl mobile-api-tests "-Dcucumber.filter.tags=@s1"   # @s1..@s9
.\mvnw.cmd test -pl web-tests "-Dcucumber.filter.tags=@case1"       # @case1..@case7
```

## Reporting

Results are written to `<module>/target/allure-results`. Serve an interactive report:
```powershell
.\mvnw.cmd allure:serve -pl mobile-api-tests
.\mvnw.cmd allure:serve -pl web-tests
```
Or build a static report with `allure:report -pl <module>` and open `<module>/target/allure-report/index.html`. A **screenshot is attached to every mobile and web scenario** (including failing/failed ones).

## Assumptions

- **Controlgroup (web case 3):** the assessment instructions were an image, so the actions were inferred from the screenshot — horizontal group (SUV / Automatic / Insurance / 2 cars) and vertical group (Truck / Standard / Insurance / 1 car).
- **Book Now** has no click handler in the demo, so it is clicked but **not asserted**.
- On jqueryui.com, **Resizable** and **Sortable** live under *Interactions* and **Widget Factory** under *Utilities*; navigation is done by clicking the left-menu link text.
- **S8/S9 are expected failures** (`@fail-case`): tapping/typing the "throw unhandled exception" control crashes the app, so the real home-title assertion fails (`-Dmaven.test.failure.ignore=true` keeps the build green).
- The **legacy Selendroid app** (targetSdk 10) requires `appium:noReset=true` and `appium:autoLaunch=false`, multi-window settings (`includeSiblingWindows`, `enableMultiWindows`, `limitXPathContextScope=false`) so toasts/popups are visible, and dismissal of the "built for an older version of Android" dialog and the permission-review dialog.

## Known limitations

- **Mobile is not run in CI** (it needs an emulator/Appium); only API and web run in GitHub Actions.
- Setup is **Windows-focused** for mobile (paths, `.cmd` wrapper).
- Web tests target **Chromium only**.

## CI

`.github/workflows/ci.yml` runs on `push`/`pull_request` to `main` and on `workflow_dispatch`, on `ubuntu-latest` with Temurin JDK 17 and Maven cache (`actions/setup-java`):

- **`api-tests`** — `./mvnw -B test -pl mobile-api-tests -Dcucumber.filter.tags=@api` (passes `REQRES_API_KEY` from secrets if present; not required).
- **`web-tests`** — installs Chromium with system dependencies via the Playwright CLI (`org.codehaus.mojo:exec-maven-plugin:3.6.4:java`, `mainClass=com.microsoft.playwright.CLI`, `args="install --with-deps chromium"`), then `./mvnw -B test -pl web-tests -Dcucumber.filter.tags=@web -Dheadless=true`.

Both jobs upload `target/allure-results` as an artifact, attempt `allure:report`, and upload the generated report as a second artifact.
