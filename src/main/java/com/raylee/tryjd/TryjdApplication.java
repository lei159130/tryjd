package com.raylee.tryjd;

import com.raylee.tryjd.fxml.view.CookiesFxmlView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TryjdApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(TryjdApplication.class, CookiesFxmlView.class, args);
    }

}
