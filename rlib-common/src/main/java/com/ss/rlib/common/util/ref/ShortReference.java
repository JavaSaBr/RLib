package com.ss.rlib.common.util.ref;

import lombok.*;

/**
 * The reference to short value.
 *
 * @author JavaSaBr
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ShortReference extends AbstractReference {

    /**
     * The value of this reference.
     */
    private short value;

    @Override
    public void free() {
        this.value = 0;
    }
}
