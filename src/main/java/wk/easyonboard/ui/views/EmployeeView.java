package wk.easyonboard.ui.views;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import wk.easyonboard.common.datatransfer.*;
import wk.easyonboard.ui.services.EmployeeService;

import java.util.UUID;

/**
 * Created by david.leyendecker on 27.04.2017.
 */
public class EmployeeView extends HeaderViewBase {
    private EmployeeDTO employee;
    private Binder<EmployeeDTO> binder;

    public EmployeeView() {
        super(ApplicationView.EMPLOYEE);
    }

    @Override
    protected Component buildContent() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.addComponent(buildMasterData());
        return layout;
    }

    private Component buildWorkflowTimeline() {
        VerticalLayout layout = new VerticalLayout();

        layout.addComponent(createTimeLine(true, "Send welcome mail to " + employee.getEmail()));
        layout.addComponent(createTimeLine(true, "Create atlassian account for " + employee.getUsername()));
        layout.addComponent(createTimeLine(false, "Create LDAP account for " + employee.getEmail()));

        return buildBorderPanel(layout);
    }

    private Component createTimeLine(boolean isSuccess, String name) {
        HorizontalLayout line = new HorizontalLayout();

        Label titleLabel = new Label();
        titleLabel.setContentMode(ContentMode.HTML);
        VaadinIcons icon = isSuccess ? VaadinIcons.CHECK_SQUARE_O : VaadinIcons.THIN_SQUARE;
        titleLabel.setValue(icon.getHtml() + " " + name);
        line.addComponent(titleLabel);

        return line;
    }

    private Component buildMasterData() {
        this.employee = new EmployeeDTO();
        this.employee.setAdress(new AdressDTO());
        this.binder = new Binder<>();

        FormLayout formLayout = new FormLayout();
        formLayout.setSizeFull();
        formLayout.setMargin(true);

        TextField lastNameField = new TextField("Last name");
        lastNameField.setSizeFull();
        lastNameField.setReadOnly(true);
        binder.forField(lastNameField)
                .asRequired("Last name is a requiered ")
                .bind(EmployeeDTO::getLastName, EmployeeDTO::setLastName);
        formLayout.addComponent(lastNameField);

        TextField firstNameField = new TextField("First name");
        firstNameField.setSizeFull();
        firstNameField.setReadOnly(true);
        binder.forField(firstNameField)
                .asRequired("First name is a requiered ")
                .bind(EmployeeDTO::getFirstName, EmployeeDTO::setFirstName);
        formLayout.addComponent(firstNameField);

        TextField usernameField = new TextField("Username");
        usernameField.setSizeFull();
        usernameField.setReadOnly(true);
        binder.forField(firstNameField)
                .asRequired("Username is a requiered ")
                .bind(EmployeeDTO::getUsername, EmployeeDTO::setUsername);
        formLayout.addComponent(usernameField);

        TextField mailAddressField = new TextField("eMail");
        mailAddressField.setSizeFull();
        mailAddressField.setReadOnly(true);
        binder.forField(mailAddressField)
                .asRequired("eMail is a requiered ")
                .bind(EmployeeDTO::getEmail, EmployeeDTO::setEmail);
        formLayout.addComponent(mailAddressField);

        DateField entersOnField = new DateField("Entery date");
        entersOnField.setReadOnly(true);
        binder.forField(entersOnField)
                .asRequired("Enters on is a requiered ")
                .bind(EmployeeDTO::getEntersOn, EmployeeDTO::setEntersOn);
        formLayout.addComponent(entersOnField);

        return buildBorderPanel(formLayout);
    }

    private static Component buildBorderPanel(Component children) {
        Panel outer = new Panel(children);
        outer.setWidth(50, Unit.PERCENTAGE);
        return outer;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if(viewChangeEvent.getParameters() != null){
            // split at "/", add each part as a label
            String[] params = viewChangeEvent.getParameters().split("/");
            UUID uuid = UUID.fromString(params[params.length - 1]);
            this.employee = EmployeeService.getInstance().getById(uuid);
            this.binder.readBean(this.employee);

            getRoot().addComponent(buildWorkflowTimeline());
        }
    }
}
