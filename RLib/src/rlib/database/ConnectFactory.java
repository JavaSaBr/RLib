package rlib.database;

import java.sql.Connection;
import java.sql.SQLException;

import static java.sql.SQLException.*;

/**
 * Интерфейс для реализации фабрики подкюлчений к БД.
 *
 * @author Ronn
 */
public interface ConnectFactory {

    /**
     * Метод получения подключения для работы с БД.
     *
     * @return connection подключение, для работы с БД.
     * @throws SQLException.
     */
    public abstract Connection getConnection() throws SQLException;
}
