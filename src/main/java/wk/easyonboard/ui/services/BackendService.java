package wk.easyonboard.ui.services;

import com.owlike.genson.ext.jaxrs.GensonJsonConverter;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

/**
 * Created by david.leyendecker on 26.04.2017.
 */
public abstract class BackendService<DTO> {
    private static final URI SERVER = UriBuilder.fromUri("http://localhost").port(9080).build();

    protected WebTarget buildClient() {
        ClientConfig config = new ClientConfig(GensonJsonConverter.class);
        return ClientBuilder.newClient(config)
                .target(SERVER)
                .path("api");
    }

    public abstract List<DTO> getAll();

    public abstract int getCount();
}
