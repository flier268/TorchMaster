package net.xalcon.torchmaster.platform;

import java.util.function.Supplier;
//? if >=1.19.3
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public interface RegistryObject<T> extends Supplier<T>
{

    /**
     * Gets the registry key of the object wrapped where the active version exposes one.
     *
     * @return the registry key
     */
    //? if >=1.19.3
    RegistryKey<T> getResourceKey();
    //? if <1.19.3
    //Object getResourceKey();

    /**
     * Gets the id of the object.
     *
     * @return the id of the object
     */
    //? if >=1.21.11 {
    /*Identifier getId();
    *///?} else {
    Identifier getId();
    //?}

    /**
     * Gets the object behind this wrapper. Calling this method too early
     * might result in crashes.
     *
     * @return the object behind this wrapper
     */
    @Override
    T get();

    /**
     * Gets this object wrapped in a vanilla holder object where the active version supports it.
     *
     * @return the holder
     */
    default Object asHolder() {
        throw new UnsupportedOperationException("Holder access is not available on this version");
    }
}
