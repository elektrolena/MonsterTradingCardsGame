package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.TradingDeal;
import at.technikum.apps.mtcg.exceptions.ExceptionMessage;
import at.technikum.apps.mtcg.parsing.JsonParser;
import at.technikum.apps.mtcg.service.TradingService;
import at.technikum.server.http.*;

import java.util.List;

public class TradingController extends Controller {
    private final TradingService tradingService;

    public TradingController(JsonParser parser, TradingService tradingService) {
        super(parser);
        this.tradingService = tradingService;
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/tradings") || route.matches("^/tradings/[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$");
    }

    @Override
    public Response handle(Request request) {if(request.getRoute().equals("/tradings")){
            if(request.getMethod().equals("GET")){
                return getOpenTradingDeals(request);
            } else if(request.getMethod().equals("POST")) {
                return openTradingDeal(request);
            }
        } else if(request.getRoute().matches("^/tradings/[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$")) {
            String tradingDealId = extractLastRoutePart(request.getRoute());
            if(request.getMethod().equals("DELETE")) {
                return deleteTradingDeal(request, tradingDealId);
            } else if(request.getMethod().equals("POST")) {
                return finishTradingDeal(request, tradingDealId);
            }
        }
        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getMessage());
    }

    Response getOpenTradingDeals(Request request) {
        List<TradingDeal> openTradingDeals = this.tradingService.getAllTradingDeals(request.getAuthorizationToken());

        return createResponse(HttpContentType.APPLICATION_JSON, HttpStatus.OK, this.parser.getTradingDeals(openTradingDeals));
    }

    Response openTradingDeal(Request request) {
        this.tradingService.openTradingDeal(request.getAuthorizationToken(), this.parser.getTradingDealFromBody(request));

        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.CREATED, ExceptionMessage.CREATED_TRADING.getStatusMessage());
    }

    Response deleteTradingDeal(Request request, String tradingDealId) {
        this.tradingService.deleteOpenTradingDeal(request.getAuthorizationToken(), tradingDealId);

        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.OK, ExceptionMessage.OK_TRADING_DELETE.getStatusMessage());

    }

    Response finishTradingDeal(Request request, String tradingDealId) {
        this.tradingService.finishTradingDeal(request.getAuthorizationToken(), this.parser.getCardFromBody(request), tradingDealId);

        return createResponse(HttpContentType.TEXT_PLAIN, HttpStatus.OK, ExceptionMessage.OK_TRADING_EXECUTE.getStatusMessage());
    }
}
