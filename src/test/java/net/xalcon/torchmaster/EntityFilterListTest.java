package net.xalcon.torchmaster;

import net.xalcon.torchmaster.port.EntityTypeKey;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityFilterListTest {
    @Test
    void appliesValidAddAndRemoveOverrides() {
        EntityFilterList filter = new EntityFilterList("torchmaster:test");

        filter.applyListOverrides(
                Arrays.asList("+minecraft:zombie", "+minecraft:skeleton", "-minecraft:zombie"),
                key -> true);

        assertFalse(filter.containsEntity(EntityTypeKey.parse("minecraft:zombie")));
        assertTrue(filter.containsEntity(EntityTypeKey.parse("minecraft:skeleton")));
    }

    @Test
    void skipsUnknownEntities() {
        EntityFilterList filter = new EntityFilterList("torchmaster:test");

        filter.applyListOverrides(Collections.singletonList("+missing:entity"), key -> false);

        assertFalse(filter.containsEntity(EntityTypeKey.parse("missing:entity")));
    }

    @Test
    void rejectsInvalidFilterStrings() {
        assertFalse(EntityFilterList.IsValidFilterString("minecraft:zombie"));
        assertFalse(EntityFilterList.IsValidFilterString("+minecraft"));
        assertTrue(EntityFilterList.IsValidFilterString("+minecraft:zombie"));
    }
}
