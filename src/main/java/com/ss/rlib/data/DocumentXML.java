package com.ss.rlib.data;

import org.jetbrains.annotations.NotNull;

/**
 * The interface for implementing a parser pf xml documents.
 *
 * @param <C> the type parameter
 * @author JavaSaBr
 */
public interface DocumentXML<C> {

    /**
     * Parse this document and get the result.
     *
     * @return the c
     */
    @NotNull
    C parse();
}
