package at.technikum.apps.mtcg.controller;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class DeckControllerTest {
    @Test
    public void shouldSupportRoute_WhenValidDeckRoute() {
        // Arrange
        DeckController deckController = new DeckController();
        String firstRoute = "/deck";
        String secondRoute = "/deck?format=plain";

        boolean doesSupportFirstRoute = false;
        boolean doesSupportSecondRoute = false;

        // Act
        doesSupportFirstRoute = deckController.supports(firstRoute);
        doesSupportSecondRoute = deckController.supports(secondRoute);

        // Assert
        assertTrue(doesSupportFirstRoute);
        assertTrue(doesSupportSecondRoute);
    }

    @Test
    public void shouldNotSupportRoute_WhenInvalidDeckRoute() {
        // Arrange
        DeckController deckController = new DeckController();
        String route = "/users";
        boolean doesSupport = false;

        // Act
        doesSupport = deckController.supports(route);

        // Assert
        assertFalse(doesSupport);
    }

    @Test
    public void shouldSupportRequestMethod_WhenValidDeckMethod() {
        // Arrange
        DeckController deckController = spy(new DeckController());
        Request getRequest = mock(Request.class);
        Request putRequest = mock(Request.class);
        Response response = mock(Response.class);

        when(getRequest.getMethod()).thenReturn("GET");
        when(putRequest.getMethod()).thenReturn("PUT");

        doReturn(response).when(deckController).getDeck(getRequest);
        doReturn(response).when(deckController).updateDeck(putRequest);

        // Act
        deckController.handle(getRequest);
        deckController.handle(putRequest);

        // Assert
        verify(deckController, times(1)).getDeck(getRequest);
        verify(deckController, times(1)).updateDeck(putRequest);
    }

    @Test
    public void shouldNotSupportRequestMethod_WhenInValidDeckMethod() {
        // Arrange
        DeckController deckController = spy(new DeckController());
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.getMethod()).thenReturn("POST");

        doReturn(response).when(deckController).getDeck(request);
        doReturn(response).when(deckController).updateDeck(request);

        // Act
        deckController.handle(request);

        // Assert
        verify(deckController, times(0)).getDeck(request);
        verify(deckController, times(0)).updateDeck(request);
    }
}
