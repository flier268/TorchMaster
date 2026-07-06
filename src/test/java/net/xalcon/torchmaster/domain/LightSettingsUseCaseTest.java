package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.BlockPosView;
import net.xalcon.torchmaster.port.LightAccessEntry;
import net.xalcon.torchmaster.port.LightSettingsView;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LightSettingsUseCaseTest
{
    private static final UUID OWNER = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID ALLOWED = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final UUID OTHER = UUID.fromString("00000000-0000-0000-0000-000000000003");

    @Test
    void ownerAllowedAndOperatorCanEditWhenRestricted()
    {
        FakeRepository repository = new FakeRepository();
        repository.save("mega", light(LightControlState.of(Optional.of(OWNER),
                java.util.Collections.singletonList(new LightAccessEntry(ALLOWED, "Allowed")), false)));
        LightSettingsUseCase useCase = new LightSettingsUseCase(repository, name -> Optional.empty(), true);

        assertTrue(useCase.snapshot("mega", 32, actor(OWNER, false)).editable());
        assertTrue(useCase.snapshot("mega", 32, actor(ALLOWED, false)).editable());
        assertTrue(useCase.snapshot("mega", 32, actor(OTHER, true)).editable());
        assertFalse(useCase.snapshot("mega", 32, actor(OTHER, false)).editable());
    }

    @Test
    void unrestrictedStillRequiresActor()
    {
        FakeRepository repository = new FakeRepository();
        repository.save("mega", light(LightControlState.empty()));
        LightSettingsUseCase useCase = new LightSettingsUseCase(repository, name -> Optional.empty(), false);

        assertTrue(useCase.snapshot("mega", 32, actor(OTHER, false)).editable());
        assertFalse(useCase.snapshot("mega", 32, new LightSettingsUseCase.Actor(null, false)).editable());
    }

    @Test
    void updateClampsSettingsToGlobalMax()
    {
        FakeRepository repository = new FakeRepository();
        repository.save("mega", light(LightControlState.of(Optional.of(OWNER), java.util.Collections.emptyList(), false)));
        LightSettingsUseCase useCase = new LightSettingsUseCase(repository, name -> Optional.empty(), true);

        assertTrue(useCase.updateSettings("mega", 16, actor(OWNER, false), LightSettings.configured(true, 40, -2, 8)));

        LightSettingsView snapshot = useCase.snapshot("mega", 16, actor(OWNER, false));
        assertEquals(16, snapshot.radiusX());
        assertEquals(0, snapshot.radiusY());
        assertEquals(8, snapshot.radiusZ());
    }

    @Test
    void rangeVisibleIsPersistedInControlStateAndListed()
    {
        FakeRepository repository = new FakeRepository();
        repository.save("mega", light(LightControlState.of(Optional.of(OWNER), java.util.Collections.emptyList(), false)));
        LightSettingsUseCase useCase = new LightSettingsUseCase(repository, name -> Optional.empty(), true);

        assertTrue(useCase.updateRangeVisible("mega", actor(OWNER, false), true));

        assertTrue(useCase.snapshot("mega", 32, actor(OWNER, false)).rangeVisible());
        assertEquals(1, useCase.visibleRangeEntries().length);
    }

    @Test
    void addAccessRequiresManagePermissionAndOnlinePlayer()
    {
        FakeRepository repository = new FakeRepository();
        repository.save("mega", light(LightControlState.of(Optional.of(OWNER), java.util.Collections.emptyList(), false)));
        LightSettingsUseCase useCase = new LightSettingsUseCase(repository,
                name -> "Allowed".equals(name) ? Optional.of(new LightSettingsUseCase.PlayerRef(ALLOWED, "Allowed")) : Optional.empty(), true);

        assertFalse(useCase.addAccess("mega", actor(OTHER, false), "Allowed"));
        assertFalse(useCase.addAccess("mega", actor(OWNER, false), " "));
        assertFalse(useCase.addAccess("mega", actor(OWNER, false), "Missing"));
        assertTrue(useCase.addAccess("mega", actor(OWNER, false), "Allowed"));
        assertTrue(useCase.addAccess("mega", actor(OWNER, false), "Allowed"));

        LightSettingsView snapshot = useCase.snapshot("mega", 32, actor(OWNER, false));
        assertEquals(1, snapshot.accessEntries().length);
        assertEquals(ALLOWED, snapshot.accessEntries()[0].uuid());
    }

    @Test
    void removeAccessRejectsInvalidOrMissingUuid()
    {
        FakeRepository repository = new FakeRepository();
        repository.save("mega", light(LightControlState.of(Optional.of(OWNER),
                java.util.Collections.singletonList(new LightAccessEntry(ALLOWED, "Allowed")), false)));
        LightSettingsUseCase useCase = new LightSettingsUseCase(repository, name -> Optional.empty(), true);

        assertFalse(useCase.removeAccess("mega", actor(OWNER, false), "bad"));
        assertFalse(useCase.removeAccess("mega", actor(OWNER, false), OTHER.toString()));
        assertTrue(useCase.removeAccess("mega", actor(OWNER, false), ALLOWED.toString()));
        assertEquals(0, useCase.snapshot("mega", 32, actor(OWNER, false)).accessEntries().length);
    }

    private static LightSettingsUseCase.Actor actor(UUID uuid, boolean operator)
    {
        return new LightSettingsUseCase.Actor(uuid, operator);
    }

    private static TestLight light(LightControlState controlState)
    {
        return new TestLight(controlState, LightSettings.unconfigured());
    }

    private static final class FakeRepository implements LightSettingsUseCase.LightRepository
    {
        private final Map<String, LightEntry> lights = new LinkedHashMap<>();

        @Override
        public Optional<LightEntry> find(String lightKey)
        {
            return Optional.ofNullable(lights.get(lightKey));
        }

        @Override
        public boolean save(String lightKey, LightEntry light)
        {
            lights.put(lightKey, light);
            return true;
        }

        @Override
        public LightEntry[] entries()
        {
            return lights.values().toArray(new LightEntry[0]);
        }
    }

    private static final class TestLight implements LightEntry
    {
        private final LightControlState controlState;
        private final LightSettings settings;

        private TestLight(LightControlState controlState, LightSettings settings)
        {
            this.controlState = controlState;
            this.settings = settings;
        }

        @Override
        public LightKind kind()
        {
            return LightKind.MEGA_TORCH;
        }

        @Override
        public BlockPosView position()
        {
            return new BlockPosView(0, 64, 0);
        }

        @Override
        public String displayName()
        {
            return "Mega Torch";
        }

        @Override
        public LightSettings settings()
        {
            return settings;
        }

        @Override
        public LightEntry withSettings(LightSettings settings)
        {
            return new TestLight(controlState, settings);
        }

        @Override
        public LightControlState controlState()
        {
            return controlState;
        }

        @Override
        public LightEntry withControlState(LightControlState controlState)
        {
            return new TestLight(controlState, settings);
        }
    }
}
