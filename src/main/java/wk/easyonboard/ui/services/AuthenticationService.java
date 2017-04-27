package wk.easyonboard.ui.services;

import wk.easyonboard.common.datatransfer.EmployeeDTO;

import java.util.List;

/**
 * Created by david.leyendecker on 26.04.2017.
 */
public class AuthenticationService extends BackendService<EmployeeDTO> {
    private static AuthenticationService instance;
    private EmployeeDTO currentUser;

    public static synchronized AuthenticationService getInstance() {
        if(instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    public boolean login(String username, String password) {
        currentUser = new EmployeeDTO();
        currentUser.setUsername(username);
        currentUser.setFirstName("David");
        currentUser.setLastName("Leyendecker");
        currentUser.setUsername("davidleyendecker");
        currentUser.setAvatarUrl("avatar.jpg");
        return true;
    }

    public EmployeeDTO getCurrentUser() {
        return this.currentUser;
    }

    @Override
    public List<EmployeeDTO> getAll() {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
