package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.EntityTypeKey;
import net.xalcon.torchmaster.port.LightInfo;
import net.xalcon.torchmaster.port.Vec3View;

import java.util.Optional;

public final class LightStoreRuntime
{
    private final LightRegistry lights;

    public LightStoreRuntime()
    {
        this(new LightRegistry());
    }

    public LightStoreRuntime(LightRegistry lights)
    {
        this.lights = lights;
    }

    public boolean shouldBlockEntity(LightStoreRuntimeContext context, EntityTypeKey entityType, Vec3View position)
    {
        for (LightEntry light : lights.entries()) {
            EntityFilter filter = filterFor(light.kind(), context);
            int radius = radiusFor(light.kind(), context);
            if (LightRules.blocksEntity(light.kind(), filter, entityType, position, light.position(), radius)) {
                return true;
            }
        }
        return false;
    }

    public boolean shouldBlockVillageSiege(LightStoreRuntimeContext context, Vec3View position)
    {
        for (LightEntry light : lights.entries()) {
            if (LightRules.blocksVillageSiege(light.kind(), position, light.position(), radiusFor(light.kind(), context))) {
                return true;
            }
        }
        return false;
    }

    public void registerLight(String lightKey, LightEntry light)
    {
        lights.register(lightKey, light);
    }

    public void unregisterLight(String lightKey)
    {
        lights.unregister(lightKey);
    }

    public Optional<LightEntry> getLight(String lightKey)
    {
        return lights.get(lightKey);
    }

    public LightInfo[] entries()
    {
        return lights.entries().stream()
                .map(light -> new LightInfo(light.displayName(), light.position()))
                .toArray(LightInfo[]::new);
    }

    public LightRegistry registry()
    {
        return lights;
    }

    private static EntityFilter filterFor(LightKind kind, LightStoreRuntimeContext context)
    {
        switch (kind) {
            case MEGA_TORCH:
                return context.megaTorchFilter();
            case DREAD_LAMP:
                return context.dreadLampFilter();
            default:
                return new EntityFilter();
        }
    }

    private static int radiusFor(LightKind kind, LightStoreRuntimeContext context)
    {
        switch (kind) {
            case MEGA_TORCH:
                return context.config().megaTorchRadius();
            case DREAD_LAMP:
                return context.config().dreadLampRadius();
            default:
                return 0;
        }
    }
}
