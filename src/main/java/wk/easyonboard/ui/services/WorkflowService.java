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

    public WorkflowDTO getById(UUID id) {
        if(id == null) {
            return null;
        }
        return buildClient().path("workflow")
                .path(id.toString())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildGet()
                .invoke(WorkflowDTO.class);
    }

    public List<UUID> getRunningWorkflows(EmployeeDTO employee) {
        return buildClient()
                .path("employee")
                .path(employee.getId().toString())
                .path("runningWorkflows")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildGet()
                .invoke(new GenericType<List<UUID>>() {
                });
    }

    public List<WorkflowItemStatusDTO> getWorkflowStatus(UUID workflowId) {
        return buildClient()
                .path("workflow")
                .path(workflowId.toString())
                .path("status")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildGet().invoke(new GenericType<List<WorkflowItemStatusDTO>>() {
                });
    }

    public UUID createWorkflowForEmployee(EmployeeDTO employee, WorkflowDTO workflowDTO) {
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
