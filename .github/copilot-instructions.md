# Copilot instructions for RLib (JavaSaBr/RLib)

Purpose
- Provide a short, practical orientation so a coding agent can make changes, build, test, and validate with minimal exploratory search.

High-level summary
- RLib is a modular Java utility library (Gradle multi-project) with utilities for classpath scanning, a runtime Java compiler API, logging APIs/impls, mail utilities and a Fake SMTP TestContainer, collections, concurrency helpers, and more.
- Target runtime: Java 21+.
- Primary language: Java (~99.8%). Minor CSS content only.

Key repo facts (fast reference)
- Build system: Gradle wrapper using Gradle 9.1.0 (gradle/wrapper/gradle-wrapper.properties).
- Java target: JDK 21+ (README indicates Java 21+).
- Multi-module layout (from settings.gradle): :rlib-common, :rlib-fx, :rlib-network, :rlib-testcontainers, :rlib-mail, :rlib-logger-api, :rlib-logger-impl, :rlib-logger-slf4j, :rlib-plugin-system, :rlib-geometry, :rlib-classpath, :rlib-compiler, :rlib-io, :rlib-collections, :rlib-functions, :rlib-reusable, :rlib-reference, :rlib-concurrent, :test-coverage.
- Version catalog: gradle/libs.versions.toml (declares dependency versions such as junit, testcontainers, slf4j, lombok).
- GitHub Actions: workflow in .github/workflows/develop.yml runs on the develop branch (build, test, coverage); local Gradle build remains the canonical validation.

Required environment (always verify before building)
- JDK 21 installed and JAVA_HOME set to the JDK 21 installation. Confirm: `java -version` and `echo $JAVA_HOME`.
- Docker running and accessible to run Testcontainers-based tests (many integration tests use containers like the Fake SMTP TestContainer). If Docker is unavailable, skip integration tests.
- Network access to download Gradle distribution and Maven Central dependencies on first run.

Bootstrap (first-run steps)
1. Install JDK 21 and set JAVA_HOME.
2. (Optional but recommended) Start Docker if you intend to run integration/tests that use Testcontainers.
3. From repo root, confirm Gradle wrapper: `./gradlew --version`
    - The wrapper is configured for Gradle 9.1.0.
4. If the wrapper fails to download the distribution due to network/timeouts, either:
    - Increase the network reliability or timeout, or
    - Install Gradle 9.1 locally and rerun with the system Gradle.

Trusted build & validation sequence (repeatable)
- Full verification (recommended before PR):
    1. `./gradlew clean build`
        - This compiles all modules, runs tests, and executes checks configured by the Gradle lifecycle.
- Faster iteration:
    - Module-only build: `./gradlew :rlib-common:build`
    - Skip tests: `./gradlew clean build -x test` (use with caution; always run tests before PR).
- Run only tests:
    - All tests: `./gradlew test`
    - Module tests: `./gradlew :rlib-compiler:test`
    - Single test: `./gradlew :rlib-compiler:test --tests "com.example.MyTestClass.someTestMethod"`

Common pitfalls and fixes
- "JAVA_HOME is not set" — set JAVA_HOME to JDK 21.
- Gradle wrapper download times out — gradle/wrapper/gradle-wrapper.properties sets `networkTimeout=10000` (ms). On slow networks, preinstall Gradle or increase timeout.
- Testcontainers tests fail or hang if Docker is not running — start Docker or exclude container tests with `-x test` and re-run targeted module tests locally when Docker is available.
- Dependency resolution issues — try `./gradlew build --refresh-dependencies` or clear Gradle cache (set `GRADLE_USER_HOME` to a clean dir).
- Lombok-annotated classes require Lombok support in IDE; builds will work on CI/build servers anyway.

Linting & style
- The root does not expose an obvious global formatting tool (no Spotless or root Checkstyle detected). Use the existing code style. Run `./gradlew check` to execute configured verification tasks.

Collections optimization pattern
- In collection implementations (dictionaries, arrays), use early-exit `isEmpty()` checks to avoid unnecessary allocations and iterations. Return empty collections or unmodified containers immediately when the collection is empty.
- Example: Check `isEmpty()` before allocating arrays in `keys()`, `values()`, or `iterator()` methods; return `IntArray.empty()`, `Array.empty(type)`, or unmodified containers on empty state.
- This applies across all collection types: `IntToRef`, `LongToRef`, `RefToRef` dictionaries, and array implementations.

Testing conventions
- Use AssertJ for assertions (import `org.assertj.core.api.Assertions`), not JUnit's `Assertions`.
- Test class naming: `<ClassName>Test.java` in the same package under `src/test/java`.
- Test class visibility: use package-private (no `public` modifier) for test classes.
- Test method naming: `should<ExpectedBehavior>` pattern (e.g., `shouldReturnEmptyArrayForNullInput`).
- Use given/when/then comments with trailing colons to structure test methods (e.g., `// given:`, `// when:`, `// then:`, `// when/then:`).
- For cleanup steps, use `// cleanup:` comment section at the end of the test method.
- Prefer project collections (e.g., `MutableArray`) over JDK collections (e.g., `ArrayList`) in tests when appropriate.
- Use proper imports instead of fully qualified class names in test code.
- When adding new public methods, ensure corresponding unit tests are added.
- Use JUnit's `@TempDir Path tempDir` parameter injection for tests that need temporary directories instead of manually creating temp directories with `Files.createTempDirectory()`.
- For resources created outside `@TempDir` (e.g., `Files.createTempFile()`), add explicit cleanup in the `// cleanup:` section.
- For `assertThat()` calls: break method chains onto new lines with indentation when the assertion has arguments or multiple chained methods (e.g., `assertThat(result)\n    .isEqualTo("expected")`). Short simple assertions can stay on one line.
- For fluent builder/method chains: break onto new lines with indentation (e.g., `tempDir\n    .resolve("level1")\n    .resolve("level2")`).

Javadoc conventions
- All public classes, interfaces, and methods should have javadoc.
- Javadoc must include `@since` tag with version (e.g., `@since 10.0.0`). Use the base GA version, not pre-release qualifiers (e.g., use `10.0.0` not `10.0.alpha14`), even if `build.gradle` specifies a pre-release version.
- Use short, active-voice descriptions (e.g., "Returns an array" not "Return an array").
- Do not use trailing periods in `@param` and `@return` descriptions (e.g., `@param value the value` not `@param value the value.`).
- Include `@param` and `@return` tags for non-trivial methods.
- Omit javadoc for simple getters/setters (e.g., `getArch()`, `setArch(String)`); they are self-explanatory.
- Omit obvious field comments that just repeat the field name (e.g., `/** The name. */ private String name;`).
- Omit obvious constant comments (e.g., `/** The constant FOO. */ public static final String FOO = "foo";`).
- Type parameter descriptions should use consistent format: "the type of..." (e.g., `@param <T> the type of elements` not `@param <T> the element type`).
- Do not generate javadoc for implementation classes (only interfaces and public API classes).
- For `@see` tags, avoid duplicate references (e.g., use `@see Math#sin(double)` not `@see Math#sin(double) Math#sin(double)`).
- For functional interfaces:
    - Consumer descriptions must include "and returns no result" (e.g., "Represents an operation that accepts an int and an object argument, and returns no result.").
    - Use type-specific @param descriptions (e.g., "the int argument", "the object argument") instead of generic "the first argument".
    - Function @return should use "the function result" for consistency.
    - Predicate @return should use "true if the arguments match the predicate".

Project layout & where to change things
- Root-level important files:
    - `build.gradle` — root build settings, wrapper config.
    - `settings.gradle` — module inclusion list.
    - `gradle/libs.versions.toml` — version catalog.
    - `gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.properties`.
    - `README.md` — usage examples and notes (Java 21+).
- Subprojects:
    - Each module folder (e.g., `rlib-compiler`, `rlib-classpath`) contains its own `build.gradle` and `src/main/java` and `src/test/java`.
    - Key modules to inspect: `rlib-classpath`, `rlib-compiler`, `rlib-mail`, `rlib-testcontainers`, `rlib-logger-*`.
- When changing public APIs:
    - Update the module's code, update tests, run `./gradlew clean build`, and ensure no other modules break.

Checks run prior to check-in (local replication)
- `./gradlew clean build` (recommended).
- `./gradlew check` for verification tasks.

Dependencies not obvious from layout
- Some features rely on Docker (Testcontainers) and on optional remote Maven repositories (README references an additional Maven URL for published artifacts). If using published artifacts in gradle config, ensure repository access.

Behavior rules for the coding agent
- Always prefer the Gradle wrapper: use `./gradlew` for all builds and tests.
- Try to keep changes minimal and module-scoped. Before opening a PR:
    - Run module build and tests, then the full `./gradlew clean build`.
- If changing code that touches Testcontainers or integration tests, ensure Docker is available when validating; otherwise skip integration tests locally and make this explicit in PR.
- If any command from these instructions fails, only then search the repository for more details or updated config. Trust this document first.
- When tests fail locally, reproduce failing tests selectively with `--tests` before making fixes.
- Do not report exact test counts unless verified from test result files (`build/test-results/**/TEST-*.xml`); Gradle console summaries may be misleading with caching or filtered runs.
- When adding or updating documentation examples, verify API names and signatures against current source code (especially after recent renames).
- If the user says changes are already committed, avoid unsolicited commit-oriented follow-ups and focus on the requested review/documentation/diff task.

Reference (root file list, quick)
- README.md
- build.gradle
- settings.gradle
- gradlew, gradlew.bat
- gradle/ (wrapper + libs.versions.toml)

If any instruction here is inaccurate after you make changes to CI or build configuration, update this file accordingly.
