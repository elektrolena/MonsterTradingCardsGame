package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.SessionService;
import at.technikum.server.http.*;

public class SessionController extends Controller {

    private final SessionService sessionService;

    public SessionController(JsonParser parser, SessionService sessionService) {
        super(parser);
        this.sessionService = sessionService;
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/sessions");
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals("POST")) {
            return start(request);
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    Response start(Request request) {
        User user = this.parser.getUserFromBody(request);

        return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getUserCredentials(sessionService.login(user)));
    }
}