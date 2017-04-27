package wk.easyonboard.ui.services;

import wk.easyonboard.common.datatransfer.EmployeeDTO;
import wk.easyonboard.common.datatransfer.WorkflowDTO;
import wk.easyonboard.common.datatransfer.WorkflowItemStatusDTO;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

/**
 * Created by david.leyendecker on 27.04.2017.
 */
public class WorkflowService extends BackendService<WorkflowDTO> {
    private static WorkflowService instance;

    public static synchronized WorkflowService getInstance() {
        if (instance == null) {
            instance = new WorkflowService();
        }
        return instance;
    }

    @Override
    public List<WorkflowDTO> getAll() {
        return buildClientPathWithPath()
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildGet()
                .invoke(new GenericType<List<WorkflowDTO>>() {
                });
    }

    @Override
    public int getCount() {
        return buildClientPathWithPath()
                .path("count")
                .request(MediaType.TEXT_PLAIN)
                .buildGet()
                .invoke(Integer.class);
    }

    public List<WorkflowItemStatusDTO> getWorkflowStatus(EmployeeDTO employee) {
        UUID result = buildClient()
                .path("workflow")
                .path("employeeStatus")
                .path(employee.getId().toString())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildGet()
                .invoke(UUID.class);

        return buildClient()
                .path("workflow")
                .path(result.toString())
                .path("status")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildGet().invoke(new GenericType<List<WorkflowItemStatusDTO>>() {
                });
    }


    public UUID createWorkflowForUser(EmployeeDTO employee, WorkflowDTO workflowDTO) {
        UUID result = buildClient()
                .path("workflow")
                .path(workflowDTO.getId().toString())
                .path("start")
                .path(employee.getId().toString())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildPost(null)
                .invoke(UUID.class);
        return result;
    }

    private WebTarget buildClientPathWithPath() {
        return buildClient()
                .path("workflows");
    }
}
