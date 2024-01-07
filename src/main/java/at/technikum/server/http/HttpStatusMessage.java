package at.technikum.server.http;

public enum HttpStatusMessage {
    OK_DECK("The deck has been successfully configured."),
    OK_TRADING_DELETE("Trading deal successfully deleted."),
    OK_TRADING_EXECUTE("Trading deal successfully executed."),
    CREATED_PACKAGE("Package and cards successfully created."),
    CREATED_TRADING("Trading deal successfully created."),
    NO_CONTENT_CARD("The request was fine, but the user doesn't have any cards."),
    NO_CONTENT_DECK("The request was fine, but the deck doesn't have any cards."),
    NO_CONTENT_TRADING("The request was fine, but there are no trading deals available."),
    BAD_REQUEST_DECK("The provided deck did not include the required amount of cards."),
    UNAUTHORIZED_SESSION("Invalid username/password provided."),
    FORBIDDEN_DECK("At least one of the provided cards does not belong to the user or is not available."),
    FORBIDDEN_PACKAGE("Provided user is not 'admin'."),
    FORBIDDEN_TRADING_DELETE("The deal contains a card that is not owned by the user."),
    FORBIDDEN_TRADING_EXECUTE("The offered card is not owned by the user, or owned by the same user as the card of the trading deal, or the requirements are not met (Type, MinimumDamage), or the offered card is locked in the deck."),
    FORBIDDEN_TRADING_OPEN("The deal contains a card that is not owned by the user or locked in the deck."),
    FORBIDDEN_TRANSACTION("Not enough money for buying a card package."),
    NOT_FOUND_TRADING("The provided deal ID was not found."),
    NOT_FOUND_TRANSACTION("No card package available for buying."),
    NOT_FOUND_USER("User not found in app!"),
    ALREADY_EXISTS_PACKAGE("At least one card in the package already exists."),
    ALREADY_EXISTS_TRADING("A deal with this deal ID already exists."),
    ALREADY_EXISTS_USER("User with same username already registered!");

    private final String statusMessage;

    HttpStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
