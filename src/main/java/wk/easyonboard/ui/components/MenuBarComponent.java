package wk.easyonboard.ui.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import wk.easyonboard.common.datatransfer.EmployeeDTO;
import wk.easyonboard.ui.extensions.ComponentFactory;
import wk.easyonboard.ui.extensions.Resources;
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
        final CssLayout menuContent = ComponentFactory.crateSidebar();

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

        settingsItem = settings.addItem("", Resources.getAvatarResource(null), null);

        settingsItem.addItem("Edit Profile", menuItem -> {
        });
        settingsItem.addItem("Preferences", menuItem -> {
        });
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", menuItem -> {
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
        settingsItem.setIcon(Resources.getAvatarResource(employee));
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
