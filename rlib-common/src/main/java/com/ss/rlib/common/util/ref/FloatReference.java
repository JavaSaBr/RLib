package com.ss.rlib.common.util.ref;

import lombok.*;

/**
 * The reference to float value.
 *
 * @author JavaSaBr
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FloatReference extends AbstractReference {

    /**
     * The value of this reference.
     */
    private float value;

    @Override
    public void free() {
        this.value = 0F;
    }
}
