package thecat.tools.dao.fx.screens;

/**
 * Created by thecat on 09/10/16.
 */
public enum ScreenEnum {
    
    MAIN("main", "fxml/main.fxml"),
    DAO_OPTIONS("options", "fxml/options.fxml");

    private final String id;
    private final String fxml;

    ScreenEnum(String id, String fxml) {
        this.id = id;
        this.fxml = fxml;
    }

    public String getId() {
        return id;
    }

    public String getFxml() {
        return fxml;
    }

}

