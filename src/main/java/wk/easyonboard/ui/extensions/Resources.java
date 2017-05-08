package wk.easyonboard.ui.extensions;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Image;
import wk.easyonboard.common.datatransfer.EmployeeDTO;
import wk.easyonboard.ui.EasyonboardServlet;

import javax.ws.rs.core.UriBuilder;

public class Resources {
    private Resources() {
    }

    public static ExternalResource getAvatarResource(EmployeeDTO employee) {
        UriBuilder imgPathBuilder = UriBuilder.fromUri(EasyonboardServlet.WEBCONTENT).path("img");
        if(employee == null || employee.getAvatarUrl() == null || employee.getAvatarUrl().isEmpty()) {
            return new ExternalResource(imgPathBuilder.path("avatar.jpg").build().toString());
        }
        return new ExternalResource(imgPathBuilder.path(employee.getAvatarUrl()).build().toString());
    }

    public static Image createAvatarImage(EmployeeDTO employee) {
        Resource avatarResource = Resources.getAvatarResource(employee);
        Image image = new Image("", avatarResource);
        image.setWidth(100, Sizeable.Unit.PIXELS);
        return image;
    }
}
