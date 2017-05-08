package wk.easyonboard.ui.views;

import com.google.common.collect.Lists;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.ui.Grid;
import wk.easyonboard.common.datatransfer.EmployeeDTO;
import wk.easyonboard.ui.services.EmployeeService;

import java.util.List;
import java.util.UUID;

/**
 * Created by david.leyendecker on 26.04.2017.
 */
public class EmployeeListView extends OverviewViewBase<EmployeeDTO> {
    public EmployeeListView() {
        super(ApplicationView.EMPLOYEE_OVERVIEW, ApplicationView.EMPLOYEE);
    }

    @Override
    protected UUID getObjectId(EmployeeDTO firstSelectedItem) {
        return firstSelectedItem.getId();
    }

    @Override
    protected DataProvider<EmployeeDTO, ?> buildDataProvider() {
        return DataProvider.fromCallbacks(
                query -> EmployeeService.getInstance().getAll().stream(),
                query -> EmployeeService.getInstance().getCount()
        );
    }

    @Override
    protected void buildColumns(Grid<EmployeeDTO> grid) {
        grid.addColumn(EmployeeDTO::getLastName).setCaption("Last name");
        grid.addColumn(EmployeeDTO::getFirstName).setCaption("First name");
        grid.addColumn(EmployeeDTO::getEntersOn).setCaption("Entry date");
    }

    @Override
    protected List<ApplicationView> getActions() {
        return Lists.newArrayList(ApplicationView.EMPLOYEE_ADD);
    }
}
