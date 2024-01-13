package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.HistoryService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class HistoryController extends Controller {

    private final HistoryService historyService;

    public HistoryController(JsonParser parser, HistoryService historyService) {
        super(parser);
        this.historyService = historyService;
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/history");
    }

    @Override
    public Response handle(Request request) {
        if(request.getMethod().equals("GET")) {
            return getBattleHistory(request);
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    Response getBattleHistory(Request request) {
        String responseBody = this.historyService.getBattleHistory(request);
        return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, responseBody);
    }
}
