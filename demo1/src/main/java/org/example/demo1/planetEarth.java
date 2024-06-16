package org.example.demo1;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;

public class planetEarth {
    private String name = "EARTH";
    private String imagePath = "file:src/main/resources/org/example/demo1/planetEarth.png";
    protected int size = 250;
    protected int x;
    protected int y;
    private ArrayList<LightPlane> currentPlanes = new ArrayList<>();
    protected Group group;

    public planetEarth(int x, int y, Group group) {
        this.x = x;
        this.y = y;
        this.group = group;
    }
    public planetEarth(Group group) {
        this(500, 200, group);
    }

    public int getSize() {
        return size;
    }

    public ArrayList<LightPlane> getCurrentPlanes() {
        return currentPlanes;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void draw() {
        Rectangle rec = new Rectangle();
        rec.setStyle("-fx-stroke: white; -fx-stroke-width: 2; -fx-stroke-dash-array: 10 15;");
        rec.setFill(Color.TRANSPARENT);
        rec.setHeight(this.size);
        rec.setWidth(this.size);
        rec.setLayoutX(this.x);
        rec.setLayoutY(this.y);
        group.getChildren().add(rec);

        Label currentPl = new Label(String.valueOf(this.currentPlanes.size()));
        currentPl.setTextFill(Color.WHITE);
        currentPl.setStyle("-fx-font-weight: bold");
        currentPl.setLayoutX(this.x + 235);
        currentPl.setLayoutY(this.y);
        group.getChildren().add(currentPl);

        Label objname = new Label(this.name);
        objname.setTextFill(Color.YELLOW);
        objname.setStyle("-fx-font-weight: bold");
        objname.setLayoutX(this.x + 5);
        objname.setLayoutY(this.y);
        group.getChildren().add(objname);

        ImageView image = new ImageView(imagePath);
        image.setFitHeight(this.size);
        image.setFitWidth(this.size);
        image.setX(this.x);
        image.setY(this.y);
        group.getChildren().add(image);
    }
    public void rerender() {
        this.group.getChildren().clear();
        draw();
    }
    public void planesList() {
        Stage planesList = new Stage();

        VBox box = new VBox();
        Group group = new Group();

        Label planeList = new Label("Current planes on planet " + this.name);
        planeList.setFont(Font.font("Times New Roman", 14));

        Label sortLabel = new Label("Choose the sorting criteria:");
        sortLabel.setFont(Font.font("Times New Roman", 14));

        ComboBox<String> comboBox = new ComboBox<>(
                FXCollections.observableArrayList("Name", "HP", "Speed")
        );
        comboBox.setValue("...");

        Text list = new Text();

        ArrayList<LightPlane> arr = new ArrayList<>();

        Button searchBtn = new Button("Sort!");
        searchBtn.setFont(Font.font("Times New Roman", 14));
        searchBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                StringBuilder line = new StringBuilder();
                arr.clear();
                arr.addAll(getCurrentPlanes());
                switch (comboBox.getValue()) {
                    case "Name" :
                        arr.sort(Comparator.comparing(LightPlane::getName));
                        break;
                    case "HP" :
                        arr.sort(Comparator.comparing(LightPlane::getHP));
                        break;
                    case "Speed" :
                        arr.sort(Comparator.comparing(LightPlane::getSpeed));
                        break;
                }
                line.setLength(0);
                for(LightPlane plane : arr) {
                    line.append(plane.toString()).append('\n');
                }
                list.setText("");
                list.setText(String.valueOf(line));
            }
        });

        box.setSpacing(10);
        box.getChildren().addAll(planeList, sortLabel, comboBox, searchBtn, list);

        box.setAlignment(Pos.CENTER);
        group.getChildren().add(box);

        Scene s = new Scene(group);

        planesList.setTitle(this.getName() + "'s planes");
        planesList.setHeight(600);
        planesList.setWidth(550);
        planesList.setScene(s);
        planesList.show();
    }
}
