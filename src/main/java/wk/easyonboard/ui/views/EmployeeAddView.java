package wk.easyonboard.ui.views;

import com.google.common.collect.Lists;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;
import wk.easyonboard.common.datatransfer.AdressDTO;
import wk.easyonboard.common.datatransfer.CompanyUnitDTO;
import wk.easyonboard.common.datatransfer.EmployeeDTO;
import wk.easyonboard.common.datatransfer.WorkflowDTO;
import wk.easyonboard.ui.services.CompanyUnitService;
import wk.easyonboard.ui.services.EmployeeService;
import wk.easyonboard.ui.services.WorkflowService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by david.leyendecker on 26.04.2017.
 */
public class EmployeeAddView extends HeaderViewBase {
    private EmployeeDTO employee;
    private Binder<EmployeeDTO> binder;
    private ComboBox<WorkflowDTO> workflowsCombobox;

    public EmployeeAddView() {
        super(ApplicationView.EMPLOYEE_ADD);
    }

    @Override
    protected Component buildContent() {

        this.employee = new EmployeeDTO();
        this.employee.setAdress(new AdressDTO());
        this.binder = new Binder<>();

        FormLayout formLayout = new FormLayout();
        formLayout.setSizeFull();
        formLayout.setMargin(true);

        TextField lastNameField = new TextField("Last name");
        lastNameField.setSizeFull();
        binder.forField(lastNameField)
                .asRequired("Last name is a requiered ")
                .bind(EmployeeDTO::getLastName, EmployeeDTO::setLastName);
        formLayout.addComponent(lastNameField);

        TextField firstNameField = new TextField("First name");
        firstNameField.setSizeFull();
        binder.forField(firstNameField)
                .asRequired("First name is a requiered ")
                .bind(EmployeeDTO::getFirstName, EmployeeDTO::setFirstName);
        formLayout.addComponent(firstNameField);

        TextField usernameField = new TextField("Username");
        usernameField.setSizeFull();
        binder.forField(firstNameField)
                .asRequired("Username is a requiered ")
                .bind(EmployeeDTO::getUsername, EmployeeDTO::setUsername);
        formLayout.addComponent(usernameField);


        Runnable updateUsername = () -> {
            if(!lastNameField.isEmpty() && !firstNameField.isEmpty() && usernameField.isEmpty()) {
                usernameField.setValue(firstNameField.getValue().toLowerCase() + "." + lastNameField.getValue().toLowerCase());
            }
        };
        lastNameField.addValueChangeListener(valueChangeEvent -> {
            updateUsername.run();
        });
        firstNameField.addValueChangeListener(valueChangeEvent -> {
            updateUsername.run();
        });


        TextField mailAddressField = new TextField("eMail");
        mailAddressField.setSizeFull();
        binder.forField(mailAddressField)
                .asRequired("eMail is a requiered ")
                .bind(EmployeeDTO::getEmail, EmployeeDTO::setEmail);
        formLayout.addComponent(mailAddressField);

        DateField entersOnField = new DateField("Enters on");
        binder.forField(entersOnField)
                .asRequired("Enters on is a requiered ")
                .bind(EmployeeDTO::getEntersOn, EmployeeDTO::setEntersOn);
        formLayout.addComponent(entersOnField);

        ComboBox<CompanyUnitDTO> companyUnitsComboBox = createComboBox("Company unit", CompanyUnitService.getInstance().getAll(), CompanyUnitDTO::getName);
        companyUnitsComboBox.setEmptySelectionCaption("Please select a company unit");
        binder.forField(companyUnitsComboBox)
                .asRequired("Company unit is requiered")
                .withConverter(CompanyUnitDTO::getId, id -> CompanyUnitService.getInstance().getById(id))
                .bind(EmployeeDTO::getCompanyUnitId, EmployeeDTO::setCompanyUnitId);

        ComboBox<EmployeeDTO> mentorsComboBox = createComboBox("Mentor", createMentorsListProvider(companyUnitsComboBox), c -> c.getFirstName() + " " + c.getLastName());
        companyUnitsComboBox.addValueChangeListener(valueChangeEvent -> {
            mentorsComboBox.setSelectedItem(null);
            mentorsComboBox.setDataProvider(createMentorsListProvider(companyUnitsComboBox));
        });
        binder.forField(mentorsComboBox)
                .asRequired("Mentor is requiered")
                .withConverter(EmployeeDTO::getId, id -> EmployeeService.getInstance().getById(id))
                .bind(EmployeeDTO::getMentorId, EmployeeDTO::setMentorId);

        formLayout.addComponent(companyUnitsComboBox);
        formLayout.addComponent(mentorsComboBox);

        workflowsCombobox = createComboBox("Workflows", WorkflowService.getInstance().getAll(), WorkflowDTO::getName);
        formLayout.addComponent(workflowsCombobox);

        binder.readBean(employee);

        Button save = new Button("Save", this::save);
        Button cancel = new Button("Cancel", this::cancel);

        HorizontalLayout buttonBar = new HorizontalLayout(cancel, save);
        formLayout.addComponent(buttonBar);

        Panel outer = new Panel(formLayout);
        outer.setWidth(50, Unit.PERCENTAGE);
        return outer;
    }

    private static ListDataProvider<EmployeeDTO> createMentorsListProvider(ComboBox<CompanyUnitDTO> source) {
        List<UUID> mentorIds = source.getSelectedItem().map(CompanyUnitDTO::getManager).orElseGet(Lists::newArrayList);
        List<EmployeeDTO> items = EmployeeService.getInstance().getAll()
                .stream()
                .filter(employee -> mentorIds.contains(employee.getId()))
                .collect(Collectors.toList());
        return new ListDataProvider<>(items);
    }

    private static <DTO> ComboBox<DTO> createComboBox(String caption, List<DTO> items,
                                                      ItemCaptionGenerator<DTO> captionGenerator) {
        ListDataProvider<DTO> dataProvider = new ListDataProvider<>(items);
        return createComboBox(caption, dataProvider, captionGenerator);
    }

    private static <DTO> ComboBox<DTO> createComboBox(String caption, ListDataProvider<DTO> dataProvider,
                                                      ItemCaptionGenerator<DTO> captionGenerator) {
        ComboBox<DTO> comboBox = new ComboBox<>(caption);
        comboBox.setItemCaptionGenerator(captionGenerator);
        comboBox.setDataProvider(dataProvider);
        comboBox.setEmptySelectionAllowed(false);
        comboBox.setTextInputAllowed(false);
        return comboBox;
    }

    public void save(Button.ClickEvent event) {

        // Commit the fields from UI to DAO
        if (!binder.writeBeanIfValid(employee)) {
            Notification.show("Invalid input data");
        } else {
            // Save DAO to backend with direct synchronous service API
            EmployeeDTO newEmployee = EmployeeService.getInstance().persist(employee);
            workflowsCombobox.getSelectedItem().ifPresent(workflowDTO -> {
                WorkflowService.getInstance().createWorkflowForUser(newEmployee, workflowDTO);
            });
            String msg = String.format("Saved '%s %s'.", newEmployee.getFirstName(), newEmployee.getLastName());
            Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
            ApplicationView.EMPLOYEE_OVERVIEW.navigateTo();
        }
    }

    public void cancel(Button.ClickEvent event) {
        ApplicationView.EMPLOYEE_OVERVIEW.navigateTo();
    }
}
