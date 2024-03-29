package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class PackageControllerTest {
    @Test
    public void shouldSupportRoute_WhenValidPackageRoute() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        PackageService packageService = mock(PackageService.class);
        PackageController packageController = new PackageController(parser, packageService);
        String route = "/packages";

        boolean doesSupport = false;

        // Act
        doesSupport = packageController.supports(route);

        // Assert
        assertTrue(doesSupport);
    }

    @Test
    public void shouldNotSupportRoute_WhenInvalidPackageRoute() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        PackageService packageService = mock(PackageService.class);
        PackageController packageController = new PackageController(parser, packageService);
        String route = "/users";
        boolean doesSupport = false;

        // Act
        doesSupport = packageController.supports(route);

        // Assert
        assertFalse(doesSupport);
    }

    @Test
    public void shouldSupportRequestMethod_WhenValidPackageMethod() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        PackageService packageService = mock(PackageService.class);
        PackageController packageController = spy(new PackageController(parser, packageService));
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.getMethod()).thenReturn("POST");

        doReturn(response).when(packageController).createPackage(request);

        // Act
        packageController.handle(request);

        // Assert
        verify(packageController, times(1)).createPackage(request);
    }

    @Test
    public void shouldNotSupportRequestMethod_WhenInValidPackageMethod() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        PackageService packageService = mock(PackageService.class);
        PackageController packageController = spy(new PackageController(parser, packageService));
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.getMethod()).thenReturn("GET");

        doReturn(response).when(packageController).createPackage(request);

        // Act
        packageController.handle(request);

        // Assert
        verify(packageController, times(0)).createPackage(request);
    }
}
