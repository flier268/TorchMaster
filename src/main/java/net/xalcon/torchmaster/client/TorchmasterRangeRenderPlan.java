package net.xalcon.torchmaster.client;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TorchmasterRangeRenderPlan
{
    private final List<Entry> entries;

    private TorchmasterRangeRenderPlan(List<Entry> entries)
    {
        this.entries = entries;
    }

    public static TorchmasterRangeRenderPlan fromSnapshots(Iterable<TorchmasterLightRangeDisplay.RangeSnapshot> snapshots)
    {
        List<Entry> entries = new ArrayList<>();
        for (TorchmasterLightRangeDisplay.RangeSnapshot snapshot : snapshots) {
            TorchmasterLineBoxRenderer.Style rangeStyle = TorchmasterLineBoxRenderer.rangeStyle(snapshot.pos);
            TorchmasterLineBoxRenderer.Style sampleStyle = TorchmasterLineBoxRenderer.sampleStyle(snapshot.pos);
            entries.add(new Entry(snapshot.rangeBox, rangeStyle));
            for (BlockPos pos : snapshot.randomAirBlocks) {
                entries.add(new Entry(TorchmasterRangeBoxes.sampleBox(pos), sampleStyle));
            }
        }
        return new TorchmasterRangeRenderPlan(Collections.unmodifiableList(entries));
    }

    public List<Entry> entries()
    {
        return entries;
    }

    public static final class Entry
    {
        public final TorchmasterRangeBoxes.Box box;
        public final TorchmasterLineBoxRenderer.Style style;

        private Entry(TorchmasterRangeBoxes.Box box, TorchmasterLineBoxRenderer.Style style)
        {
            this.box = box;
            this.style = style;
        }
    }
}
