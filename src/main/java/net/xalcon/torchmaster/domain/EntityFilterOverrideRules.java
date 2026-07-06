package net.xalcon.torchmaster.domain;

import net.xalcon.torchmaster.port.EntityTypeKey;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class EntityFilterOverrideRules
{
    private static final Pattern FILTER_PATTERN = Pattern.compile("[+-][a-z0-9_-]+:[a-z0-9_-]+");

    private EntityFilterOverrideRules()
    {
    }

    public static boolean isValidFilterString(Object object)
    {
        return object instanceof String && FILTER_PATTERN.matcher((String)object).matches();
    }

    public static EntityFilterOverride parse(Object object, Predicate<EntityTypeKey> entityExists)
    {
        if (!isValidFilterString(object)) {
            return EntityFilterOverride.invalidFormat(object);
        }

        String rawValue = (String)object;
        char prefix = rawValue.charAt(0);
        String entityId = rawValue.substring(1);
        EntityTypeKey entityType;
        try {
            entityType = EntityTypeKey.parse(entityId);
        } catch (RuntimeException ignored) {
            return EntityFilterOverride.invalidEntityId(rawValue, entityId);
        }

        if (prefix == '+') {
            if (!entityExists.test(entityType)) {
                return EntityFilterOverride.unknownEntity(rawValue, entityId, entityType);
            }
            return EntityFilterOverride.add(rawValue, entityId, entityType);
        }
        if (prefix == '-') {
            return EntityFilterOverride.remove(rawValue, entityId, entityType);
        }
        return EntityFilterOverride.invalidFormat(object);
    }
}
