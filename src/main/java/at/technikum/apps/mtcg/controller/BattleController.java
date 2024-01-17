package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.BattleService;
import at.technikum.server.http.*;

public class BattleController extends Controller {
    private final BattleService battleService;

    public BattleController(JsonParser parser, BattleService battleService) {
        super(parser);
        this.battleService = battleService;
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/battles");
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals("POST")) {
            return battle(request);
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    Response battle(Request request) {
        String battleLog = this.battleService.createBattleLog(request.getAuthorizationToken());

        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.OK, battleLog);
    }
}