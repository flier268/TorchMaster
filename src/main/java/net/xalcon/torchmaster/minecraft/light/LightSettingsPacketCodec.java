package net.xalcon.torchmaster.minecraft.light;

//? if >=1.16
import net.minecraft.network.PacketByteBuf;
//? if <1.16
//import net.minecraft.util.PacketByteBuf;
import net.xalcon.torchmaster.blocks.LightType;
import net.xalcon.torchmaster.domain.LightSettings;
import net.xalcon.torchmaster.port.LightAccessEntry;
import net.xalcon.torchmaster.port.LightSettingsView;

public final class LightSettingsPacketCodec
{
    public static final int QUERY = 0;
    public static final int UPDATE = 1;
    public static final int ADD_ACCESS = 2;
    public static final int REMOVE_ACCESS = 3;
    public static final int SNAPSHOT = 4;
    public static final int RANGE_VISIBLE = 5;
    public static final int VISIBLE_RANGES_QUERY = 6;

    private LightSettingsPacketCodec()
    {
    }

    public static void writeType(PacketByteBuf buf, LightType lightType)
    {
        buf.writeInt(lightType == LightType.MegaTorch ? 0 : 1);
    }

    public static LightType readType(PacketByteBuf buf)
    {
        return buf.readInt() == 0 ? LightType.MegaTorch : LightType.DreadLamp;
    }

    public static void writeSettings(PacketByteBuf buf, LightSettings settings)
    {
        buf.writeBoolean(settings.enabled());
        buf.writeInt(settings.rangeWest());
        buf.writeInt(settings.rangeEast());
        buf.writeInt(settings.rangeDown());
        buf.writeInt(settings.rangeUp());
        buf.writeInt(settings.rangeNorth());
        buf.writeInt(settings.rangeSouth());
    }

    public static LightSettings readSettings(PacketByteBuf buf)
    {
        return LightSettings.configured(buf.readBoolean(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    public static void writeSnapshot(PacketByteBuf buf, LightSettingsView snapshot)
    {
        buf.writeBoolean(snapshot.found());
        buf.writeBoolean(snapshot.editable());
        buf.writeBoolean(snapshot.accessManageable());
        buf.writeBoolean(snapshot.enabled());
        buf.writeInt(snapshot.rangeWest());
        buf.writeInt(snapshot.rangeEast());
        buf.writeInt(snapshot.rangeDown());
        buf.writeInt(snapshot.rangeUp());
        buf.writeInt(snapshot.rangeNorth());
        buf.writeInt(snapshot.rangeSouth());
        buf.writeInt(snapshot.globalMax());
        buf.writeBoolean(snapshot.chunkAligned());
        buf.writeBoolean(snapshot.rangeVisible());
        buf.writeBoolean(snapshot.appliesToSettings());
        buf.writeBoolean(snapshot.previewSyncStart());
        buf.writeBoolean(snapshot.previewSyncEnd());
        LightAccessEntry[] accessEntries = snapshot.accessEntries();
        buf.writeInt(accessEntries.length);
        for (LightAccessEntry entry : accessEntries) {
            buf.writeString(entry.uuidString());
            buf.writeString(entry.name());
        }
    }

    public static LightSettingsView readSnapshot(PacketByteBuf buf)
    {
        boolean found = buf.readBoolean();
        boolean editable = buf.readBoolean();
        boolean accessManageable = buf.readBoolean();
        boolean enabled = buf.readBoolean();
        int rangeWest = buf.readInt();
        int rangeEast = buf.readInt();
        int rangeDown = buf.readInt();
        int rangeUp = buf.readInt();
        int rangeNorth = buf.readInt();
        int rangeSouth = buf.readInt();
        int globalMax = buf.readInt();
        boolean chunkAligned = buf.readBoolean();
        boolean rangeVisible = buf.readBoolean();
        boolean appliesToSettings = buf.readBoolean();
        boolean previewSyncStart = buf.readBoolean();
        boolean previewSyncEnd = buf.readBoolean();
        int accessCount = buf.readInt();
        LightAccessEntry[] accessEntries = new LightAccessEntry[accessCount];
        for (int i = 0; i < accessCount; i++) {
            String uuid = buf.readString();
            String name = buf.readString();
            try {
                accessEntries[i] = new LightAccessEntry(java.util.UUID.fromString(uuid), name);
            } catch (IllegalArgumentException ignored) {
                accessEntries[i] = new LightAccessEntry(null, name);
            }
        }
        if (found) {
            return LightSettingsView.present(editable, accessManageable, enabled, rangeWest, rangeEast, rangeDown, rangeUp, rangeNorth, rangeSouth,
                    globalMax, chunkAligned, rangeVisible, accessEntries);
        }
        if (previewSyncStart) {
            return LightSettingsView.previewSyncStartMarker();
        }
        if (previewSyncEnd) {
            return LightSettingsView.previewSyncEndMarker();
        }
        return appliesToSettings ? LightSettingsView.missing(globalMax) : LightSettingsView.previewRemoved(globalMax);
    }
}
