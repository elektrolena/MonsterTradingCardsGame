package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.TransactionService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.*;

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
        Package cardPackage = this.transactionService.buyPackage(request.getAuthorizationToken(), userService);

        return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getCards(cardPackage.getCards()));

    }
}
