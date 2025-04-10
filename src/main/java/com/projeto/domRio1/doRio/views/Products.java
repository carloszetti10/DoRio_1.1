package com.projeto.domRio1.doRio.views;


import com.projeto.domRio1.doRio.model.PosException;
import com.projeto.domRio1.doRio.model.entity.Category;
import com.projeto.domRio1.doRio.model.entity.Product;
import com.projeto.domRio1.doRio.model.service.CategoryService;
import com.projeto.domRio1.doRio.model.service.ProductService;
import com.projeto.domRio1.doRio.views.popups.ProductEdit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class Products extends AbstractController {

    @FXML
    private ComboBox<Category> category;
    @FXML
    private TextField name;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @FXML
    private void initialize() {
        category.getItems().clear();
        category.getItems().addAll(categoryService.findAll());

        MenuItem edit = new MenuItem("Edit Product");
        edit.setOnAction(event -> {
            Product product = tableView.getSelectionModel().getSelectedItem();
            if(null != product) {

            }
        });

        MenuItem changeState = new MenuItem("Change Status");
        changeState.setOnAction(event -> {
            Product product = tableView.getSelectionModel().getSelectedItem();


        });

        tableView.setContextMenu(new ContextMenu(edit, changeState));
    }

    @FXML
    private TableView<Product> tableView;

    @FXML
    private void search() {
        tableView.getItems().clear();
        List<Product> list = productService.search(category.getValue(), name.getText());
        tableView.getItems().addAll(list);
    }

    @FXML
    private void clear() {
        category.setValue(null);
        name.clear();
        tableView.getItems().clear();
    }

    @FXML
    private void upload() {

        try {
            // get category
            Category category = this.category.getValue();

            if(null == category) {
                throw new PosException("Please select target category for upload.");
            }

            // open file chooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Upload Product TSV File");
            Path desktop = Paths.get(System.getProperty("user.home")).resolve("Desktop");
            fileChooser.setInitialDirectory(desktop.toFile());
            fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Tab Separated File", "*.tsv"));

            File file = fileChooser.showOpenDialog(this.category.getScene().getWindow());

            if(null != file) {
                // upload file
                productService.upload(category, file);

                // refresh table (search)
                name.clear();
                search();
            }

        } catch (PosException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addNew() {;
    }

    private void save(Product product) {
        productService.save(product);
        category.setValue(product.getCategory());
        search();
    }
}
