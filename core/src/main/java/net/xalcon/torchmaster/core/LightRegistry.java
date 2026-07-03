package net.xalcon.torchmaster.core;

import net.xalcon.torchmaster.adapter.ConfigView;
import net.xalcon.torchmaster.adapter.EntityTypeKey;
import net.xalcon.torchmaster.adapter.Vec3View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class LightRegistry {
    private final Map<String, LightEntry> lights = new LinkedHashMap<>();

    public void register(String key, LightEntry light) {
        lights.put(key, light);
    }

    public void unregister(String key) {
        lights.remove(key);
    }

    public Optional<LightEntry> get(String key) {
        return Optional.ofNullable(lights.get(key));
    }

    public Collection<Map.Entry<String, LightEntry>> keyedEntries() {
        return new ArrayList<>(lights.entrySet());
    }

    public Collection<LightEntry> entries() {
        return new ArrayList<>(lights.values());
    }

    public void clear() {
        lights.clear();
    }

    public boolean blocksEntity(EntityFilter megaTorchFilter, EntityFilter dreadLampFilter, ConfigView config, EntityTypeKey entityType, Vec3View position) {
        for (LightEntry light : lights.values()) {
            EntityFilter filter = filterFor(light.kind(), megaTorchFilter, dreadLampFilter);
            int radius = radiusFor(light.kind(), config);
            if (LightRules.blocksEntity(light.kind(), filter, entityType, position, light.position(), radius)) {
                return true;
            }
        }
        return false;
    }

    public boolean blocksVillageSiege(ConfigView config, Vec3View position) {
        for (LightEntry light : lights.values()) {
            if (LightRules.blocksVillageSiege(light.kind(), position, light.position(), radiusFor(light.kind(), config))) {
                return true;
            }
        }
        return false;
    }

    private static EntityFilter filterFor(LightKind kind, EntityFilter megaTorchFilter, EntityFilter dreadLampFilter) {
        switch (kind) {
            case MEGA_TORCH:
                return megaTorchFilter;
            case DREAD_LAMP:
                return dreadLampFilter;
            default:
                return new EntityFilter();
        }
    }

    private static int radiusFor(LightKind kind, ConfigView config) {
        switch (kind) {
            case MEGA_TORCH:
                return config.megaTorchRadius();
            case DREAD_LAMP:
                return config.dreadLampRadius();
            default:
                return 0;
        }
    }
}
