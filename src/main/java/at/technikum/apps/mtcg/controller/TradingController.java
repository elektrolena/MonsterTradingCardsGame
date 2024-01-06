package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.TradingDeal;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseTradingRepository;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.service.TradingService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.List;
import java.util.Optional;

public class TradingController extends Controller {

    private final UserService userService;
    private final TradingService tradingService;

    public TradingController() {
        this.userService = new UserService(new DatabaseUserRepository());
        this.tradingService = new TradingService(new DatabaseTradingRepository());
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/tradings") || route.equals("/tradings/\\w+");
    }

    @Override
    public Response handle(Request request) {
        Optional<User> optionalUser = checkForAuthorizedRequest(request, this.userService);
        if(optionalUser.isEmpty()){
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED_ACCESS.getMessage());
        }
        User user = optionalUser.get();

        if(request.getRoute().equals("/tradings")){
            if(request.getMethod().equals("GET")){
                return getOpenTradingDeals();
            } else if(request.getMethod().equals("POST")) {
                return createTradingDeal(request, user);
            }
        } else if(request.getRoute().equals("/tradings/\\w+")) {
            if(request.getMethod().equals("DELETE")) {
                return deleteTradingDeal(request, user);
            } else if(request.getMethod().equals("POST")) {
                return finishTradingDeal(request, user);
            }
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    private Response getOpenTradingDeals() {
        Optional<List<TradingDeal>> openTradingDeals = this.tradingService.getAllTradingDeals();
        if(openTradingDeals.isEmpty()) {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.NO_CONTENT, "The request was fine, but there are no trading deals available.");
        }
        return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getTradingDeals(openTradingDeals.get()));
    }

    private Response createTradingDeal(Request request, User user) {
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    private Response deleteTradingDeal(Request request, User user) {
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    private Response finishTradingDeal(Request request, User user) {
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }
}
