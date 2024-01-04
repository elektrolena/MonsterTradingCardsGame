package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseCardRepository;
import at.technikum.apps.mtcg.repository.DatabasePackageRepository;
import at.technikum.apps.mtcg.repository.DatabaseUserRepository;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.Objects;
import java.util.Optional;

public class PackageController extends Controller {

    private final UserService userService;
    private final PackageService packageService;
    public PackageController() {
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

    // TODO: move logic into service?
    private Response createPackage(Request request) {
        Optional <User> optionalUser = checkForAuthorizedRequest(request, userService);
        if(optionalUser.isEmpty()){
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED_ACCESS.getMessage());
        }

        User admin = optionalUser.get();

        if(!Objects.equals(admin.getUsername(), "admin")) {
            return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.FORBIDDEN, "Provided user is not 'admin'.");
        }
        if(Objects.equals(admin.getToken(), "admin-mtcgToken")) {
            if(packageService.save(getCardsFromBody(request))) {
                return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.CREATED, "Package and cards successfully created.");
            } else {
                return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.ALREADY_EXISTS, "At least one card in the package already exists.");
            }
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }
}
