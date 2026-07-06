package net.xalcon.torchmaster.domain;

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
}
