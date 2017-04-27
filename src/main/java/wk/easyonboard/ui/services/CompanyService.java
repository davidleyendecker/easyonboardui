package wk.easyonboard.ui.services;

import wk.easyonboard.common.datatransfer.CompanyDTO;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by david.leyendecker on 27.04.2017.
 */
public class CompanyService extends BackendService<CompanyDTO> {
    private static CompanyService instance;

    public static synchronized CompanyService getInstance() {
        if(instance == null) {
            instance = new CompanyService();
        }
        return instance;
    }

    @Override
    public List<CompanyDTO> getAll() {
        return buildClientPathWithPath()
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildGet()
                .invoke(new GenericType<List<CompanyDTO>>() { });
    }

    @Override
    public int getCount() {
        return buildClientPathWithPath()
                .path("count")
                .request(MediaType.TEXT_PLAIN)
                .buildGet()
                .invoke(Integer.class);
    }

    private WebTarget buildClientPathWithPath() {
        return buildClient().path("companies");
    }

    public CompanyDTO getCurrent() {
        return getAll().get(0);
    }
}
