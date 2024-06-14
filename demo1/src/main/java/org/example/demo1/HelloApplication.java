package org.example.demo1;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.demo1.serialization.GameSerialization;
import org.example.demo1.serialization.PlaneSerialization;
import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.nio.file.attribute.GroupPrincipal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class HelloApplication extends Application {
    static Group mainGroup;
    private static ArrayList<LightPlane> planes;

    static {
        planes = new ArrayList<>();
        mainGroup = new Group();
    }
    private void addShip(LightPlane plane) {
        Group planeGroup = new Group();
        plane.setGroup(planeGroup);
        planes.add(plane);
        mainGroup.getChildren().add(planeGroup);
        plane.draw();
    }
    private final AnimationTimer mainTimer = new AnimationTimer() {
        private long update = 0;
        @Override
        public void handle(long l) {
            if(l - update >= 1_000_000_000) {
                for(LightPlane plane : planes) {
                    if(plane.getHP() < 100 && plane.isBelongs()) {
                        plane.setHP(plane.getHP() + 5);
                        plane.rerender();
                    }
                }
                update = l;
            }
        }
    };

    @Override
    public void start(Stage stage) throws Exception {

        Group groupEarth= new Group();
        planetEarth earth = new planetEarth(250, 420, groupEarth);
        mainGroup.getChildren().add(groupEarth);
        earth.draw();

        Group groupMars = new Group();
        planetMars mars = new planetMars(700, 350, groupMars);
        mainGroup.getChildren().add(groupMars);
        mars.draw();

        Group groupKepler = new Group();
        planetKepler kepler = new planetKepler(1000, 10, groupKepler);
        mainGroup.getChildren().add(groupKepler);
        kepler.draw();

        Scene scene = new Scene(mainGroup, 2560, 1440);
        Color background = Color.rgb(15, 34, 114);
        scene.setFill(background);

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                double mouseX = (mouseEvent.getSceneX() - mainGroup.getTranslateX());
                double mouseY = (mouseEvent.getSceneY() - mainGroup.getTranslateY());

                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    for(LightPlane plane : planes) {
                        if(mouseEvent.getX() >= plane.getX() && mouseEvent.getX() <= (plane.getX() + plane.size)
                                && mouseEvent.getY() >= plane.getY() && mouseEvent.getY() <= (plane.getY() + plane.size)) {
                            plane.setActive(!plane.isActive());
                            plane.rerender();
                        }
                    }
                }
                else if(mouseEvent.getButton() == MouseButton.SECONDARY) {
                    for(LightPlane plane : planes) {
                        if (mouseEvent.getX() >= plane.getX() && mouseEvent.getX() <= (plane.getX() + plane.size)
                                && mouseEvent.getY() >= plane.getY() && mouseEvent.getY() <= (plane.getY() + plane.size)) {
                            plane.changeStats(plane);
                            plane.rerender();
                        }
                    }
                    if(mouseEvent.getX() >= earth.getX() && mouseEvent.getX() <= (earth.getX() + earth.size)
                            && mouseEvent.getY() >= earth.getY() && mouseEvent.getY() <= (earth.getY() + earth.size)) {
                        earth.planesList();
                    }

                    if(mouseEvent.getX() >= mars.getX() && mouseEvent.getX() <= (mars.getX() + mars.size)
                            && mouseEvent.getY() >= mars.getY() && mouseEvent.getY() <= (mars.getY() + mars.size)) {
                        mars.planesList();
                    }

                    if(mouseEvent.getX() >= kepler.getX() && mouseEvent.getX() <= (kepler.getX() + kepler.size)
                            && mouseEvent.getY() >= kepler.getY() && mouseEvent.getY() <= (kepler.getY() + kepler.size)) {
                        kepler.planesList();
                    }
                }
            }
        });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch(keyEvent.getCode()) {
                    case ESCAPE:
                        for(LightPlane plane : planes) {
                            if(plane.isActive()) {
                                plane.setActive(false);
                                plane.rerender();
                            }
                        }
                        break;
                    case DELETE:
                        ArrayList<Integer> deleted = new ArrayList<>();
                        for (int i = 0; i < planes.size(); i++) {
                            LightPlane p = planes.get(i);
                            if (p.isActive()) {
                                mainGroup.getChildren().remove(p.getGroup());
                                p.getGroup().getChildren().clear();
                                deleted.add(i);
                            }
                        }
                        for (int i : deleted) {
                            planes.remove(i);
                        }
                        break;
                    case RIGHT:
                        for(LightPlane plane : planes) {
                            if(plane.isActive()) {
                                plane.setX((int) (plane.getX() + plane.getSpeed()/2));
                                plane.setReversed(false);
                                plane.checkIntersect(earth, mars, kepler);
                                plane.rerender();
                                earth.rerender();
                                mars.rerender();
                                kepler.rerender();
                            }
                        }
                        break;
                    case LEFT:
                        for(LightPlane plane : planes) {
                            if(plane.isActive()) {
                                plane.setX((int) (plane.getX() - plane.getSpeed()/2));
                                plane.setReversed(true);
                                plane.checkIntersect(earth, mars, kepler);
                                plane.rerender();
                                earth.rerender();
                                mars.rerender();
                                kepler.rerender();
                            }
                        }
                        break;
                    case UP:
                        for(LightPlane plane : planes) {
                            if(plane.isActive()) {
                                plane.setY((int) (plane.getY() - plane.getSpeed()/2));
                                plane.checkIntersect(earth, mars, kepler);
                                plane.rerender();
                                earth.rerender();
                                mars.rerender();
                                kepler.rerender();
                            }
                        }
                        break;
                    case DOWN:
                        for(LightPlane plane : planes) {
                            if(plane.isActive()) {
                                plane.setY((int) (plane.getY() + plane.getSpeed()/2));
                                plane.checkIntersect(earth, mars, kepler);
                                plane.rerender();
                                earth.rerender();
                                mars.rerender();
                                kepler.rerender();
                            }
                        }
                        break;
                    case NUMPAD6:
                        mainGroup.setTranslateX(mainGroup.getTranslateX() - 50);
                        break;
                    case NUMPAD4:
                        mainGroup.setTranslateX(mainGroup.getTranslateX() + 50);
                        break;
                    case NUMPAD8:
                        mainGroup.setTranslateY(mainGroup.getTranslateY() - 50);
                        break;
                    case NUMPAD2:
                        mainGroup.setTranslateY(mainGroup.getTranslateY() + 50);
                        break;
                    case INSERT:
                        addObjectWindow();
                        break;
                    case S:
                        planesNoBelong(planes);
                        break;
                    case F:
                        lookForSpecificPlane(planes);
                        break;
                    case H:
                        countPlanes(planes);
                    case C:
                        for(int i = 0; i < planes.size(); i++) {
                            if(planes.get(i).isActive()) {
                                try {
                                    addShip(planes.get(i).clone());
                                } catch (CloneNotSupportedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        break;
                    case W:
                        File file = new FileChooser().showSaveDialog(stage);
                        if(file != null) {
                            writeToFile(file);
                        }
                        break;
                    case R:
                        file = new FileChooser().showOpenDialog(stage);
                        if(file != null) {
                            planes.clear();
                            readFromFile(file);
                        }
                        break;
                }
            }
        });

        stage.setTitle("lab4");
        stage.setHeight(720);
        stage.setWidth(1280);
        stage.setScene(scene);
        stage.show();
        mainTimer.start();
    }

    public static void main(String[] args) {
        launch();
    }

    private void addObjectWindow() {
        Stage addBoatWindow = new Stage();

        VBox box = new VBox();
        Group group = new Group();
        Label objName = new Label("Enter ship's name");
        objName.setFont(Font.font("Times New Roman", 14));
        TextField nameField = new TextField();
        nameField.setFont(Font.font("Times New Roman", 14));

        Label objX = new Label("Enter ship's X coordinate: ");
        objX.setFont(Font.font("Times New Roman", 14));

        TextField layoutX = new TextField();
        layoutX.setFont(Font.font("Times New Roman", 14));

        Label objY = new Label("Enter ship's Y coordinate: ");
        objY.setFont(Font.font("Times New Roman", 14));

        TextField layoutY = new TextField();
        layoutY.setFont(Font.font("Times New Roman", 14));

        Button addBtn = new Button("OK");
        addBtn.setFont(Font.font("Times New Roman", 14));

        Label planeTypeLabel = new Label("Choose plane type");
        planeTypeLabel.setFont(Font.font("Times New Roman", 14));

        ComboBox<String> comboBox = new ComboBox<>(
                FXCollections.observableArrayList("Light Plane", "Fighter Plane", "Cruiser Plane")
        );
        comboBox.setValue("...");

        RadioButton objActive = new RadioButton();
        objActive.setText("Make Active");
        objActive.setFont(Font.font("Times New Roman", 18));

        box.setSpacing(10);
        box.getChildren().addAll(objName, nameField, objX, layoutX, objY, layoutY, planeTypeLabel, comboBox, objActive, addBtn);

        box.setAlignment(Pos.CENTER);

        group.getChildren().add(box);

        addBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Group planeGroup = new Group();
                LightPlane plane = switch (comboBox.getValue()) {
                    case "Light Plane" ->
                            new LightPlane(nameField.getText(), (int) Double.parseDouble(layoutX.getText()), (int) Double.parseDouble(layoutY.getText()));
                    case "Fighter Plane" ->
                            new FighterPlane(nameField.getText(), (int) Double.parseDouble(layoutX.getText()), (int) Double.parseDouble(layoutY.getText()));
                    default ->
                            new CruiserPlane(nameField.getText(), (int) Double.parseDouble(layoutX.getText()), (int) Double.parseDouble(layoutY.getText()));
                };
                plane.setGroup(planeGroup);
                plane.checkStats();
                if(objActive.isSelected()) {
                    plane.setActive(true);
                }
                planes.add(plane);
                mainGroup.getChildren().add(planeGroup);
                plane.draw();

                addBoatWindow.close();
             }
            });

        Scene s = new Scene(group);

        addBoatWindow.setTitle("New plane");
        addBoatWindow.setWidth(400);
        addBoatWindow.setHeight(350);
        addBoatWindow.setScene(s);
        addBoatWindow.show();
    }

    public void writeToFile(File f) {
        ArrayList<Serializable> children = new ArrayList<>();
        planes.stream().forEach(x -> children.add(new PlaneSerialization(x)));
        GameSerialization gs = new GameSerialization(children);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(gs);
            oos.flush();
            byte [] data = bos.toByteArray();
            FileOutputStream byteStream = new FileOutputStream(f);
            byteStream.write(data);
            byteStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readFromFile (File f) {

        GameSerialization gs = null;

        try {
            FileInputStream fileStream = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(fileStream);

            gs = (GameSerialization)in.readObject();

            in.close();
            fileStream.close();
        } catch(IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        assert gs != null;
        for(int i = 0; i < gs.getChildren().size(); i++) {
            addShip(((PlaneSerialization) gs.getChildren().get(i)).toPlane());
        }
    }
    public void planesNoBelong(ArrayList<LightPlane> planes) {
        ArrayList <LightPlane> noBelong = new ArrayList<>();
        for(LightPlane plane : planes) {
            if(!plane.isBelongs()) {
                noBelong.add(plane);
            }
        }
        String lineToPut = "";
        for(LightPlane plane : noBelong) {
            lineToPut += plane.toString() + '\n';
        }

        Stage noBelonging = new Stage();
        VBox box = new VBox();
        Group group = new Group();

        ComboBox<String> comboBox = new ComboBox<>(
                FXCollections.observableArrayList("Name", "HP", "Speed")
        );
        comboBox.setValue("...");

        Label title = new Label("Planes that are NOT on any planet: ");
        title.setFont(Font.font("Times New Roman", 18));

        Text planeList = new Text();

        Button searchBtn = new Button("Sort!");
        searchBtn.setFont(Font.font("Times New Roman", 14));

        box.setSpacing(10);
        box.getChildren().addAll(title, comboBox, searchBtn, planeList);

        box.setAlignment(Pos.CENTER);

        group.getChildren().add(box);

        searchBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String result = "";
                switch (comboBox.getValue()){
                    case "Name" :
                        noBelong.sort(Comparator.comparing(LightPlane::getName));
                        break;
                    case "HP" :
                        noBelong.sort(Comparator.comparing(LightPlane::getHP));
                    case "Speed" :
                        noBelong.sort(Comparator.comparing(LightPlane::getSpeed));
                }
                for(LightPlane plane : noBelong) {
                    result += plane.toString();
                }
                planeList.setText(result);
            }
        });

        Scene s = new Scene(group);

        noBelonging.setTitle("Planes that are not on any planet");
        noBelonging.setWidth(425);
        noBelonging.setHeight(350);
        noBelonging.setScene(s);
        noBelonging.show();
    }
    public void lookForSpecificPlane(ArrayList<LightPlane> planes) {
        Stage lookForSpecificPlane = new Stage();
        VBox box = new VBox();
        Group group = new Group();

        Label name = new Label("Enter plane's name: ");
        name.setFont(Font.font("Times New Roman", 16));
        TextField objName = new TextField();
        objName.setFont(Font.font("Times New Roman", 14));

        Label health = new Label("Enter plane's HP: ");
        health.setFont(Font.font("Times New Roman", 16));
        TextField objHP = new TextField();
        objHP.setFont(Font.font("Times New Roman", 14));

        Label speed = new Label("Enter plane's speed: ");
        speed.setFont(Font.font("Times New Roman", 16));
        TextField objSpd = new TextField();
        objSpd.setFont(Font.font("Times New Roman", 14));

        Label damage = new Label("Enter plane's damage: ");
        damage.setFont(Font.font("Times New Roman", 16));
        TextField objDmg = new TextField();
        objDmg.setFont(Font.font("Times New Roman", 14));

        Text field = new Text();
        field.setFont(Font.font("Times New Roman", 16));

        Button btn = new Button("Search!");
        btn.setFont(Font.font("Times New Roman", 14));

        box.setSpacing(10);
        box.getChildren().addAll(name, objName, health, objHP, speed, objSpd, damage, objDmg, btn, field);

        box.setAlignment(Pos.CENTER);
        group.getChildren().add(box);

        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String name = objName.getText();
                double health = Double.parseDouble(objHP.getText());
                double speed = Double.parseDouble(objSpd.getText());
                int damage = Integer.parseInt(objDmg.getText());

                String result = "";

                for (LightPlane plane : planes) {
                    if (plane.getName().equals(name)) {
                        if (plane.getHP() == health) {
                            if (plane.getSpeed() == speed) {
                                if (plane.getDamage() == damage) {
                                    String temp = plane.getClass().getSimpleName() + " " + plane.getName() + "; X: " +
                                            plane.getX() + ", Y: " + plane.getY() + "; ";
                                    String onPlanet = plane.isBelongs() ? "location: on planet " + plane.getCurrentPlanet() : "location: in outer space";
                                    temp += onPlanet;
                                    result = temp;
                                }
                            }
                        }
                    }
                }
                if (result.isBlank() && result instanceof String) {
                    field.setText("No planes found!");
                } else {
                    field.setText(result);
                }
            }
        });

        Scene s = new Scene(group);

        lookForSpecificPlane.setTitle("Look for specific object");
        lookForSpecificPlane.setWidth(500);
        lookForSpecificPlane.setHeight(500);
        lookForSpecificPlane.setScene(s);
        lookForSpecificPlane.show();
    }
    public void countPlanes(ArrayList<LightPlane> planes) {
        Stage countPlanes = new Stage();
        Group group = new Group();
        VBox box = new VBox();

        Label choiceLbl = new Label("Choose the criteria: ");
        choiceLbl.setFont(Font.font("Times New Roman", 14));

        Label result = new Label();
        result.setFont(Font.font("Times New Roman", 16));

        ComboBox<String> comboBox = new ComboBox<>(
                FXCollections.observableArrayList("Active", "Non-active", ">50% HP"));
        comboBox.setValue("...");

        Button btn = new Button("Count!");
        btn.setFont(Font.font("Times New Roman", 16));

        box.setSpacing(10);
        box.getChildren().addAll(choiceLbl, comboBox, btn, result);
        box.setAlignment(Pos.CENTER);
        group.getChildren().add(box);

        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int counter = 0;
                String line;
                switch (comboBox.getValue()) {
                    case "Active":
                        for (LightPlane plane : planes) {
                            if (plane.isActive()) counter += 1;
                        }
                        if(counter != 1) line = "There are " + counter + " planes matching selected criteria.";
                        else line = "There are " + counter + " plane matching selected criteria.";
                        result.setText(line);
                        break;
                    case "Non-active":
                        for (LightPlane plane : planes) {
                            if (!plane.isActive()) counter += 1;
                        }
                        if(counter != 1) line = "There are " + counter + " planes matching selected criteria.";
                        else line = "There are " + counter + " plane matching selected criteria.";
                        result.setText(line);
                        break;
                    case ">50% HP":
                        for (LightPlane plane : planes) {
                            if (plane.getHP() > 50.0) counter += 1;
                        }
                        if(counter != 1) line = "There are " + counter + " planes matching selected criteria.";
                        else line = "There are " + counter + " plane matching selected criteria.";
                        result.setText(line);
                        break;
                }
            }
        });
        Scene s = new Scene(group);

        countPlanes.setTitle("Count planes");
        countPlanes.setWidth(500);
        countPlanes.setHeight(500);
        countPlanes.setScene(s);
        countPlanes.show();
    }
}