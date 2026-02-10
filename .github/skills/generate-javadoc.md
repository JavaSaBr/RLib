# Skill: Generate Javadoc

## Purpose
Generate comprehensive Javadocs for public API interfaces and classes in a specified module, following consistent documentation standards.

## Trigger
When the user asks to:
- Generate javadocs for a module
- Add documentation to public APIs
- Document interfaces/classes with `@since` tags

## Requirements

### What to Document
1. **Public API interfaces and classes only** - files in main source packages
2. **Skip implementation classes** - files in `impl/` packages are NOT documented
3. **Skip test classes** - files in `src/test/` are NOT documented

### What to Omit
1. **Simple getters/setters** - methods like `getArch()`, `setArch(String)` are self-explanatory
2. **Obvious field comments** - avoid comments that just repeat the field name (e.g., `/** The name. */ private String name;`)
3. **Obvious constructor javadocs** - avoid comments like "Instantiates a new Foo"

### Javadoc Standards
Each documented element must include:

1. **Class/Interface level:**
   - Short description of purpose (active voice)
   - `@param` for type parameters with meaningful descriptions (if generic)
   - `@since 10.0.0`

2. **Method level:**
   - Short description for non-trivial methods
   - `@param` and `@return` only for **non-trivial** methods
   - `@since 10.0.0`
   - `@throws` only when explicitly thrown

3. **Constants/Fields:**
   - Short description (only if not obvious from the name)
   - `@since 10.0.0`

### Example Format

```java
/**
 * An immutable array interface that provides type-safe, indexed access to elements.
 *
 * @param <E> the type of elements in this array
 * @since 10.0.0
 */
public interface Array<E> extends Iterable<E> {

  /**
   * Creates an empty immutable array of the specified type.
   *
   * @param <E> the type of elements
   * @param type the component type of the array
   * @return an empty immutable array
   * @since 10.0.0
   */
  static <E> Array<E> empty(Class<? super E> type) {
    // ...
  }

  /**
   * Returns the number of elements in this array.
   *
   * @return the number of elements
   * @since 10.0.0
   */
  int size();

  /**
   * Returns the element at the specified index.
   *
   * @param index the index of the element to return
   * @return the element at the specified index
   * @throws IndexOutOfBoundsException if the index is out of range
   * @since 10.0.0
   */
  E get(int index);
}
```

## Execution Steps

1. **Analyze module structure:**
   ```bash
   ls -la <module>/src/main/java/<package-path>/
   ```

2. **Identify public API files:**
   - List all `.java` files in main packages
   - Exclude `impl/` subdirectories
   - Exclude `package-info.java` (optional to document)

3. **Read each file** to understand existing documentation state

4. **Add Javadocs** using `replace_string_in_file` tool:
   - Add class-level Javadoc with description and `@since`
   - Add method-level Javadocs with description and `@since`
   - Add `@param`/`@return` only for non-trivial methods

5. **Validate changes:**
   ```bash
   ./gradlew :<module>:compileJava
   ```

6. **Run full build:**
   ```bash
   ./gradlew :<module>:build
   ```

7. **Update summary file** at `./summary.md` with documented files

## Output
- All public API files documented with proper Javadocs
- Build passes successfully
- Summary file updated with documentation status

## Notes
- Use existing code style from the project
- Preserve existing Javadocs that are already complete (just add `@since` if missing)
- Group related edits to minimize tool calls
- Check for errors after editing each file
