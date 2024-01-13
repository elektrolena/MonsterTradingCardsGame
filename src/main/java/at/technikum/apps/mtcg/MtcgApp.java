package at.technikum.apps.mtcg;

import at.technikum.apps.mtcg.controller.*;
import at.technikum.apps.mtcg.exceptions.HttpStatusException;
import at.technikum.server.ServerApplication;
import at.technikum.server.http.*;

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
            } catch(HttpStatusException e) {
                Response response = new Response();
                response.setStatus(e.getHttpStatus());
                response.setContentType(e.getHttpContentType());
                response.setBody(e.getHttpStatusMessage().toString());

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
