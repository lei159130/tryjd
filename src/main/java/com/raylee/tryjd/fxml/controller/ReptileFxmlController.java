package com.raylee.tryjd.fxml.controller;

import com.raylee.tryjd.model.dto.Goods;
import com.raylee.tryjd.model.dto.MenuDto;
import com.raylee.tryjd.model.vo.QueryVo;
import com.raylee.tryjd.service.TryJDService;
import com.sun.xml.internal.bind.v2.runtime.property.PropertyFactory;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

@Slf4j
@FXMLController
public class ReptileFxmlController implements Initializable {

    @Value("${api.host}")
    private String API_HOST;

    @FXML
    private ChoiceBox choiceBox1;
    @FXML
    private ChoiceBox choiceBox2;
    @FXML
    private ChoiceBox choiceBox3;
    @FXML
    private TableView tableView;

    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colImg;
    @FXML
    private TableColumn colTitle;
    @FXML
    private TableColumn colTotal;
    @FXML
    private TableColumn colApply;
    @FXML
    private TableColumn colPrice;
    @FXML
    private TableColumn colStartTime;
    @FXML
    private TableColumn colEndTime;
    @FXML
    private TableColumn colOpe;

    @Autowired
    private TryJDService tryJDService;

    QueryVo vo = new QueryVo();
    ObservableList<MenuDto> cids, types, states;
    ObservableList<Goods> goods;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cids = FXCollections.observableArrayList(tryJDService.getCIds());
        types = FXCollections.observableArrayList(tryJDService.getTypes());
        states = FXCollections.observableArrayList(tryJDService.getStates());
        goods = FXCollections.observableArrayList(tryJDService.getGoods(vo));

        //映射表格列
        colId.setCellValueFactory(new PropertyValueFactory("activityId"));
        colImg.setCellValueFactory(new PropertyValueFactory("image"));
        colTitle.setCellValueFactory(new PropertyValueFactory("title"));
        colTotal.setCellValueFactory(new PropertyValueFactory("total"));
        colApply.setCellValueFactory(new PropertyValueFactory("apply"));
        colPrice.setCellValueFactory(new PropertyValueFactory("price"));
        colStartTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Goods, Date>, ObservableValue<Date>>() {
            private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            @Override
            public ObservableValue call(TableColumn.CellDataFeatures<Goods, Date> param) {
                SimpleStringProperty property = new SimpleStringProperty();
                property.setValue(sdf.format(param.getValue().getStartTime()));
                return property;
            }
        });
        colEndTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Goods, Date>, ObservableValue<Date>>() {
            private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            @Override
            public ObservableValue call(TableColumn.CellDataFeatures<Goods, Date> param) {
                SimpleStringProperty property = new SimpleStringProperty();
                property.setValue(sdf.format(param.getValue().getEndTime()));
                return property;
            }
        });

        choiceBox1.setItems(cids);
        choiceBox2.setItems(types);
        choiceBox3.setItems(states);
        tableView.setItems(goods);
        choiceBox1.getSelectionModel().

                select(0);
        choiceBox2.getSelectionModel().

                select(0);
        choiceBox3.getSelectionModel().

                select(0);

        StringConverter converter = new CustomStringConverter();

        choiceBox1.setConverter(converter);
        choiceBox2.setConverter(converter);
        choiceBox3.setConverter(converter);

        ChangeListener listener = new CustomChangeListener(vo);

        choiceBox1.getSelectionModel().

                selectedItemProperty().

                addListener(listener);
        choiceBox2.getSelectionModel().

                selectedItemProperty().

                addListener(listener);
        choiceBox3.getSelectionModel().

                selectedItemProperty().

                addListener(listener);
    }

    private class CustomStringConverter extends StringConverter<MenuDto> {

        @Override
        public String toString(MenuDto object) {
            return object.getName();
        }

        @Override
        public MenuDto fromString(String string) {
            return null;
        }
    }

    private class CustomChangeListener implements ChangeListener<MenuDto> {

        private QueryVo vo;

        public CustomChangeListener(QueryVo vo) {
            this.vo = vo;
        }

        @Override
        public void changed(ObservableValue<? extends MenuDto> observable, MenuDto oldValue, MenuDto newValue) {
            switch (newValue.getKey()) {
                case CIDS:
                    vo.setCids(newValue.getValue());
                    break;
                case ACTIVITY_TYPE:
                    vo.setType(newValue.getValue());
                    break;
                case ACTIVITY_STATE:
                    vo.setStatus(newValue.getValue());
                    break;
            }
            goods.removeAll();
            goods.addAll(tryJDService.getGoods(vo));
        }
    }
}
