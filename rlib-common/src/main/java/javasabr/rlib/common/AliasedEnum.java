package javasabr.rlib.common;

import java.util.Collection;

public interface AliasedEnum<T extends Enum<T>> {

  Collection<String> aliases();
}
