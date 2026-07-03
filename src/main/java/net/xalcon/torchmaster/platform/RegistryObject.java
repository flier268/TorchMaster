package net.xalcon.torchmaster.platform;

import net.minecraft.resources.ResourceKey;
//? if >=1.21.11 {
/*import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.resources.ResourceLocation;
//?}

import java.util.function.Supplier;

public interface RegistryObject<T> extends Supplier<T>
{

    /**
     * Gets the {@link ResourceKey} of the registry of the object wrapped.
     *
     * @return the {@link ResourceKey} of the registry
     */
    ResourceKey<T> getResourceKey();

    /**
     * Gets the id of the object.
     *
     * @return the id of the object
     */
    //? if >=1.21.11 {
    /*Identifier getId();
    *///?} else {
    ResourceLocation getId();
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
