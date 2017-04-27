package wk.easyonboard.ui.components;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import wk.easyonboard.common.datatransfer.EmployeeDTO;
import wk.easyonboard.ui.services.AuthenticationService;
import wk.easyonboard.ui.views.ApplicationView;

import java.util.Arrays;

/**
 * Created by david.leyendecker on 26.04.2017.
 */
public class MenuBarComponent extends CustomComponent {
    private static final String STYLE_VISIBLE = "valo-menu-visible";
    private MenuBar.MenuItem settingsItem;

    public MenuBarComponent() {
        setPrimaryStyleName(ValoTheme.MENU_ROOT);
        setId("app-menu");
        setSizeUndefined();

        setCompositionRoot(buildContent());
    }

    private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildUserMenu());
        menuContent.addComponent(buildToggleButton());
        menuContent.addComponent(buildMenuItems());

        return menuContent;
    }

    private Component buildTitle() {
        Label logo = new Label("Easy.on.board <strong>Dashboard</strong>", ContentMode.HTML);
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName(ValoTheme.MENU_TITLE);
        return logoWrapper;
    }

    private Component buildUserMenu() {
        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");

        settingsItem = settings.addItem("", new ThemeResource(
                "img/profile-pic-300px.jpg"), null);
        //updateUserName(null);
        settingsItem.addItem("Edit Profile", menuItem -> {
            //ProfilePreferencesWindow.open(user, false);
        });
        settingsItem.addItem("Preferences", menuItem -> {
            //ProfilePreferencesWindow.open(user, true);
        });
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", menuItem -> {

            //DashboardEventBus.post(new UserLoggedOutEvent());

        });
        updateUserName();
        return settings;
    }

    private Component buildToggleButton() {
        Button valoMenuToggleButton = new Button("Menu", clickEvent -> {
            if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
                getCompositionRoot().removeStyleName(STYLE_VISIBLE);
            } else {
                getCompositionRoot().addStyleName(STYLE_VISIBLE);
            }
        });
        valoMenuToggleButton.setIcon(FontAwesome.LIST);
        valoMenuToggleButton.addStyleName("valo-menu-toggle");
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
        return valoMenuToggleButton;
    }

    private Component buildMenuItems() {
        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");

        Arrays.stream(ApplicationView.values())
                .filter(ApplicationView::isShowInMenu)
                .map(ValoMenuItemButton::new)
                .forEach(menuItemsLayout::addComponent);

        return menuItemsLayout;
    }

    private void updateUserName() {
        EmployeeDTO employee = AuthenticationService.getInstance().getCurrentUser();
        settingsItem.setText(employee.getFirstName() + " " + employee.getLastName());
        Resource avatarResource = new ExternalResource("http://localhost:8080/VAADIN/img/" + employee.getAvatarUrl());
//        Image image = new Image("", avatarResource);
//        image.setWidth(200, Unit.PIXELS);
//        image.setWidth(200, Unit.PIXELS);
        settingsItem.setIcon(avatarResource);
    }

    private class ValoMenuItemButton extends Button {
        private final ApplicationView view;

        public ValoMenuItemButton(ApplicationView view) {
            this.view = view;
            setPrimaryStyleName(ValoTheme.MENU_ITEM);
            setIcon(view.getIcon());
            setCaption(view.getTitle().substring(0, 1).toUpperCase()
                    + view.getTitle().substring(1));
            addClickListener(clickEvent -> {
                view.navigateTo();
            });
        }
    }
}
