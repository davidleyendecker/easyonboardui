package wk.easyonboard.ui.views;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import wk.easyonboard.common.datatransfer.EmployeeDTO;
import wk.easyonboard.ui.extensions.ComponentFactory;
import wk.easyonboard.ui.extensions.Resources;
import wk.easyonboard.ui.services.EmployeeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Created by david.leyendecker on 26.04.2017.
 */
public class DashboardView extends HeaderViewBase {
    public DashboardView() {
        super(ApplicationView.DASHBOARD);
    }

    @Override
    protected Component buildContent() {
        VerticalLayout current = new VerticalLayout();
        current.addComponent(ComponentFactory.createSubHeader("New employees"));
        current.setMargin(false);

        CssLayout wrapper = new CssLayout();
        wrapper.setStyleName("dashboard-employees");
        wrapper.setWidthUndefined();
        EmployeeService.getInstance()
                .getAll()
                .stream()
                .filter(e -> isWithinNext14Days(e.getEntersOn()))
                .forEach(e -> {
                    wrapper.addComponent(buildEmployeeCard(e));
                    Panel spacing = new Panel();
                    spacing.setWidth(10, Unit.PIXELS);
                    spacing.setStyleName(ValoTheme.PANEL_BORDERLESS);
                    wrapper.addComponent(spacing);
                });

        current.addComponent(wrapper);

        return current;
    }

    private static boolean isWithinNext14Days(LocalDate date) {
        return date != null && (date.isAfter(LocalDate.now().minusDays(1)) && date.isBefore(LocalDate.now().plusDays(14)));
    }

    private Component buildEmployeeCard(EmployeeDTO employee) {
        VerticalLayout verticalLayout = new VerticalLayout(
                Resources.createAvatarImage(employee),
                new Label(String.format("%s %s", employee.getFirstName(), employee.getLastName())),
                new Label(employee.getEntersOn().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
        );

        Panel components = new Panel(verticalLayout);
        components.setWidth(125, Unit.PIXELS);
        return components;
    }
}