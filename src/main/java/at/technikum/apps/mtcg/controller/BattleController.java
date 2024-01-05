package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.service.BattleService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.Optional;

public class BattleController extends Controller {

    private final UserService userService;
    private final BattleService battleService;

    public BattleController() {
        super();
        this.userService = new UserService(new DatabaseUserRepository());
        this.battleService = new BattleService();
    }
    @Override
    public boolean supports(String route) {
        return route.equals("/battles");
    }

    @Override
    public Response handle(Request request) {
        if(request.getMethod().equals("POST")) {
            return battle(request);
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    Response battle(Request request) {
        Optional<User> optionalUser = checkForAuthorizedRequest(request, userService);
        if(optionalUser.isEmpty()){
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED_ACCESS.getMessage());
        }
        User user = optionalUser.get();
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.OK, this.battleService.createBattleLog(user, userService));
    }
}
