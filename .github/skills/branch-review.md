# Skill: Branch Review

## Purpose
Review the current branch against `develop` and report only material issues with high signal: bugs, regressions, API breaks, resource/concurrency risks, and behavior changes.

## When to Use
- Before opening a pull request to `develop`
- After major refactoring or API changes
- When you want a strict, impact-focused review

## Review Scope (RLib-specific)
1. **Public API compatibility first**
   - Treat removals/signature changes in public API as high priority by default
   - If branch is intentionally alpha-breaking, mark these as accepted and continue

2. **Prioritize API surface over internals**
   - Start with changed files in `src/main/java`
   - Prioritize non-`impl/` packages
   - Review `impl/` changes when they can affect exposed behavior

3. **Javadoc quality is part of API review**
   - Check public API javadocs for:
     - `@since 10.0.0`
     - Active voice descriptions
     - No trailing periods in `@param`/`@return`
     - No unnecessary getter/setter javadocs

4. **Collections performance pattern**
   - Verify empty-state fast paths (`isEmpty()`) in collection operations
   - Ensure no unnecessary allocation/iteration when collections are empty

5. **Runtime/integration constraints**
   - Many integration tests use Testcontainers
   - If Docker is unavailable, call out that integration validation is limited

6. **Testing conventions awareness**
   - Prefer AssertJ assertions
   - Keep test naming and style aligned with repo conventions

7. **Version mapping correctness**
   - For version-to-name lookups (for example OS distribution naming), verify runtime version normalization before map access
   - Confirm behavior for real version formats (for example `10.15.7`, `14.4.1`, `15.1`) with targeted tests

8. **Path handling in file discovery**
   - Ensure file discovery helpers return full/usable paths when downstream code reads files
   - Flag bare filename returns that make behavior depend on current working directory

## Process

### 1. Gather branch diff
```bash
git --no-pager diff --name-only develop...HEAD
git --no-pager diff --stat develop...HEAD
git --no-pager diff develop...HEAD
```

### 2. Triage files
- Group into:
  - Public API changes
  - Internal behavior/refactoring
  - Documentation-only changes

### 3. Review by risk
- Public API contract changes
- Behavioral logic changes
- Concurrency/resource handling
- Performance-sensitive paths
- Documentation contract gaps (for public API)

### 4. Report findings
Use this format:
- **Severity:** High / Medium / Low
- **File:** path
- **Issue:** concise technical problem
- **Impact:** why it matters in real usage
- **Fix:** concrete recommendation

Only include findings that are actionable and materially important.

## Output Expectations
- If no material issues: explicitly state no material issues found
- If issues exist: provide a short prioritized list
- Do not include style-only or trivial nits
