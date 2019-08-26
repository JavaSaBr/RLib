package com.ss.rlib.common.util.ref;

import lombok.*;

/**
 * The reference to double value.
 *
 * @author JavaSaBr
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DoubleReference extends AbstractReference {

    /**
     * The value of this reference.
     */
    private double value;

    @Override
    public void free() {
        this.value = 0D;
    }
}
