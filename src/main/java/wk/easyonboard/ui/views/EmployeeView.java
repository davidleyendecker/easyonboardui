package wk.easyonboard.ui.views;

import com.google.common.collect.Lists;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import wk.easyonboard.common.datatransfer.*;
import wk.easyonboard.ui.services.EmployeeService;
import wk.easyonboard.ui.services.WorkflowService;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by david.leyendecker on 27.04.2017.
 */
public class EmployeeView extends HeaderViewBase {
    private EmployeeDTO employee;
    private Binder<EmployeeDTO> binder;
    private VerticalLayout innerContainer;

    public EmployeeView() {
        super(ApplicationView.EMPLOYEE);
    }

    @Override
    protected Component buildContent() {
        this.innerContainer = new VerticalLayout();
        this.innerContainer.setSpacing(true);
        this.innerContainer.setMargin(false);
        this.innerContainer.addComponent(buildMasterData());
        return this.innerContainer;
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
                .bind(EmployeeDTO::getLastName, EmployeeDTO::setLastName);
        formLayout.addComponent(lastNameField);

        TextField firstNameField = new TextField("First name");
        firstNameField.setSizeFull();
        firstNameField.setReadOnly(true);
        binder.forField(firstNameField)
                .bind(EmployeeDTO::getFirstName, EmployeeDTO::setFirstName);
        formLayout.addComponent(firstNameField);

        TextField usernameField = new TextField("Username");
        usernameField.setSizeFull();
        usernameField.setReadOnly(true);
        binder.forField(firstNameField)
                .bind(EmployeeDTO::getUsername, EmployeeDTO::setUsername);
        formLayout.addComponent(usernameField);

        TextField mailAddressField = new TextField("eMail");
        mailAddressField.setSizeFull();
        mailAddressField.setReadOnly(true);
        binder.forField(mailAddressField)
                .bind(EmployeeDTO::getEmail, EmployeeDTO::setEmail);
        formLayout.addComponent(mailAddressField);

        DateField entersOnField = new DateField("Entery date");
        entersOnField.setReadOnly(true);
        binder.forField(entersOnField)
                .bind(EmployeeDTO::getEntersOn, EmployeeDTO::setEntersOn);
        formLayout.addComponent(entersOnField);

        return buildBorderPanel(formLayout);
    }

    private static Component buildBorderPanel(Component children) {
        Panel outer = new Panel(children);
        outer.setWidth(50, Unit.PERCENTAGE);
        return outer;
    }

    private List<Component> buildWorkflowTimeline(EmployeeDTO employee) {
        List<Component> components = Lists.newArrayList();

        List<UUID> runningWorkflows = WorkflowService.getInstance().getRunningWorkflows(employee);
        for (UUID runningWorkflow : runningWorkflows) {
            VerticalLayout workflowContainer = new VerticalLayout();

            List<WorkflowItemStatusDTO> workflowStatus = WorkflowService.getInstance().getWorkflowStatus(runningWorkflow);
            WorkflowItemStatusDTO workflowItemStatusDTO = workflowStatus.get(0);
            WorkflowDTO byId = WorkflowService.getInstance().getById(workflowItemStatusDTO.getOriginalWorkflowId());
            Label workflowTitle = new Label(byId.getName());
            workflowTitle.setStyleName(ValoTheme.LABEL_H2);
            workflowContainer.addComponent(workflowTitle);

            Map<UUID, WorkflowItemDTO> itemsFromOriginalById = byId.getItems().stream().collect(Collectors.toMap(WorkflowItemDTO::getId, x -> x));
            for (WorkflowItemStatusDTO itemStatusDTO : workflowStatus) {
                workflowContainer.addComponent(
                        createTimeLine(itemStatusDTO.isSuccess(), itemsFromOriginalById.get(itemStatusDTO.getWorkflowItemId()).getName())
                );
            }

            components.add(buildBorderPanel(workflowContainer));
        }
        return components;
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

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if(viewChangeEvent.getParameters() != null){

            String[] params = viewChangeEvent.getParameters().split("/");
            UUID uuid = UUID.fromString(params[params.length - 1]);
            this.employee = EmployeeService.getInstance().getById(uuid);
            this.binder.readBean(this.employee);

            buildWorkflowTimeline(this.employee).forEach(innerContainer::addComponent);
        }
    }
}
