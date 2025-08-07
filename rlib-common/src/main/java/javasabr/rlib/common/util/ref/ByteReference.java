package javasabr.rlib.common.util.ref;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The reference to byte value.
 *
 * @author JavaSaBr
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ByteReference extends AbstractReference {

    /**
     * The value of this reference.
     */
    private byte value;

    @Override
    public void free() {
        this.value = 0;
    }
}
