package wk.easyonboard.ui.components;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import wk.easyonboard.ui.ViewNavigator;

/**
 * Created by david.leyendecker on 26.04.2017.
 */
public class MainComponent extends HorizontalLayout {
    public MainComponent() {
        setSizeFull();
        addStyleName("mainview");

        addComponent(new MenuBarComponent());
        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        new ViewNavigator(content);
    }
}
