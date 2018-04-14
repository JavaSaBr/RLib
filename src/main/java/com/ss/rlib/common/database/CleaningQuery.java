package com.ss.rlib.common.database;

import org.jetbrains.annotations.NotNull;

/**
 * The query to clean a table.
 *
 * @author JavaSaBr
 */
public final class CleaningQuery {

    /**
     * The description.
     */
    @NotNull
    private final String description;

    /**
     * The query.
     */
    @NotNull
    private final String query;

    public CleaningQuery(@NotNull final String description, @NotNull final String query) {
        this.description = description;
        this.query = query;
    }

    /**
     * Get the description.
     *
     * @return the description.
     */
    public @NotNull String getDescription() {
        return description;
    }

    /**
     * Get the query.
     *
     * @return the query.
     */
    public @NotNull String getQuery() {
        return query;
    }

    @Override
    public String toString() {
        return "CleaningQuery{" + "description='" + description + '\'' + ", query='" + query + '\'' + '}';
    }
}
