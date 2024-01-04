package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.apps.mtcg.repository.DatabasePackageRepository;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.service.TransactionService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.List;
import java.util.Optional;

public class TransactionController extends Controller {

    private final UserService userService;
    private final TransactionService transactionService;

    public TransactionController() {
        super();
        this.userService = new UserService(new DatabaseUserRepository());
        this.transactionService = new TransactionService(new DatabaseCardRepository(), new DatabasePackageRepository());
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

    private Response buyPackage(Request request) {
        Optional <User> optionalUser = checkForAuthorizedRequest(request, userService);
        if(optionalUser.isEmpty()){
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED_ACCESS.getMessage());
        }

        User user = optionalUser.get();
        if(user.getCoins() < 5) {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.FORBIDDEN, "Not enough money for buying a card package.");
        }
        Optional<Package> cardPackage = this.transactionService.buyPackage(user, userService);
        if(cardPackage.isEmpty()) {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.NOT_FOUND, "No card package available for buying.");
        } else {
            return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getCards(cardPackage.get().getCards()));
        }
    }
}
