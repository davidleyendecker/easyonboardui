package wk.easyonboard.ui.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Resource;
import wk.easyonboard.ui.EasyonboardUi;

import java.util.UUID;

/**
 * Created by david.leyendecker on 26.04.2017.
 */
public enum ApplicationView {
    DASHBOARD("Overview", DashboardView.class, VaadinIcons.DASHBOARD, true),
    EMPLOYEE_OVERVIEW("Employees", EmployeeListView.class, VaadinIcons.GROUP, true),
    EMPLOYEE_ADD("Add new employee", EmployeeAddView.class, VaadinIcons.PLUS, false),
    EMPLOYEE("Employee", EmployeeView.class, VaadinIcons.MALE, false),
    WORKFLOW_OVERVIEW("Workflows", WorkflowListView.class, VaadinIcons.TASKS, true);

    private final String title;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean showInMenu;

    ApplicationView(String title, Class<? extends View> viewClass, Resource icon, boolean showInMenu) {
        this.title = title;
        this.viewClass = viewClass;
        this.icon = icon;
        this.showInMenu = showInMenu;
    }

    public String getTitle() {
        return this.title;
    }

    public Class<? extends View> getViewClass() {
        return this.viewClass;
    }

    public Resource getIcon() {
        return this.icon;
    }

    public boolean isShowInMenu() {
        return showInMenu;
    }

    public void navigateTo() {
        EasyonboardUi.getCurrent().getNavigator().navigateTo(this.getViewClass().getSimpleName());
    }

    public void navigateTo(UUID id) {
        EasyonboardUi.getCurrent().getNavigator().navigateTo(this.getViewClass().getSimpleName() + "/" + id.toString());
    }
}
