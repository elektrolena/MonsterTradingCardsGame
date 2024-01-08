package at.technikum.apps.mtcg;

import at.technikum.apps.mtcg.controller.*;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.server.ServerApplication;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MtcgApp implements ServerApplication {

    private List<Controller> controllers = new ArrayList<>();

    public MtcgApp() {
        Injector injector = new Injector();

        this.controllers = injector.createController();
    }

    @Override
    public Response handle(Request request) {

        for (Controller controller: controllers) {
            if (!controller.supports(request.getRoute())) {
                continue;
            }

            try {
                return controller.handle(request);
            } catch(SQLException e) {
                Response response = new Response();
                response.setStatus(HttpStatus.INTERNAL_ERROR);
                response.setContentType(HttpContentType.TEXT_PLAIN);
                response.setBody("Internal Server Error. Please try again later.");

                return response;
            }
        }

        Response response = new Response();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody("Route " + request.getRoute() + " not found in app!");

        return response;
    }
}
