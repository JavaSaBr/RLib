package rlib.data;

import org.jetbrains.annotations.NotNull;

/**
 * The interface for implementing a parser pf xml documents.
 *
 * @author JavaSaBr
 */
public interface DocumentXML<C> {

    /**
     * Parse this document and get the result.
     */
    @NotNull
    C parse();
}
