package rlib.database;

import org.jetbrains.annotations.NotNull;

/**
 * The query to clean a table.
 *
 * @author JavaSaBr
 */
public final class CleaningQuery {

    /**
     * The name of the table.
     */
    @NotNull
    private final String name;

    /**
     * The query.
     */
    @NotNull
    private final String query;

    /**
     * @param name  имя таблицы
     * @param query запрос
     */
    public CleaningQuery(@NotNull final String name, @NotNull final String query) {
        this.name = name;
        this.query = query;
    }

    /**
     * @return the name of the table.
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * @return the query.
     */
    @NotNull
    public String getQuery() {
        return query;
    }

    @Override
    public String toString() {
        return "CleaningQuery{" +
                "name='" + name + '\'' +
                ", query='" + query + '\'' +
                '}';
    }
}
