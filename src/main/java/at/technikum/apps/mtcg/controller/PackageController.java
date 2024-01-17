package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.server.http.*;

public class PackageController extends Controller {

    private final PackageService packageService;
    public PackageController(JsonParser parser, PackageService packageService) {
        super(parser);
        this.packageService = packageService;
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
        this.packageService.save(request.getAuthorizationToken(), this.parser.getCardsFromBody(request));
        return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.CREATED, ExceptionMessage.CREATED_PACKAGE.getStatusMessage());
    }
}
