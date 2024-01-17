package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.TradingService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TradingControllerTest {
    @Test
    public void shouldSupportRoute_WhenValidTradingRoute() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        TradingService tradingService = mock(TradingService.class);
        TradingController tradingController = new TradingController(parser, tradingService);
        String firstRoute = "/tradings";
        String secondRoute = "/tradings/943ac10b-58bc-4272-a567-0b0eb2c3d479";
        boolean doesSupportFirstRoute = false;
        boolean doesSupportSecondRoute = false;

        // Act
        doesSupportFirstRoute = tradingController.supports(firstRoute);
        doesSupportSecondRoute = tradingController.supports(secondRoute);

        // Assert
        assertTrue(doesSupportFirstRoute);
        assertTrue(doesSupportSecondRoute);
    }

    @Test
    public void shouldNotSupportRoute_WhenInvalidTradingRoute() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        TradingService tradingService = mock(TradingService.class);
        TradingController tradingController = new TradingController(parser, tradingService);
        String firstRoute = "/tradingDeal";
        String secondRoute = "/tradings/uuid";
        boolean doesSupportFirstRoute = false;
        boolean doesSupportSecondRoute = false;

        // Act
        doesSupportFirstRoute = tradingController.supports(firstRoute);
        doesSupportSecondRoute = tradingController.supports(secondRoute);

        // Assert
        assertFalse(doesSupportFirstRoute);
        assertFalse(doesSupportSecondRoute);
    }

    @Test
    public void shouldSupportRequestMethod_WhenValidTradingMethod() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        TradingService tradingService = mock(TradingService.class);
        TradingController tradingController = spy(new TradingController(parser, tradingService));
        Request getTradingsRequest = mock(Request.class);
        Request postTradingsRequest = mock(Request.class);
        Request deleteTradingDealRequest = mock(Request.class);
        Request postTradingDealRequest = mock(Request.class);
        Response response = mock(Response.class);
        String tradingDealId = "943ac10b-58bc-4272-a567-0b0eb2c3d479";

        when(getTradingsRequest.getRoute()).thenReturn("/tradings");
        when(getTradingsRequest.getMethod()).thenReturn("GET");

        when(postTradingsRequest.getRoute()).thenReturn("/tradings");
        when(postTradingsRequest.getMethod()).thenReturn("POST");

        when(deleteTradingDealRequest.getRoute()).thenReturn("/tradings/943ac10b-58bc-4272-a567-0b0eb2c3d479");
        when(deleteTradingDealRequest.getMethod()).thenReturn("DELETE");

        when(postTradingDealRequest.getRoute()).thenReturn("/tradings/943ac10b-58bc-4272-a567-0b0eb2c3d479");
        when(postTradingDealRequest.getMethod()).thenReturn("POST");

        doReturn(response).when(tradingController).getOpenTradingDeals(getTradingsRequest);
        doReturn(response).when(tradingController).openTradingDeal(postTradingsRequest);
        doReturn(response).when(tradingController).deleteTradingDeal(deleteTradingDealRequest, tradingDealId);
        doReturn(response).when(tradingController).finishTradingDeal(postTradingDealRequest, tradingDealId);

        // Act
        tradingController.handle(getTradingsRequest);
        tradingController.handle(postTradingsRequest);
        tradingController.handle(deleteTradingDealRequest);
        tradingController.handle(postTradingDealRequest);

        // Assert
        verify(tradingController, times(1)).getOpenTradingDeals(getTradingsRequest);
        verify(tradingController, times(1)).openTradingDeal(postTradingsRequest);
        verify(tradingController, times(1)).deleteTradingDeal(deleteTradingDealRequest, tradingDealId);
        verify(tradingController, times(1)).finishTradingDeal(postTradingDealRequest, tradingDealId);
    }

    @Test
    public void shouldNotSupportRequestMethod_WhenInValidDeckMethod() {
        // Arrange
        JsonParser parser = mock(JsonParser.class);
        TradingService tradingService = mock(TradingService.class);
        TradingController tradingController = spy(new TradingController(parser, tradingService));
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        String tradingDealId = "943ac10b-58bc-4272-a567-0b0eb2c3d479";

        when(request.getRoute()).thenReturn("/tradings");
        when(request.getMethod()).thenReturn("PUT");

        doReturn(response).when(tradingController).getOpenTradingDeals(request);
        doReturn(response).when(tradingController).openTradingDeal(request);
        doReturn(response).when(tradingController).deleteTradingDeal(request, tradingDealId);
        doReturn(response).when(tradingController).finishTradingDeal(request, tradingDealId);

        // Act
        tradingController.handle(request);

        // Assert
        verify(tradingController, times(0)).getOpenTradingDeals(request);
        verify(tradingController, times(0)).openTradingDeal(request);
        verify(tradingController, times(0)).deleteTradingDeal(request, tradingDealId);
        verify(tradingController, times(0)).finishTradingDeal(request, tradingDealId);
    }
}
