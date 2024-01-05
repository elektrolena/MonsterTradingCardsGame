package at.technikum.apps.mtcg.controller;

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
        PackageController packageController = new PackageController();
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
        PackageController packageController = new PackageController();
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
        PackageController packageController = spy(new PackageController());
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
    public void shouldNotSupportRequestMethod_WhenInValidPackaeMethod() {
        // Arrange
        PackageController packageController = spy(new PackageController());
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
