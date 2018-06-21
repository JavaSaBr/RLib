package com.ss.rlib.common.plugin.extension;

import static java.util.Collections.unmodifiableList;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The class to present an extension point.
 *
 * @author JavaSaBr
 */
public class ExtensionPoint<T> {

    /**
     * The list of extensions.
     */
    @NotNull
    private final Array<T> extensions;

    /**
     * The reference to a read only list.
     */
    @NotNull
    private final AtomicReference<List<T>> readOnlyList;

    public ExtensionPoint() {
        this.extensions = ArrayFactory.newCopyOnModifyArray(Object.class);
        this.readOnlyList = new AtomicReference<>(Collections.emptyList());
    }

    /**
     * Register a new extension.
     *
     * @param extension the new extension.
     * @return this point.
     */
    public ExtensionPoint<T> register(@NotNull T extension) {
        this.extensions.add(extension);

        List<T> currentList = readOnlyList.get();
        List<T> newList = unmodifiableList(Arrays.asList(extensions.array()));

        while (!readOnlyList.compareAndSet(currentList, newList)) {
            currentList = readOnlyList.get();
            newList = unmodifiableList(Arrays.asList(extensions.array()));
        }

        return this;
    }

    /**
     * Get all registered extensions.
     *
     * @return the all registered extensions.
     */
    public @NotNull List<T> getExtensions() {
        return readOnlyList.get();
    }
}
