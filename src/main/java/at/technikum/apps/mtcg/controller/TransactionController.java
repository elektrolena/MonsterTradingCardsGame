package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.TransactionService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.*;

import java.util.Optional;

public class TransactionController extends Controller {

    private final UserService userService;
    private final TransactionService transactionService;

    public TransactionController(JsonParser parser, UserService userService, TransactionService transactionService) {
        super(parser);
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/transactions/packages");
    }

    @Override
    public Response handle(Request request) {
        if(request.getMethod().equals("POST")) {
            return buyPackage(request);
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    Response buyPackage(Request request) {
        Optional <User> optionalUser = checkForAuthorizedRequest(request, userService);
        if(optionalUser.isEmpty()){
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED_ACCESS.getMessage());
        }

        User user = optionalUser.get();
        if(user.getCoins() < 5) {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.FORBIDDEN, ExceptionMessage.FORBIDDEN_TRANSACTION.getStatusMessage());
        }
        Optional<Package> cardPackage = this.transactionService.buyPackage(user, userService);
        if(cardPackage.isEmpty()) {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.NOT_FOUND, ExceptionMessage.NOT_FOUND_TRANSACTION.getStatusMessage());
        } else {
            return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getCards(cardPackage.get().getCards()));
        }
    }
}
