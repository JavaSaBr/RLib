package com.ss.rlib.common.data;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to implement a parser of xml documents.
 *
 * @param <C> the result type.
 * @author JavaSaBr
 */
public interface DocumentXML<C> {

    /**
     * Parse this document and get the result.
     *
     * @return the result.
     */
    @NotNull C parse();
}
