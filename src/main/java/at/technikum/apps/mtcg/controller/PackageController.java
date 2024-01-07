package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.apps.mtcg.repository.DatabasePackageRepository;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.*;

import java.util.Objects;
import java.util.Optional;

public class PackageController extends Controller {

    private final UserService userService;
    private final PackageService packageService;
    public PackageController() {
        super();
        this.userService = new UserService(new DatabaseUserRepository());
        this.packageService = new PackageService(new DatabaseCardRepository(), new DatabasePackageRepository());
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/packages");
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals("POST")) {
            return createPackage(request);
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    Response createPackage(Request request) {
        Optional <User> optionalUser = checkForAuthorizedRequest(request, this.userService);
        if(optionalUser.isEmpty()){
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED_ACCESS.getMessage());
        }

        User admin = optionalUser.get();

        if(!Objects.equals(admin.getUsername(), "admin")) {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.FORBIDDEN, HttpStatusMessage.FORBIDDEN_PACKAGE.getStatusMessage());
        }
        if(Objects.equals(admin.getToken(), "admin-mtcgToken")) {
            if(this.packageService.save(this.parser.getCardsFromBody(request))) {
                return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.CREATED, HttpStatusMessage.CREATED_PACKAGE.getStatusMessage());
            } else {
                return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.ALREADY_EXISTS, HttpStatusMessage.ALREADY_EXISTS_PACKAGE.getStatusMessage());
            }
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }
}
