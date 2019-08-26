package com.ss.rlib.common.util.ref;

import lombok.*;

/**
 * The reference to char value.
 *
 * @author JavaSaBr
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CharReference extends AbstractReference {

    /**
     * The value of this reference.
     */
    private char value;

    @Override
    public void free() {
        this.value = 0;
    }
}
