package at.technikum.server.util;

import at.technikum.server.http.HttpMethod;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class HttpMapper {

    public static Request toRequestObject(String httpRequest) {
        Request request = new Request();

        request.setMethod(getHttpMethod(httpRequest));
        request.setRoute(getRoute(httpRequest));
        request.setHost(getHttpHeader("Host", httpRequest));
        request.setAuthorizationToken(getHttpHeader("Authorization", httpRequest));
        if(request.getAuthorizationToken() != null) {
            request.setAuthorizationToken(request.getAuthorizationToken().split(" ")[1]);
        }

        return parseContent(httpRequest, request);
    }

    private static Request parseContent(String httpRequest, Request request) {
        String contentLengthHeader = getHttpHeader("Content-Length", httpRequest);
        if (null == contentLengthHeader) {
            return request;
        }

        int contentLength = Integer.parseInt(contentLengthHeader);
        request.setContentLength(contentLength);

        if (0 == contentLength) {
            return request;
        }

        request.setBody(httpRequest.substring(httpRequest.length() - contentLength));

        return request;
    }

    public static String toResponseString(Response response) {

        return "HTTP/1.1 " + response.getStatusCode() + " " + response.getStatusMessage() + "\r\n" +
                "Content-Type: " + response.getContentType() + "\r\n" +
                "Content-Length: " + response.getBody().length() + "\r\n" +
                "\r\n" +
                response.getBody();
    }

    private static HttpMethod getHttpMethod(String httpRequest) {
        String httpMethod = httpRequest.split(" ")[0];

        return switch (httpMethod) {
            case "GET" -> HttpMethod.GET;
            case "POST" -> HttpMethod.POST;
            case "PUT" -> HttpMethod.PUT;
            case "DELETE" -> HttpMethod.DELETE;
            default -> throw new RuntimeException("No HTTP Method");
        };
    }

    private static String getRoute(String httpRequest) {
        return httpRequest.split(" ")[1];
    }

    private static String getHttpHeader(String header, String httpRequest) {
        Pattern regex = Pattern.compile("^" + header + ":\\s(.+)", Pattern.MULTILINE);
        Matcher matcher = regex.matcher(httpRequest);

        if (!matcher.find()) {
            return null;
        }

        return matcher.group(1);
    }
}
