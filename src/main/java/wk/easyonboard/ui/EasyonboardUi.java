package wk.easyonboard.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import wk.easyonboard.ui.components.MainComponent;
import wk.easyonboard.ui.services.AuthenticationService;
import wk.easyonboard.ui.views.ApplicationView;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class EasyonboardUi extends UI {
    protected void init(VaadinRequest vaadinRequest) {

        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        AuthenticationService.getInstance().login("davidleyendecker", "123");

        setContent(new MainComponent());
        ApplicationView.DASHBOARD.navigateTo();
    }
}
