package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.EntityTypeKey;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityFilterOverrideRulesTest
{
    @Test
    void validatesFilterStringFormat()
    {
        assertTrue(EntityFilterOverrideRules.isValidFilterString("+minecraft:zombie"));
        assertTrue(EntityFilterOverrideRules.isValidFilterString("-minecraft:skeleton"));
        assertFalse(EntityFilterOverrideRules.isValidFilterString("minecraft:zombie"));
        assertFalse(EntityFilterOverrideRules.isValidFilterString("+minecraft"));
        assertFalse(EntityFilterOverrideRules.isValidFilterString(7));
    }

    @Test
    void parsesAddOverride()
    {
        EntityFilterOverride override = EntityFilterOverrideRules.parse("+minecraft:zombie", key -> true);

        assertEquals(EntityFilterOverride.Action.ADD, override.action());
        assertEquals(EntityTypeKey.parse("minecraft:zombie"), override.entityType());
        assertEquals("minecraft:zombie", override.entityId());
    }

    @Test
    void parsesRemoveOverrideWithoutExistenceCheck()
    {
        EntityFilterOverride override = EntityFilterOverrideRules.parse("-missing:entity", key -> false);

        assertEquals(EntityFilterOverride.Action.REMOVE, override.action());
        assertEquals(EntityTypeKey.parse("missing:entity"), override.entityType());
    }

    @Test
    void reportsUnknownEntityForAddOverride()
    {
        EntityFilterOverride override = EntityFilterOverrideRules.parse("+missing:entity", key -> false);

        assertEquals(EntityFilterOverride.Action.UNKNOWN_ENTITY, override.action());
        assertEquals(EntityTypeKey.parse("missing:entity"), override.entityType());
    }

    @Test
    void reportsInvalidFormat()
    {
        EntityFilterOverride override = EntityFilterOverrideRules.parse("minecraft:zombie", key -> true);

        assertEquals(EntityFilterOverride.Action.INVALID_FORMAT, override.action());
    }
}
