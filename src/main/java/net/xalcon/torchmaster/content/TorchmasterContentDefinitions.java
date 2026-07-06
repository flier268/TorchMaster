package net.xalcon.torchmaster.content;

import net.xalcon.torchmaster.domain.LightKind;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class TorchmasterContentDefinitions
{
    public static final String MEGA_TORCH = "megatorch";
    public static final String DREAD_LAMP = "dreadlamp";
    public static final String FERAL_FLARE_LANTERN = "feral_flare_lantern";
    public static final String FROZEN_PEARL = "frozen_pearl";
    public static final String INVISIBLE_LIGHT = "invisible_light";

    public static final BlockDefinition MEGA_TORCH_BLOCK = BlockDefinition.entityBlockingLight(MEGA_TORCH, LightKind.MEGA_TORCH, true);
    public static final BlockDefinition DREAD_LAMP_BLOCK = BlockDefinition.entityBlockingLight(DREAD_LAMP, LightKind.DREAD_LAMP, true);
    public static final BlockDefinition FERAL_FLARE_LANTERN_BLOCK = BlockDefinition.feralFlareLantern(FERAL_FLARE_LANTERN, true);
    public static final BlockDefinition INVISIBLE_LIGHT_BLOCK = BlockDefinition.invisibleLight(INVISIBLE_LIGHT);
    public static final ItemDefinition FROZEN_PEARL_ITEM = ItemDefinition.standalone(FROZEN_PEARL, true);

    private static final List<BlockDefinition> BLOCKS = Collections.unmodifiableList(Arrays.asList(
            MEGA_TORCH_BLOCK,
            DREAD_LAMP_BLOCK,
            FERAL_FLARE_LANTERN_BLOCK,
            INVISIBLE_LIGHT_BLOCK));

    private static final List<String> CREATIVE_TAB_ITEM_IDS = Collections.unmodifiableList(Arrays.asList(
            MEGA_TORCH,
            DREAD_LAMP,
            FERAL_FLARE_LANTERN,
            FROZEN_PEARL));

    private TorchmasterContentDefinitions()
    {
    }

    public static List<BlockDefinition> blocks()
    {
        return BLOCKS;
    }

    public static List<String> creativeTabItemIds()
    {
        return CREATIVE_TAB_ITEM_IDS;
    }

    public enum BlockKind
    {
        ENTITY_BLOCKING_LIGHT,
        FERAL_FLARE_LANTERN,
        INVISIBLE_LIGHT
    }

    public enum ItemKind
    {
        BLOCK_ITEM,
        STANDALONE
    }

    public static final class BlockDefinition
    {
        private final String id;
        private final BlockKind kind;
        private final LightKind lightKind;
        private final boolean creativeTabItem;

        private BlockDefinition(String id, BlockKind kind, LightKind lightKind, boolean creativeTabItem)
        {
            this.id = id;
            this.kind = kind;
            this.lightKind = lightKind;
            this.creativeTabItem = creativeTabItem;
        }

        public static BlockDefinition entityBlockingLight(String id, LightKind lightKind, boolean creativeTabItem)
        {
            return new BlockDefinition(id, BlockKind.ENTITY_BLOCKING_LIGHT, lightKind, creativeTabItem);
        }

        public static BlockDefinition feralFlareLantern(String id, boolean creativeTabItem)
        {
            return new BlockDefinition(id, BlockKind.FERAL_FLARE_LANTERN, null, creativeTabItem);
        }

        public static BlockDefinition invisibleLight(String id)
        {
            return new BlockDefinition(id, BlockKind.INVISIBLE_LIGHT, null, false);
        }

        public String id()
        {
            return id;
        }

        public BlockKind kind()
        {
            return kind;
        }

        public LightKind lightKind()
        {
            return lightKind;
        }

        public boolean hasCreativeTabItem()
        {
            return creativeTabItem;
        }
    }

    public static final class ItemDefinition
    {
        private final String id;
        private final ItemKind kind;
        private final boolean shownInCreativeTab;

        private ItemDefinition(String id, ItemKind kind, boolean shownInCreativeTab)
        {
            this.id = id;
            this.kind = kind;
            this.shownInCreativeTab = shownInCreativeTab;
        }

        public static ItemDefinition standalone(String id, boolean shownInCreativeTab)
        {
            return new ItemDefinition(id, ItemKind.STANDALONE, shownInCreativeTab);
        }

        public String id()
        {
            return id;
        }

        public ItemKind kind()
        {
            return kind;
        }

        public boolean isShownInCreativeTab()
        {
            return shownInCreativeTab;
        }
    }
}
