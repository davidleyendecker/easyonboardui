package wk.easyonboard.ui.services;

import wk.easyonboard.common.datatransfer.EmployeeDTO;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

/**
 * Created by david.leyendecker on 26.04.2017.
 */
public class EmployeeService extends BackendService<EmployeeDTO> {
    private static EmployeeService instance;

    public synchronized static EmployeeService getInstance() {
        if (instance == null) {
            instance = new EmployeeService();
        }
        return instance;
    }

    public EmployeeDTO persist(EmployeeDTO employee) {
        UUID entity = buildClientPathWithPath()
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPost(Entity.entity(employee, MediaType.APPLICATION_JSON_TYPE))
                .invoke(UUID.class);

        EmployeeDTO employeeDTO = getById(entity);
        return employeeDTO;
    }

    @Override
    public List<EmployeeDTO> getAll() {
        return buildClientPathWithPath()
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildGet().invoke(new GenericType<List<EmployeeDTO>>() { });
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
        return buildClient().path("employees");
    }

    public EmployeeDTO getById(UUID id) {
        if(id == null) {
            return null;
        }
        return buildClient().path("employee")
                .path(id.toString())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildGet()
                .invoke(EmployeeDTO.class);
    }
}
