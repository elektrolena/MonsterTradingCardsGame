package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.HistoryService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class HistoryControllerTest {
    @Test
    public void shouldSupportRoute_WhenValidHistoryRoute() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        HistoryService historyService = mock(HistoryService.class);
        HistoryController historyController = new HistoryController(parser, historyService);
        String route = "/history";

        boolean doesSupport = false;

        // Act
        doesSupport = historyController.supports(route);

        // Assert
        assertTrue(doesSupport);
    }

    @Test
    public void shouldNotSupportRoute_WhenInvalidHistoryRoute() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        HistoryService historyService = mock(HistoryService.class);
        HistoryController historyController = new HistoryController(parser, historyService);
        String route = "/battles";
        boolean doesSupport = false;

        // Act
        doesSupport = historyController.supports(route);

        // Assert
        assertFalse(doesSupport);
    }

    @Test
    public void shouldSupportRequestMethod_WhenValidHistoryMethod() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        HistoryService historyService = mock(HistoryService.class);
        HistoryController historyController = spy(new HistoryController(parser, historyService));
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.getMethod()).thenReturn("GET");

        doReturn(response).when(historyController).getBattleHistory(request);

        // Act
        historyController.handle(request);

        // Assert
        verify(historyController, times(1)).getBattleHistory(request);
    }

    @Test
    public void shouldNotSupportRequestMethod_WhenInValidStatsMethod() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        HistoryService historyService = mock(HistoryService.class);
        HistoryController historyController = spy(new HistoryController(parser, historyService));
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.getMethod()).thenReturn("POST");

        doReturn(response).when(historyController).getBattleHistory(request);

        // Act
        historyController.handle(request);

        // Assert
        verify(historyController, times(0)).getBattleHistory(request);
    }
}
