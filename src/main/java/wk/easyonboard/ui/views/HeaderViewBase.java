package wk.easyonboard.ui.views;

import com.google.common.collect.Lists;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by david.leyendecker on 26.04.2017.
 */
public abstract class HeaderViewBase extends Panel implements View {
    private Label titleLabel;
    private final VerticalLayout root;
    private final ApplicationView applicationView;

    protected VerticalLayout getRoot() {
        return root;
    }

    public HeaderViewBase(ApplicationView applicationView) {
        this.applicationView = applicationView;

        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();

        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName(String.format("%s-view", applicationView.getViewClass().getSimpleName()));
        setContent(root);
        Responsive.makeResponsive(root);

        root.addComponent(buildHeader());

        Component content = buildContent();
        root.addComponent(content);

        root.setExpandRatio(content, 1);
    }

    protected abstract Component buildContent();

    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);

        titleLabel = new Label(applicationView.getTitle());
        titleLabel.setId(applicationView.getViewClass().getSimpleName() + "-title");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        List<ApplicationView> actions = getActions();
        if (actions != null && !actions.isEmpty()) {

            HorizontalLayout tools = new HorizontalLayout();
            actions.stream().map(headerAction -> {
                Button button = new Button("", clickEvent -> headerAction.navigateTo());
                button.setIcon(headerAction.getIcon());
                button.setIconAlternateText(headerAction.getTitle());
                return button;
            }).forEach(tools::addComponent);

            tools.setSpacing(true);
            tools.addStyleName("toolbar");
            header.addComponent(tools);
        }

        return header;
    }

    protected List<ApplicationView> getActions() {
        return Lists.newArrayList();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

}
