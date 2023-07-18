package com.example.inventorymanagementsystem;
//David Osei Boateng
//10888204

import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.security.cert.Extension;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class dashboardController implements Initializable {

    @FXML
    private Button addProducts_addBtn;

    @FXML
    private TextField addProducts_brand;

    @FXML
    private Button addProducts_btn;

    @FXML
    private TableView<productData> addProducts_tableView;

    @FXML
    private TableColumn<productData, String> addProducts_col_productId;

    @FXML
    private TableColumn<productData, String> addProducts_col_type;

    @FXML
    private TableColumn<productData, String> addProducts_col_brand;

    @FXML
    private TableColumn<productData, String> addProducts_col_productName;

    @FXML
    private TableColumn<productData, String> addProducts_col_price;

    @FXML
    private TableColumn<productData, String> addProducts_col_status;



    @FXML
    private Button addProducts_deleteBtn;

    @FXML
    private AnchorPane addProducts_form;

    @FXML
    private ImageView addProducts_imageView;

    @FXML
    private Button addProducts_importButton;

    @FXML
    private TextField addProducts_price;

    @FXML
    private TextField addProducts_productId;

    @FXML
    private TextField addProducts_productName;

    @FXML
    private ComboBox<?> addProducts_productType;

    @FXML
    private Button addProducts_resetBtn;

    @FXML
    private TextField addProducts_search;

    @FXML
    private ComboBox<?> addProducts_status;

    @FXML
    private Button addProducts_updateBtn;

    @FXML
    private Button close;

    @FXML
    private Label home_availableProducts;

    @FXML
    private Button home_btn;

    @FXML
    private AnchorPane home_form;

    @FXML
    private AreaChart<?, ?> home_incomeChart;

    @FXML
    private Label home_numberOrder;

    @FXML
    private BarChart<?, ?> home_orderChart;

    @FXML
    private Label home_totalIncome;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private TextField orders_amount;

    @FXML
    private Label orders_balance;

    @FXML
    private ComboBox<?> orders_brand;

    @FXML
    private Button orders_addBtn;


    @FXML
    private Button orders_btn;

    @FXML
    private TableView<customerData> orders_tableView;

    @FXML
    private TableColumn<customerData, String> orders_col_type;

    @FXML
    private TableColumn<customerData, String> orders_col_brand;

    @FXML
    private TableColumn<customerData, String> orders_col_price;

    @FXML
    private TableColumn<customerData, String> orders_col_productName;

    @FXML
    private TableColumn<customerData, String> orders_col_quantity;



    @FXML
    private AnchorPane orders_form;

    @FXML
    private Button orders_payBtn;

    @FXML
    private ComboBox<?> orders_productName;

    @FXML
    private ComboBox<?> orders_productType;

    @FXML
    private Spinner<?> orders_quantity;

    @FXML
    private Button orders_receiptBtn;

    @FXML
    private Button orders_resetBtn;

    @FXML
    private Label orders_total;

    @FXML
    private Label username;

    //DATABASE TOOLS
    private Connection connect;
    private PreparedStatement prepare;

    private Statement statement;

    private ResultSet result;

    private Image image;

    public void addProductAdd(){
        String sql = "INSERT INTO product (product_id,type,brand,productName,price,status,image,date)" + "VALUES(?,?,?,?,?,?,?,?)";

        connect = database.connectDB();

        try{
            Alert alert;

            if (addProducts_productId.getText().isEmpty() || addProducts_productType.getSelectionModel().getSelectedItem() == null
                    || addProducts_brand.getText().isEmpty() || addProducts_productName.getText().isEmpty() || addProducts_price.getText().isEmpty()
                    || addProducts_status.getSelectionModel().getSelectedItem() == null || getData.path == ""){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Occured");
                alert.setHeaderText(null);
                alert.setContentText("Cannot take in blank values please fill the fields");
                alert.showAndWait();
            }else {

                String checkData = "SELECT product_id FROM product WHERE product_id = '"+
                        addProducts_productId.getText()+"'";

                statement = connect.createStatement();

                result = statement.executeQuery(checkData);

                if(result.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Occured");
                    alert.setHeaderText(null);
                    alert.setContentText("Product ID: "+addProducts_productId.getText()+" already exists.");
                    alert.showAndWait();
                }else{

                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, addProducts_productId.getText());
                    prepare.setString(2, (String) addProducts_productType.getSelectionModel().getSelectedItem());
                    prepare.setString(3, addProducts_brand.getText());
                    prepare.setString(4, addProducts_productName.getText());
                    prepare.setString(5, addProducts_price.getText());
                    prepare.setString(6, (String) addProducts_status.getSelectionModel().getSelectedItem());

                    String uri = getData.path;
                    if (uri != null) {
                        uri = uri.replace("\\", "\\\\");
                    }
                    prepare.setString(7, uri);

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(8, String.valueOf(sqlDate));
                    prepare.executeUpdate();
                    //Updating the table view
                    addProductsShowListData();
                    //Reset all fields
                    addProductsReset();
                }
            }

        }catch (Exception e){e.printStackTrace();}
    }

    public void addProductsUpdate(){
        String uri = getData.path;
        uri = uri.replace("\\","\\\\");

        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "UPDATE product SET type = '"
                +addProducts_productType.getSelectionModel().getSelectedItem()
                +"', brand = '"+addProducts_brand.getText()
                +"', productName = '"+addProducts_productName.getText()
                +"', price = '"+addProducts_price
                +"', status = '"+addProducts_status.getSelectionModel().getSelectedItem()
                +"', image = '"+uri
                +"', date = '"+sqlDate+"' WHERE product_id = '"
                +addProducts_productId.getText()+"'";
        connect = database.connectDB();
        try{
            Alert alert;
            if (addProducts_productId.getText().isEmpty() || addProducts_productType.getSelectionModel().getSelectedItem() == null
                    || addProducts_brand.getText().isEmpty() || addProducts_productName.getText().isEmpty() || addProducts_price.getText().isEmpty()
                    || addProducts_status.getSelectionModel().getSelectedItem() == null || getData.path == ""){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Occured");
                alert.setHeaderText(null);
                alert.setContentText("Cannot take in blank values please fill the fields");
                alert.showAndWait();
            }
            else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm");
                alert.setContentText("Do you wish to update Product ID: "+addProducts_productId.getText()+"?");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)){
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Info");
                    alert.setHeaderText(null);
                    alert.setContentText("Succesfully Updated");
                    alert.showAndWait();

                    addProductsShowListData();
                    addProductsReset();
                }
                }

            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    public void addProductsDelete(){
        String sql = "DELETE FROM product WHERE product_id = '"+addProducts_productId.getText()+"'";
        connect = database.connectDB();

        try{
            Alert alert;
            if (addProducts_productId.getText().isEmpty() || addProducts_productType.getSelectionModel().getSelectedItem() == null
                    || addProducts_brand.getText().isEmpty() || addProducts_productName.getText().isEmpty() || addProducts_price.getText().isEmpty()
                    || addProducts_status.getSelectionModel().getSelectedItem() == null || getData.path == ""){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Occured");
                alert.setHeaderText(null);
                alert.setContentText("Cannot delete please select an inventory");
                alert.showAndWait();
            }
            else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm");
                alert.setContentText("Do you wish to delete Product ID: "+addProducts_productId.getText()+"?");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)){
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Info");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted");
                    alert.showAndWait();

                    addProductsShowListData();
                    addProductsReset();
                }

            }


        }catch (Exception e){e.printStackTrace();}
    }

        //To reset all fields
    public void addProductsReset(){
        addProducts_productId.setText("");
        addProducts_productType.getSelectionModel().clearSelection();
        addProducts_brand.setText("");
        addProducts_productName.setText("");
        addProducts_price.setText("");
        addProducts_status.getSelectionModel().clearSelection();
        addProducts_imageView.setImage(null);

        getData.path = "";
    }

    //To import images from the database
    public void addProductsImportImage(){
        FileChooser open = new FileChooser();
        open.setTitle("Open Image");
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File", "*jpg","*png"));

        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if (file != null){
            getData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(),132,150,false,true);
            addProducts_imageView.setImage(image);
        }
    }

    public void addProductsSearch(){
        FilteredList<productData> filter = new FilteredList<>(addProductsList, e-> true);
        addProducts_search.textProperty().addListener((Observable, oldvalue, newValue) -> {
            filter.setPredicate(predicateProductData ->{
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String searchKey = newValue.toLowerCase();
                if (predicateProductData.getProductId().toString().contains(searchKey)){
                    return true;
                } else if (predicateProductData.getType().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateProductData.getBrand().toLowerCase().contains(searchKey)) {
                    return true;
                }else if (predicateProductData.getProductName().toLowerCase().contains(searchKey)) {
                    return true;
                }else if (predicateProductData.getPrice().toString().contains(searchKey)) {
                    return true;
                }else if (predicateProductData.getStatus().toLowerCase().contains(searchKey)) {
                    return true;
                }else return false;
            });
        });

        SortedList<productData> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(addProducts_tableView.comparatorProperty());
        addProducts_tableView.setItems(sortList);
    }

    public ObservableList<productData> addProductsListData(){
        ObservableList<productData> productList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product";
        connect = database.connectDB();

        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            productData prodD;

            while (result.next()){
                //Enter values from the constructor
                prodD = new productData(    result.getInt("product_id"),
                                            result.getString("type"),
                                            result.getString("brand"),
                                            result.getString("productName"),
                                            result.getDouble("price"),
                                            result.getString("status"),
                                            result.getString("image"),
                                            result.getDate("date"));
                productList.add(prodD);
            }
        }catch (Exception e){e.printStackTrace();}
        return productList;
    }

    private String[] listType = {"Beverages","Bread/Bakery","Canned/Jarred Goods","Dairy Products","Dry/Baking Goods",
                                 "Frozen Products","Meat","Farm Produce","Home Cleaners","Paper Goods","Home Care"};
    public void addProductsListType(){
        List<String> listT = new ArrayList<>();

        for(String data : listType){
            listT.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listT);
        addProducts_productType.setItems(listData);
    }
    private String[] listStatus = {"Availiable","Not Availiable"};
    public void addProductsListStatus(){
        List <String> listS = new ArrayList<>();

        for(String data: listStatus){
            listS.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listS);
        addProducts_status.setItems(listData);

    }


    //Attach values to the table
    private ObservableList<productData> addProductsList;
    public void addProductsShowListData(){
        addProductsList = addProductsListData();

        addProducts_col_productId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        addProducts_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        addProducts_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        addProducts_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        addProducts_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        addProducts_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        addProducts_tableView.setItems(addProductsList);


    }
    /////////////////

    public void addProductsSelect(){
        productData prodD = addProducts_tableView.getSelectionModel().getSelectedItem();
        int num = addProducts_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1){return;}
        addProducts_productId.setText(String.valueOf(prodD.getProductId()));
        addProducts_brand.setText(prodD.getBrand());
        addProducts_productName.setText(prodD.getProductName());
        addProducts_price.setText(String.valueOf(prodD.getPrice()));

        String uri = "file:" + prodD.getImage();
        image = new Image(uri,132,150,false,true);
        addProducts_imageView.setImage(image);
        getData.path = prodD.getImage();
    }

    private String[] orderlistType = {"Beverages","Bread/Bakery","Canned/Jarred Goods","Dairy Products","Dry/Baking Goods",
            "Frozen Products","Meat","Farm Produce","Home Cleaners","Paper Goods","Home Care"};
    public void ordersListType(){
        List<String> listT = new ArrayList<>();

        for(String data : orderlistType){
            listT.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listT);
        orders_productType.setItems(listData);
        ordersListBrand();

    }

    public void ordersListBrand(){

        String sql = "SELECT brand FROM product WHERE type = '"+orders_productType.getSelectionModel().getSelectedItem()
                +"'and status = 'Availiable'";

        connect = database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ObservableList listData = FXCollections.observableArrayList();
            while (result.next()){
                listData.add(result.getString("brand"));
            }
            orders_brand.setItems(listData);

            ordersListProductName();

        }catch (Exception e){e.printStackTrace();}

    }

    public void ordersListProductName(){
        String sql = "SELECT productName FROM product WHERE brand = '"+orders_brand.getSelectionModel().getSelectedItem()+"'";

        connect = database.connectDB();
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            ObservableList listData = FXCollections.observableArrayList();

            while (result.next()){
                listData.add(result.getString("productName"));
            }
            orders_productName.setItems(listData);

        }catch (Exception e){e.printStackTrace();}
    }
    public ObservableList<customerData> ordersListData(){
            customerId();
            ObservableList<customerData> listData = FXCollections.observableArrayList();
            String sql = "SELECT * FROM customer WHERE customer_id = '"+customerid+"'";

            connect = database.connectDB();
            try{
                customerData customerD;
                prepare = connect.prepareStatement(sql);
                result = prepare.executeQuery();

                while (result.next()){
                    customerD = new customerData(result.getInt("customer_id"),
                            result.getString("type"),
                            result.getString("brand"),
                            result.getString("productName"),
                            result.getInt("quantity"),
                            result.getDouble("price"),
                            result.getDate("date"));
                    listData.add(customerD);
                }

            }catch (Exception e){e.printStackTrace();}
            return listData;
}

    private ObservableList<customerData> ordersList;
    public void ordersShowListData(){
        ordersList = ordersListData();
        //To assign values to the table view headers
        orders_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        orders_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        orders_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        orders_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orders_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        orders_tableView.setItems(ordersList);
    }
    private int customerid;
    public void customerId(){
        String customId = "SELECT * FROM customer";
        connect = database.connectDB();

        try {
            prepare = connect.prepareStatement(customId);
            result = prepare.executeQuery();
            int checkId = 0;

            while (result.next()){
                //Get last customer id
                customerid = result.getInt("customer_id");
            }

            String checkData = "SELECT * FROM customer_receipt";
            statement = connect.createStatement();
            result = statement.executeQuery(checkData);
            while (result.next()){
                checkId = result.getInt("customer_id");
            }
            if (customerid == 0){
                customerid += 1;
            } else if (checkId == customerid) {
                customerid += 1;
            }
        }catch (Exception e){e.printStackTrace();}
    }
    public void switchForm(ActionEvent event){
        if (event.getSource() == home_btn){
            home_form.setVisible(true);
            addProducts_form.setVisible(false);
            orders_form.setVisible(false);

            home_btn.setStyle("-fx-background-color: #57b3ec;");
            addProducts_btn.setStyle("-fx-background-color: #ffffff;");
            orders_btn.setStyle("-fx-background-color: #ffffff;");

        }
        else if (event.getSource()  == addProducts_btn){
            home_form.setVisible(false);
            addProducts_form.setVisible(true);
            orders_form.setVisible(false);

            home_btn.setStyle("-fx-background-color: #ffffff;");
            addProducts_btn.setStyle("-fx-background-color: #57b3ec;");
            orders_btn.setStyle("-fx-background-color: #ffffff;");

            addProductsShowListData();
            addProductsListStatus();
            addProductsListType();
            addProductsSearch();
        }
        else if (event.getSource()  == orders_btn) {
            home_form.setVisible(false);
            addProducts_form.setVisible(false);
            orders_form.setVisible(true);

            home_btn.setStyle("-fx-background-color: #ffffff;");
            addProducts_btn.setStyle("-fx-background-color: #ffffff;");
            orders_btn.setStyle("-fx-background-color: #57b3ec;");

            ordersShowListData();
            ordersListType();
            ordersListBrand();
            ordersListProductName();
        }
    }

    public void logout(){
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText(null);
            alert.setContentText("Do you wish to logout?");
            Optional<ButtonType> optional =  alert.showAndWait();
            if (optional.get().equals(ButtonType.OK)){
                //Link to Go back to login form
                Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
            }else{
                return;
            }
        }catch (Exception error){
            error.printStackTrace();
        }
    }

    public void minimize(){
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }
    public void close(){
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        //This helps show the data on the tableview
            addProductsShowListData();
            addProductsListStatus();
            addProductsListType();
            ordersShowListData();
            ordersListType();
            ordersListBrand();
            ordersListProductName();
    }
}
