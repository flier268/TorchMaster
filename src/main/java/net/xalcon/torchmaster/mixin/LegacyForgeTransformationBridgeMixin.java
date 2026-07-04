//? if forge && <=1.16.5 {
/*package net.xalcon.torchmaster.mixin;

import com.mojang.math.Transformation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Transformation.class)
public abstract class LegacyForgeTransformationBridgeMixin
{
    @Shadow(remap = false)
    private Transformation inverseVanilla()
    {
        throw new AssertionError();
    }

    @Shadow(remap = false)
    private Transformation composeVanilla(Transformation other)
    {
        throw new AssertionError();
    }

    public Transformation func_227987_b_()
    {
        return this.inverseVanilla();
    }

    public Transformation func_227985_a_(Transformation other)
    {
        return this.composeVanilla(other);
    }
}
*///?}
