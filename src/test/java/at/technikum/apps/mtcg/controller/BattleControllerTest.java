package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.BattleService;
import at.technikum.apps.mtcg.service.DeckService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class BattleControllerTest {
    @Test
    public void shouldSupportRoute_WhenValidBattleRoute() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        UserService userService = mock(UserService.class);
        DeckService deckService = mock(DeckService.class);
        BattleService battleService = mock(BattleService.class);
        BattleController battleController = new BattleController(parser, userService, battleService, deckService);
        String route = "/battle";
        boolean doesSupport = false;

        // Act
        doesSupport = battleController.supports(route);

        // Assert
        assertTrue(doesSupport);
    }

    @Test
    public void shouldNotSupportRoute_WhenInvalidBattleRoute() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        UserService userService = mock(UserService.class);
        DeckService deckService = mock(DeckService.class);
        BattleService battleService = mock(BattleService.class);
        BattleController battleController = new BattleController(parser, userService, battleService, deckService);
        String route = "/stats";
        boolean doesSupport = false;

        // Act
        doesSupport = battleController.supports(route);

        // Assert
        assertFalse(doesSupport);
    }

    @Test
    public void shouldSupportRequestMethod_WhenValidBattleMethod() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        UserService userService = mock(UserService.class);
        DeckService deckService = mock(DeckService.class);
        BattleService battleService = mock(BattleService.class);
        BattleController battleController = spy(new BattleController(parser, userService, battleService, deckService));
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.getMethod()).thenReturn("GET");

        doReturn(response).when(battleController).battle(request);

        // Act
        battleController.handle(request);

        // Assert
        verify(battleController, times(1)).battle(request);
    }

    @Test
    public void shouldNotSupportRequestMethod_WhenInValidCardMethod() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        UserService userService = mock(UserService.class);
        DeckService deckService = mock(DeckService.class);
        BattleService battleService = mock(BattleService.class);
        BattleController battleController = spy(new BattleController(parser, userService, battleService, deckService));
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.getMethod()).thenReturn("POST");

        doReturn(response).when(battleController).battle(request);

        // Act
        battleController.handle(request);

        // Assert
        verify(battleController, times(0)).battle(request);
    }
}
