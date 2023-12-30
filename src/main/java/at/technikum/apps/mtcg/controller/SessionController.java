package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.service.SessionService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class SessionController extends Controller {

    private final SessionService sessionService;


    public SessionController() {
        this.sessionService = new SessionService(new DatabaseUserRepository());
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/session");
    }

    @Override
    public Response handle(Request request) {
        String route = request.getRoute();

        if (route.equals("/session")) {
            if (request.getMethod().equals("POST")) {
                return start(request);
            }
        }

        return status(HttpStatus.BAD_REQUEST);
    }

    public Response start(Request request) {
        Response response = new Response();



        return response;
    }
}