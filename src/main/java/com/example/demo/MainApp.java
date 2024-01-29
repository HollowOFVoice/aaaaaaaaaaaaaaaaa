package com.example.demo;
import com.example.demo.controller.PersonEditDialogController;
import com.example.demo.controller.PersonOverviewController;
import com.example.demo.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    /**
     * Конструктор
     */
    static GsonBuilder builder = new GsonBuilder();
    static Gson gson = builder.create();

    public MainApp() throws IOException {
        FileReader fileReader = new FileReader("persons.txt");
        Scanner scanner = new Scanner(fileReader);
//читаем файл пока не достигнут его конец
        while (scanner.hasNext()){
            personData.add(fromJson(scanner.nextLine()));
        }
        fileReader.close();
       /* // В качестве образца добавляем некоторые данные
        personData.add(new Person("Владислав","Бас"));
        personData.add(new Person("Евгений","Березуев"));
        personData.add(new Person("Илья","Мамонов"));
        personData.add(new Person("Данил","Николаев"));
        personData.add(new Person("Игорь","Пименов"));
        personData.add(new Person("Анастасия","Реснянская"));
        personData.add(new Person("Владимир","Ростовцев"));
        personData.add(new Person("Артур","Сарян"));
        personData.add(new Person("Вадим","Федоров"));*/
    }

    private static Person fromJson(String str) {
        Person cat = gson.fromJson(str,Person.class);
        return cat;
    }
    /**
     * Возвращает данные в виде наблюдаемого списка адресатов.
     * @return
     */




    public ObservableList<Person> getPersonData() {
        return personData;
    }

    public void setPersonData(ObservableList<Person> personData) {
        this.personData = personData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");

        this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/address_book_icon.png")));

        initRootLayout();

        showPersonOverview();
    }

    /**
     * Инициализирует корневой макет.
     */
    public void initRootLayout() {
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Показывает в корневом макете сведения об адресатах.
     */

    /**
     * Открывает диалоговое окно для изменения деталей указанного адресата.
     * Если пользователь кликнул OK, то изменения сохраняются в предоставленном
     * объекте адресата и возвращается значение true.
     *
     * @param person - объект адресата, который надо изменить
     * @return true, если пользователь кликнул OK, в противном случае false.
     */
    public boolean showPersonEditDialog(Person person) {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаём адресата в контроллер.
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showPersonOverview() {
        try {
            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Помещаем сведения об адресатах в центр корневого макета.
            rootLayout.setCenter(personOverview);

            // Даём контроллеру доступ к главному приложению.
            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает главную сцену.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);



    }
    public void stop() throws java.lang.Exception {

        FileWriter fileWriter = new FileWriter("persons.txt");
        fileWriter.write(toJson(personData));
        fileWriter.close();

    }
    private static String toJson(List<Person> cats) {
        String resul ="";
        for (Person cat : cats) {
            System.out.println(gson.toJson(cat));
            resul+= gson.toJson(cat)+'\n';
        }
        return resul;
    }

}