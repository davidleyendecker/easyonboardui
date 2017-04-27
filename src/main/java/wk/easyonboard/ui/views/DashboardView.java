package wk.easyonboard.ui.views;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import wk.easyonboard.common.datatransfer.EmployeeDTO;
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
        VerticalLayout current = new VerticalLayout(
                new Label("New employees"));
        current.setMargin(false);

        CssLayout wrapper = new CssLayout();
        EmployeeService.getInstance()
                .getAll()
                .stream()
                .filter(e -> isWithinNext14Days(e.getEntersOn()))
                .forEach(e -> wrapper.addComponent(buildEmployeeCard(e)));

        current.addComponent(wrapper);
        return current;
    }

    private static boolean isWithinNext14Days(LocalDate date) {
        if(date == null) {
            return false;
        }
        return date.isAfter(LocalDate.now().minusDays(1)) && date.isBefore(LocalDate.now().plusDays(14));
    }

    private Component buildEmployeeCard(EmployeeDTO employee) {
        AbsoluteLayout layout = new AbsoluteLayout();
        layout.setWidth(220, Unit.PIXELS);
        layout.setHeight(330, Unit.PIXELS);

        Resource avatarResource = new ExternalResource("http://localhost:8080/VAADIN/img/avatar.jpg");
        Image image = new Image("", avatarResource);
        image.setWidth(200, Unit.PIXELS);
        image.setWidth(200, Unit.PIXELS);
        VerticalLayout verticalLayout = new VerticalLayout(
                image,
                new Label(String.format("%s %s", employee.getFirstName(), employee.getLastName())),
                new Label(employee.getEntersOn().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
        );
        layout.addComponent(verticalLayout);
        Panel components = new Panel(layout);
        components.setWidth(250, Unit.PIXELS);
        return components;
    }
}