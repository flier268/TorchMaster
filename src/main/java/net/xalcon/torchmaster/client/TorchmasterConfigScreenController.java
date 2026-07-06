package net.xalcon.torchmaster.client;

import net.xalcon.torchmaster.config.ITorchmasterConfig;
import net.xalcon.torchmaster.config.TorchmasterTomlConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class TorchmasterConfigScreenController
{
    private static final int ERROR_COLOR = 0xFFFF5555;
    private static final int SAVED_COLOR = 0xFF55FF55;
    private static final int RESET_COLOR = 0xFFFFFF55;
    private static final int DEFAULT_STATUS_COLOR = 0xFFA0A0A0;
    private static final ConfigRuntime DEFAULT_RUNTIME = TorchmasterConfigRuntimeAccess.DEFAULT;

    private final List<TorchmasterConfigWidgetRows.Row> rows;
    private int scrollOffset;
    private CompatText status = CompatText.empty();
    private int statusColor = DEFAULT_STATUS_COLOR;

    TorchmasterConfigScreenController()
    {
        this(new ArrayList<>());
    }

    TorchmasterConfigScreenController(List<TorchmasterConfigWidgetRows.Row> rows)
    {
        this.rows = rows;
    }

    static ConfigRuntime runtime()
    {
        return DEFAULT_RUNTIME;
    }

    void initialize(TorchmasterConfigScreenLayout layout, int screenHeight, int buttonHeight,
            TorchmasterConfigWidgetRows.WidgetFactory widgetFactory)
    {
        initialize(DEFAULT_RUNTIME.config(), layout, screenHeight, buttonHeight, widgetFactory);
    }

    void initialize(ITorchmasterConfig config, TorchmasterConfigScreenLayout layout, int screenHeight, int buttonHeight,
            TorchmasterConfigWidgetRows.WidgetFactory widgetFactory)
    {
        rows.clear();
        rows.addAll(TorchmasterConfigWidgetRows.create(TorchmasterConfigEntries.fromConfig(config), layout, buttonHeight, widgetFactory));
        updateRowPositions(layout, screenHeight, buttonHeight);
    }

    TorchmasterConfigScreenActions.ButtonDescriptor[] bottomButtons(TorchmasterConfigScreenLayout layout, int screenHeight, int buttonHeight)
    {
        return TorchmasterConfigScreenActions.bottomButtons(layout, screenHeight, buttonHeight);
    }

    ActionOutcome runAction(TorchmasterConfigScreenActions.Action action, ConfigRuntime runtime)
    {
        switch (action) {
            case SAVE:
                save(runtime);
                return ActionOutcome.NONE;
            case RESET:
                reset();
                return ActionOutcome.REBUILD_WIDGETS;
            case DONE:
                return ActionOutcome.CLOSE_SCREEN;
            default:
                throw new IllegalStateException("Unsupported config action " + action);
        }
    }

    boolean scroll(double delta, TorchmasterConfigScreenLayout layout, int screenHeight, int buttonHeight)
    {
        scrollOffset = TorchmasterConfigWidgetRows.scrollOffset(scrollOffset, delta, rows.size(), layout, screenHeight);
        updateRowPositions(layout, screenHeight, buttonHeight);
        return true;
    }

    TorchmasterScreenRenderPlan renderPlan(TorchmasterConfigScreenLayout layout)
    {
        return TorchmasterConfigScreenPresenter.plan(layout, rows, status, statusColor);
    }

    List<TorchmasterConfigWidgetRows.Row> rows()
    {
        return Collections.unmodifiableList(rows);
    }

    int scrollOffset()
    {
        return scrollOffset;
    }

    CompatText status()
    {
        return status;
    }

    int statusColor()
    {
        return statusColor;
    }

    private void save(ConfigRuntime runtime)
    {
        ITorchmasterConfig loadedConfig = runtime.config();
        if (!(loadedConfig instanceof TorchmasterTomlConfig)) {
            setStatus("screen.torchmaster.config.unsupported", ERROR_COLOR);
            return;
        }

        TorchmasterConfigEntries.Collector collector = TorchmasterConfigEntries.collector();
        for (TorchmasterConfigWidgetRows.Row row : rows) {
            TorchmasterConfigEntries.ReadResult result = row.read(collector);
            if (!result.isSuccess()) {
                setStatus(result.errorKey(), ERROR_COLOR);
                return;
            }
        }

        collector.toDraft().saveTo((TorchmasterTomlConfig)loadedConfig);
        runtime.reload();
        runtime.refreshRangeDisplay(runtime.config());
        setStatus("screen.torchmaster.config.saved", SAVED_COLOR);
    }

    private void reset()
    {
        scrollOffset = 0;
        setStatus("screen.torchmaster.config.reverted", RESET_COLOR);
    }

    private void updateRowPositions(TorchmasterConfigScreenLayout layout, int screenHeight, int buttonHeight)
    {
        TorchmasterConfigWidgetRows.updatePositions(rows, layout, scrollOffset, screenHeight, buttonHeight);
    }

    private void setStatus(String translationKey, int color)
    {
        status = CompatText.translatable(translationKey);
        statusColor = color;
    }

    interface ConfigRuntime
    {
        ITorchmasterConfig config();

        void reload();

        void refreshRangeDisplay(ITorchmasterConfig config);
    }

    enum ActionOutcome
    {
        NONE,
        REBUILD_WIDGETS,
        CLOSE_SCREEN
    }
}
