package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.SessionService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class SessionControllerTest {
    @Test
    public void shouldSupportRoute_WhenValidSessionRoute() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        SessionService sessionService = mock(SessionService.class);
        SessionController sessionController = new SessionController(parser, sessionService);
        String route = "/sessions";
        boolean doesSupport = false;

        // Act
        doesSupport = sessionController.supports(route);

        // Assert
        assertTrue(doesSupport);
    }

    @Test
    public void shouldNotSupportRoute_WhenInvalidSessionRoute() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        SessionService sessionService = mock(SessionService.class);
        SessionController sessionController = new SessionController(parser, sessionService);
        String route = "/users";
        boolean doesSupport = false;

        // Act
        doesSupport = sessionController.supports(route);

        // Assert
        assertFalse(doesSupport);
    }

    @Test
    public void shouldSupportRequestMethod_WhenValidSessionMethod() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        SessionService sessionService = mock(SessionService.class);
        SessionController sessionController = spy(new SessionController(parser, sessionService));
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.getMethod()).thenReturn("POST");

        doReturn(response).when(sessionController).start(request);

        // Act
        sessionController.handle(request);

        // Assert
        verify(sessionController, times(1)).start(request);
    }

    @Test
    public void shouldNotSupportRequestMethod_WhenInValidSessionMethod() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        SessionService sessionService = mock(SessionService.class);
        SessionController sessionController = spy(new SessionController(parser, sessionService));
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.getMethod()).thenReturn("GET");

        doReturn(response).when(sessionController).start(request);

        // Act
        sessionController.handle(request);

        // Assert
        verify(sessionController, times(0)).start(request);
    }
}
