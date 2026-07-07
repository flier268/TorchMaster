package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.LightAccessEntry;
import net.xalcon.torchmaster.port.LightSettingsView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class LightSettingsUseCase
{
    public interface LightRepository
    {
        Optional<LightEntry> find(String lightKey);

        boolean save(String lightKey, LightEntry light);

        LightEntry[] entries();
    }

    public interface PlayerDirectory
    {
        Optional<PlayerRef> findOnlinePlayer(String name);
    }

    public static final class PlayerRef
    {
        private final UUID uuid;
        private final String name;

        public PlayerRef(UUID uuid, String name)
        {
            this.uuid = uuid;
            this.name = name == null ? "" : name;
        }

        public UUID uuid()
        {
            return uuid;
        }

        public String name()
        {
            return name;
        }
    }

    public static final class Actor
    {
        private final UUID uuid;
        private final boolean operator;

        public Actor(UUID uuid, boolean operator)
        {
            this.uuid = uuid;
            this.operator = operator;
        }

        public UUID uuid()
        {
            return uuid;
        }

        public boolean operator()
        {
            return operator;
        }
    }

    private final LightRepository repository;
    private final PlayerDirectory playerDirectory;
    private final boolean restrictToOwner;

    public LightSettingsUseCase(LightRepository repository, PlayerDirectory playerDirectory, boolean restrictToOwner)
    {
        this.repository = repository;
        this.playerDirectory = playerDirectory;
        this.restrictToOwner = restrictToOwner;
    }

    public LightSettingsView snapshot(String lightKey, int globalMax, Actor actor)
    {
        Optional<LightEntry> light = repository.find(lightKey);
        if (!light.isPresent()) {
            return LightSettingsView.missing(globalMax);
        }
        return snapshot(light.get(), globalMax, actor);
    }

    public LightSettingsView snapshot(LightEntry light, int globalMax, Actor actor)
    {
        LightSettings settings = light.settings().effective(globalMax);
        LightControlState control = light.controlState();
        return LightSettingsView.present(canEdit(actor, light), canManageAccess(actor, light), settings.enabled(),
                settings.rangeWest(), settings.rangeEast(), settings.rangeDown(), settings.rangeUp(), settings.rangeNorth(), settings.rangeSouth(),
                Math.max(0, globalMax), light.blocksNaturalSpawnsOnly(), control.rangeVisible(),
                control.allowedPlayers().toArray(new LightAccessEntry[0]));
    }

    public LightSettingsView fallbackSnapshot(int globalMax)
    {
        int max = Math.max(0, globalMax);
        return LightSettingsView.present(false, true, max, max, max, max, max, max, max);
    }

    public boolean updateSettings(String lightKey, int globalMax, Actor actor, LightSettings settings)
    {
        Optional<LightEntry> light = repository.find(lightKey);
        if (!light.isPresent() || !canEdit(actor, light.get())) {
            return false;
        }
        return repository.save(lightKey, light.get().withSettings(settings.effective(globalMax)));
    }

    public boolean updateRangeVisible(String lightKey, Actor actor, boolean rangeVisible)
    {
        Optional<LightEntry> light = repository.find(lightKey);
        if (!light.isPresent() || !canEdit(actor, light.get())) {
            return false;
        }
        return repository.save(lightKey, light.get().withControlState(light.get().controlState().withRangeVisible(rangeVisible)));
    }

    public boolean addAccess(String lightKey, Actor actor, String playerName)
    {
        Optional<LightEntry> light = repository.find(lightKey);
        if (!light.isPresent() || !canManageAccess(actor, light.get()) || playerName == null || playerName.trim().isEmpty()) {
            return false;
        }
        Optional<PlayerRef> target = playerDirectory.findOnlinePlayer(playerName.trim());
        if (!target.isPresent() || target.get().uuid() == null) {
            return false;
        }
        LightControlState control = light.get().controlState();
        List<LightAccessEntry> entries = new ArrayList<>(control.allowedPlayers());
        for (int i = 0; i < entries.size(); i++) {
            if (target.get().uuid().equals(entries.get(i).uuid())) {
                entries.set(i, new LightAccessEntry(target.get().uuid(), target.get().name()));
                return repository.save(lightKey, light.get().withControlState(control.withAllowedPlayers(entries)));
            }
        }
        entries.add(new LightAccessEntry(target.get().uuid(), target.get().name()));
        return repository.save(lightKey, light.get().withControlState(control.withAllowedPlayers(entries)));
    }

    public boolean removeAccess(String lightKey, Actor actor, String playerUuid)
    {
        Optional<LightEntry> light = repository.find(lightKey);
        if (!light.isPresent() || !canManageAccess(actor, light.get())) {
            return false;
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(playerUuid);
        } catch (IllegalArgumentException ignored) {
            return false;
        }
        LightControlState control = light.get().controlState();
        List<LightAccessEntry> entries = new ArrayList<>();
        boolean removed = false;
        for (LightAccessEntry entry : control.allowedPlayers()) {
            if (uuid.equals(entry.uuid())) {
                removed = true;
            } else {
                entries.add(entry);
            }
        }
        return removed && repository.save(lightKey, light.get().withControlState(control.withAllowedPlayers(entries)));
    }

    public LightEntry[] visibleRangeEntries()
    {
        List<LightEntry> visible = new ArrayList<>();
        for (LightEntry light : repository.entries()) {
            if (light.controlState().rangeVisible()) {
                visible.add(light);
            }
        }
        return visible.toArray(new LightEntry[0]);
    }

    public boolean canEdit(Actor actor, LightEntry light)
    {
        if (actor == null || actor.uuid() == null) {
            return false;
        }
        if (!restrictToOwner || actor.operator()) {
            return true;
        }
        LightControlState control = light.controlState();
        if (control.ownerUuid().map(owner -> owner.equals(actor.uuid())).orElse(false)) {
            return true;
        }
        for (LightAccessEntry entry : control.allowedPlayers()) {
            if (actor.uuid().equals(entry.uuid())) {
                return true;
            }
        }
        return false;
    }

    public boolean canManageAccess(Actor actor, LightEntry light)
    {
        if (actor == null || actor.uuid() == null) {
            return false;
        }
        return actor.operator() || light.controlState().ownerUuid().map(owner -> owner.equals(actor.uuid())).orElse(false);
    }
}
