package wk.easyonboard.ui.views;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;

import java.util.UUID;

/**
 * Created by david.leyendecker on 27.04.2017.
 */
public abstract class OverviewViewBase<DTO> extends HeaderViewBase {
    private final ApplicationView detailView;

    public OverviewViewBase(ApplicationView applicationView) {
        this(applicationView, null);
    }

    public OverviewViewBase(ApplicationView applicationView, ApplicationView detailView) {
        super(applicationView);
        this.detailView = detailView;
    }

    @Override
    protected Component buildContent() {
        Grid<DTO> grid = new Grid<>();
        buildColumns(grid);
        grid.setSizeFull();
        grid.setDataProvider(buildDataProvider());
        grid.addSelectionListener(selectionEvent -> {
            ((SingleSelectionEvent<DTO>) selectionEvent).getSelectedItem()
                    .map(this::getObjectId)
                    .ifPresent(detailView::navigateTo);
        });
        return grid;
    }

    protected abstract UUID getObjectId(DTO firstSelectedItem);

    protected abstract DataProvider<DTO, ?> buildDataProvider();

    protected abstract void buildColumns(Grid<DTO> grid);
}