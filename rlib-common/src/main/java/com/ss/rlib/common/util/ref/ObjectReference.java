package com.ss.rlib.common.util.ref;

import lombok.*;

/**
 * The reference to object.
 *
 * @author JavaSaBr
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ObjectReference<T> extends AbstractReference {

    /**
     * The object of this reference.
     */
    private T value;

    @Override
    public void free() {
        this.value = null;
    }
}
