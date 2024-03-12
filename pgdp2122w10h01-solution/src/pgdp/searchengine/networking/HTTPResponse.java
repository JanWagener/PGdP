package pgdp.searchengine.networking;

public class HTTPResponse {
    private final HTTPStatus status;
    private final String html;

    public HTTPResponse(String responseText) {
        int statusCode = Integer.parseInt(responseText.split(" ")[1]);
        this.status = HTTPStatus.getByCode(statusCode);

        if(this.status == HTTPStatus.OK) {
            String lowerCaseResponseText = responseText.toLowerCase();
            int startIndexOfHTML = lowerCaseResponseText.indexOf("<html");
            int endIndexOfHTML = lowerCaseResponseText.indexOf("</html") + 7;
            this.html = responseText.substring(startIndexOfHTML, endIndexOfHTML);
        } else {
            this.html = "";
        }
    }

    public HTTPStatus getStatus() {
        return status;
    }

    public String getHtml() {
        return html;
    }
}
