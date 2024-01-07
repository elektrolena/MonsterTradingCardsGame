package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.TransactionService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TransactionControllerTest {
    @Test
    public void shouldSupportRoute_WhenValidTransactionRoute() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        UserService userService = mock(UserService.class);
        TransactionService transactionService = mock(TransactionService.class);
        TransactionController transactionController = new TransactionController(parser, userService, transactionService);
        String route = "/transactions/packages";
        boolean doesSupport = false;

        // Act
        doesSupport = transactionController.supports(route);

        // Assert
        assertTrue(doesSupport);
    }

    @Test
    public void shouldNotSupportRoute_WhenInvalidTransactionRoute() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        UserService userService = mock(UserService.class);
        TransactionService transactionService = mock(TransactionService.class);
        TransactionController transactionController = new TransactionController(parser, userService, transactionService);
        String route = "/packages";
        boolean doesSupport = false;

        // Act
        doesSupport = transactionController.supports(route);

        // Assert
        assertFalse(doesSupport);
    }

    @Test
    public void shouldSupportRequestMethod_WhenValidTransactionMethod() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        UserService userService = mock(UserService.class);
        TransactionService transactionService = mock(TransactionService.class);
        TransactionController transactionController = spy(new TransactionController(parser, userService, transactionService));
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.getMethod()).thenReturn("POST");

        doReturn(response).when(transactionController).buyPackage(request);

        // Act
        transactionController.handle(request);

        // Assert
        verify(transactionController, times(1)).buyPackage(request);
    }

    @Test
    public void shouldNotSupportRequestMethod_WhenInValidTransactionMethod() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        UserService userService = mock(UserService.class);
        TransactionService transactionService = mock(TransactionService.class);
        TransactionController transactionController = spy(new TransactionController(parser, userService, transactionService));
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.getMethod()).thenReturn("GET");

        doReturn(response).when(transactionController).buyPackage(request);

        // Act
        transactionController.handle(request);

        // Assert
        verify(transactionController, times(0)).buyPackage(request);
    }
}
