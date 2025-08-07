package javasabr.rlib.common.util.ref;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
