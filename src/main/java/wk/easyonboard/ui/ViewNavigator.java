package wk.easyonboard.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.ui.ComponentContainer;
import wk.easyonboard.ui.views.ApplicationView;

import java.util.Arrays;

/**
 * Created by david.leyendecker on 26.04.2017.
 */
public class ViewNavigator extends Navigator {

    public ViewNavigator(ComponentContainer container) {
        super(EasyonboardUi.getCurrent(), container);
        initViewProviders();
    }

    private void initViewProviders() {

        Arrays.stream(ApplicationView.values())
                .map(applicationView -> new ClassBasedViewProvider(applicationView.getViewClass().getSimpleName(), applicationView.getViewClass()) {
                    @Override
                    public View getView(final String viewName) {
                        View result = null;
                        if (applicationView.getViewClass().getSimpleName().equals(viewName)) {
                            result = super.getView(applicationView.getViewClass().getSimpleName());
                        }
                        return result;
                    }
                })
                .forEach(this::addProvider);
    }
}