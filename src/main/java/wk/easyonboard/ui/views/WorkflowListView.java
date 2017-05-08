package wk.easyonboard.ui.views;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.ui.Grid;
import wk.easyonboard.common.datatransfer.WorkflowDTO;
import wk.easyonboard.ui.services.WorkflowService;

import java.util.UUID;

/**
 * Created by david.leyendecker on 27.04.2017.
 */
public class WorkflowListView extends OverviewViewBase<WorkflowDTO> {
    public WorkflowListView() {
        super(ApplicationView.WORKFLOW_OVERVIEW);
    }

    @Override
    protected UUID getObjectId(WorkflowDTO firstSelectedItem) {
        return firstSelectedItem.getId();
    }

    @Override
    protected DataProvider<WorkflowDTO, ?> buildDataProvider() {
        return DataProvider.fromCallbacks(
                query -> WorkflowService.getInstance().getAll().stream(),
                query -> WorkflowService.getInstance().getCount()
        );
    }

    @Override
    protected void buildColumns(Grid<WorkflowDTO> grid) {
        grid.addColumn(WorkflowDTO::getName).setCaption("Name");
    }
}
