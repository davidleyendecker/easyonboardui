package wk.easyonboard.ui.components;

import com.vaadin.ui.VerticalLayout;
import wk.easyonboard.ui.extensions.ComponentFactory;

/**
 * Created by david.leyendecker on 27.04.2017.
 */
public class HeaderComponent extends VerticalLayout {
    public HeaderComponent(String title, String id) {
        addStyleName("viewheader");
        setSpacing(true);

        this.addComponent(ComponentFactory.createViewHeader(id, title));
    }
}
