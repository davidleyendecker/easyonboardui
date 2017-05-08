package wk.easyonboard.ui.extensions;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

public class ComponentFactory {
    private ComponentFactory() {
    }

    public static Label createViewHeader(String id, String headerText) {
        final Label titleLabel = new Label(headerText);
        titleLabel.setId(id + "-title");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        return titleLabel;
    }

    public static Label createSubHeader(String headerText) {
        Label subHeader = new Label(headerText);
        subHeader.setStyleName(ValoTheme.LABEL_H2);
        return subHeader;
    }

    public static CssLayout crateSidebar() {
        final CssLayout sidebar = new CssLayout();
        sidebar.addStyleName("sidebar");
        sidebar.addStyleName(ValoTheme.MENU_PART);
        sidebar.addStyleName("no-vertical-drag-hints");
        sidebar.addStyleName("no-horizontal-drag-hints");
        sidebar.setWidth(null);
        sidebar.setHeight("100%");
        return sidebar;
    }
}
