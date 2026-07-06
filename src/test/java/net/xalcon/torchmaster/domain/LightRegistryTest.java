package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.BlockPosView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LightRegistryTest {
    @Test
    void emptyRegistryHasNoEntries() {
        LightRegistry registry = new LightRegistry();

        assertEquals(0, registry.entries().size());
        assertEquals(0, registry.keyedEntries().size());
    }

    @Test
    void registerStoresEntryByKey() {
        LightRegistry registry = new LightRegistry();
        LightEntry entry = light(LightKind.MEGA_TORCH, 0, 64, 0);
        registry.register("mega", entry);

        assertTrue(registry.get("mega").isPresent());
        assertSame(entry, registry.get("mega").get());
        assertEquals(1, registry.entries().size());
    }

    @Test
    void unregisterRemovesStoredEntry() {
        LightRegistry registry = new LightRegistry();
        registry.register("mega", light(LightKind.MEGA_TORCH, 0, 64, 0));
        registry.unregister("mega");

        assertFalse(registry.get("mega").isPresent());
        assertEquals(0, registry.entries().size());
    }

    @Test
    void clearRemovesAllEntries() {
        LightRegistry registry = new LightRegistry();
        registry.register("dread", light(LightKind.DREAD_LAMP, 0, 64, 0));
        registry.register("mega", light(LightKind.MEGA_TORCH, 0, 64, 0));
        registry.clear();

        assertEquals(0, registry.entries().size());
        assertEquals(0, registry.keyedEntries().size());
    }

    @Test
    void keyedEntriesPreserveKeyAssociations() {
        LightRegistry registry = new LightRegistry();
        LightEntry mega = light(LightKind.MEGA_TORCH, 0, 64, 0);
        LightEntry dread = light(LightKind.DREAD_LAMP, 2, 64, 2);
        registry.register("mega", mega);
        registry.register("dread", dread);

        assertEquals(2, registry.keyedEntries().size());
        assertSame(mega, registry.keyedEntries().iterator().next().getValue());
    }

    private static LightEntry light(LightKind kind, int x, int y, int z) {
        return new LightEntry() {
            @Override
            public LightKind kind() {
                return kind;
            }

            @Override
            public BlockPosView position() {
                return new BlockPosView(x, y, z);
            }

            @Override
            public String displayName() {
                switch (kind) {
                    case MEGA_TORCH:
                        return "Mega Torch";
                    case DREAD_LAMP:
                        return "Dread Lamp";
                    default:
                        return "Light";
                }
            }
        };
    }
}
