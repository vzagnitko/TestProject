package ua.test.services.export;

/**
 * Contains export mime type
 *
 * @author victorzagnitko
 */
public enum ExportMimeType {

    CSV("text/csv");

    private String mime;

    ExportMimeType(String mime) {
        this.mime = mime;
    }

    public String getMime() {
        return mime;
    }

}
