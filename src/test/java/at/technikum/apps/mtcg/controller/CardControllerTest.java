package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.CardService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class CardControllerTest {
    @Test
    public void shouldSupportRoute_WhenValidCardRoute() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        UserService userService = mock(UserService.class);
        CardService cardService = mock(CardService.class);
        CardController cardController = new CardController(parser, userService, cardService);
        String route = "/cards";
        boolean doesSupport = false;

        // Act
        doesSupport = cardController.supports(route);

        // Assert
        assertTrue(doesSupport);
    }

    @Test
    public void shouldNotSupportRoute_WhenInvalidCardRoute() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        UserService userService = mock(UserService.class);
        CardService cardService = mock(CardService.class);
        CardController cardController = new CardController(parser, userService, cardService);
        String route = "/users";
        boolean doesSupport = false;

        // Act
        doesSupport = cardController.supports(route);

        // Assert
        assertFalse(doesSupport);
    }

    @Test
    public void shouldSupportRequestMethod_WhenValidCardMethod() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        UserService userService = mock(UserService.class);
        CardService cardService = mock(CardService.class);
        CardController cardController = spy(new CardController(parser, userService, cardService));
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.getMethod()).thenReturn("GET");

        doReturn(response).when(cardController).getAllCards(request);

        // Act
        cardController.handle(request);

        // Assert
        verify(cardController, times(1)).getAllCards(request);
    }

    @Test
    public void shouldNotSupportRequestMethod_WhenInValidCardMethod() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        UserService userService = mock(UserService.class);
        CardService cardService = mock(CardService.class);
        CardController cardController = spy(new CardController(parser, userService, cardService));
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.getMethod()).thenReturn("POST");

        doReturn(response).when(cardController).getAllCards(request);

        // Act
        cardController.handle(request);

        // Assert
        verify(cardController, times(0)).getAllCards(request);
    }
}
