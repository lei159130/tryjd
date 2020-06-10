package com.raylee.tryjd.fxml.controller;

import com.raylee.tryjd.TryjdApplication;
import com.raylee.tryjd.fxml.view.ReptileFxmlView;
import com.raylee.tryjd.service.TryJDService;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sun.security.tools.keytool.Main;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
@FXMLController
public class CookiesFxmlController implements Initializable {

    @FXML
    private TextArea textArea;
    @FXML
    private Button button;

    @Autowired
    private TryJDService tryJDService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tryJDService.Init();
    }

    @FXML
    public void submit(ActionEvent actionEvent) {
        String cookies = textArea.getText();
        if (StringUtils.isEmpty(cookies)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.titleProperty().set("错误");
            alert.headerTextProperty().set("cookies不能为空!");
            alert.showAndWait();
            return;
        }
        tryJDService.setCookies(cookies);
        TryjdApplication.showView(ReptileFxmlView.class);
    }
}
