package wk.easyonboard.ui.services;

import wk.easyonboard.common.datatransfer.CompanyUnitDTO;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

/**
 * Created by david.leyendecker on 27.04.2017.
 */
public class CompanyUnitService extends BackendService<CompanyUnitDTO> {
    private static CompanyUnitService instance;

    public static synchronized CompanyUnitService getInstance() {
        if(instance == null) {
            instance = new CompanyUnitService();
        }
        return instance;
    }

    @Override
    public List<CompanyUnitDTO> getAll() {
        return buildClientPathWithPath()
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildGet()
                .invoke(new GenericType<List<CompanyUnitDTO>>() { });
    }

    public CompanyUnitDTO getById(UUID id) {
        if(id == null) {
            return null;
        }
        return buildClient().path("company")
                .path("unit")
                .path(id.toString())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildGet()
                .invoke(CompanyUnitDTO.class);
    }

    private WebTarget buildClientPathWithPath() {
        return buildClient()
                .path("company")
                .path(CompanyService.getInstance().getCurrent().getId().toString())
                .path("units");
    }

    @Override
    public int getCount() {
        return buildClientPathWithPath()
                .path("count")
                .request(MediaType.TEXT_PLAIN)
                .buildGet()
                .invoke(Integer.class);
    }
}
