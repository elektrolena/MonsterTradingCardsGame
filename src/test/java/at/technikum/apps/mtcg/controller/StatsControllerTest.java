package at.technikum.apps.mtcg.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class StatsControllerTest {
    @Test
    public void shouldSupportRoute_WhenValidStatsRoute() {
        // Arrange
        StatsController statsController = new StatsController();
        String firstRoute = "/stats";
        String secondRoute = "/scoreboard";

        boolean doesSupportFirstRoute = false;
        boolean doesSupportSecondRoute = false;

        // Act
        doesSupportFirstRoute = statsController.supports(firstRoute);
        doesSupportSecondRoute = statsController.supports(secondRoute);

        // Assert
        assertTrue(doesSupportFirstRoute);
        assertTrue(doesSupportSecondRoute);
    }

    @Test
    public void shouldNotSupportRoute_WhenInvalidStatsRoute() {
        // Arrange
        StatsController statsController = new StatsController();
        String route = "/scores";
        boolean doesSupport = false;

        // Act
        doesSupport = statsController.supports(route);

        // Assert
        assertFalse(doesSupport);
    }

    @Test
    public void shouldSupportRequestMethod_WhenValidStatsMethod() {
        // Arrange
        StatsController statsController = spy(new StatsController());
        Request getStatsRequest = mock(Request.class);
        Request getScoreBoardRequest = mock(Request.class);
        Response response = mock(Response.class);

        when(getStatsRequest.getRoute()).thenReturn("/stats");
        when(getStatsRequest.getMethod()).thenReturn("GET");

        when(getScoreBoardRequest.getRoute()).thenReturn("/scoreboard");
        when(getScoreBoardRequest.getMethod()).thenReturn("GET");

        doReturn(response).when(statsController).getUserStats(getStatsRequest);
        doReturn(response).when(statsController).getScoreBoard(getScoreBoardRequest);

        // Act
        statsController.handle(getStatsRequest);
        statsController.handle(getScoreBoardRequest);

        // Assert
        verify(statsController, times(1)).getUserStats(getStatsRequest);
        verify(statsController, times(1)).getScoreBoard(getScoreBoardRequest);
    }

    @Test
    public void shouldNotSupportRequestMethod_WhenInValidStatsMethod() {
        // Arrange
        StatsController statsController = spy(new StatsController());
        Request getStatsRequest = mock(Request.class);
        Request getScoreBoardRequest = mock(Request.class);
        Response response = mock(Response.class);

        when(getStatsRequest.getRoute()).thenReturn("/stats");
        when(getStatsRequest.getMethod()).thenReturn("POST");

        when(getScoreBoardRequest.getRoute()).thenReturn("/scoreboard");
        when(getScoreBoardRequest.getRoute()).thenReturn("POST");

        doReturn(response).when(statsController).getUserStats(getStatsRequest);
        doReturn(response).when(statsController).getScoreBoard(getScoreBoardRequest);

        // Act
        statsController.handle(getStatsRequest);

        // Assert
        verify(statsController, times(0)).getUserStats(getStatsRequest);
        verify(statsController, times(0)).getScoreBoard(getScoreBoardRequest);
    }
}
