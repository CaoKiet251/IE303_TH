import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import model.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductApp extends Application {
    private List<Product> products;
    private VBox productDetailsPane;
    private ImageView mainImageView;
    private Label productNameLabel;
    private Label productPriceLabel;
    private Label productDescriptionLabel;
    private Label productBrandLabel;

    @Override
    public void start(Stage primaryStage) {
        initializeProducts();
        
        // Main layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.getStyleClass().add("root");

        // Create split pane
        SplitPane splitPane = new SplitPane();
        
        // Left side - Product details
        productDetailsPane = createProductDetailsPane();
        
        // Right side - Product grid
        GridPane productGrid = createProductGrid();
        ScrollPane scrollPane = new ScrollPane(productGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("scroll-pane");
        
        // Add both sides to split pane
        splitPane.getItems().addAll(productDetailsPane, scrollPane);
        splitPane.setDividerPositions(0.4); // 40% left, 60% right
        
        root.setCenter(splitPane);

        // Show first product by default
        if (!products.isEmpty()) {
            showProduct(products.get(0));
        }

        // Apply styles
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        primaryStage.setTitle("Adidas Shop");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add selection to first product card after scene is shown
        if (!products.isEmpty()) {
            Platform.runLater(() -> {
                root.lookupAll(".product-card").stream()
                    .findFirst()
                    .ifPresent(node -> node.getStyleClass().add("product-card-selected"));
            });
        }
    }

    private VBox createProductDetailsPane() {
        VBox detailsPane = new VBox(10);
        detailsPane.setPadding(new Insets(10));
        detailsPane.setAlignment(Pos.TOP_LEFT);  // Align everything to the left
        detailsPane.getStyleClass().add("product-details");
        
        // Main product image
        mainImageView = new ImageView();
        mainImageView.setFitWidth(220);
        mainImageView.setFitHeight(220);
        mainImageView.setPreserveRatio(true);

        // Create container for centering the image
        HBox imageContainer = new HBox(mainImageView);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setPrefWidth(250); // Match separator width

        // Add separator
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color:rgb(82, 82, 82); -fx-max-width: 250px; -fx-min-width: 250px; -fx-border-width: 0 0 0 0; -fx-height: 1px;");
        
        // Product info labels
        productBrandLabel = new Label();
        productBrandLabel.getStyleClass().add("detail-brand-label");
        
        productNameLabel = new Label();
        productNameLabel.getStyleClass().add("detail-product-name");
        productNameLabel.setWrapText(true);

        productPriceLabel = new Label();
        productPriceLabel.getStyleClass().add("detail-product-price");

        productDescriptionLabel = new Label();
        productDescriptionLabel.getStyleClass().add("detail-product-description");
        productDescriptionLabel.setWrapText(true);
        
        detailsPane.getChildren().addAll(
            imageContainer,
            separator,
            productNameLabel,
            productPriceLabel,
            productBrandLabel,
            productDescriptionLabel
        );
        
        return detailsPane;
    }

    private void initializeProducts() {
        products = new ArrayList<>();
        products.add(new Product("4DFWD PULSE SHOES", 160.00, "This product is excluded from all promotional discounts and offers.", "/images/img1.png", "Adidas"));
        products.add(new Product("FORUM MID SHOES", 100.00, "This product is excluded from all promotional discounts and offers.", "/images/img2.png", "Adidas"));
        products.add(new Product("SUPERNOVA SHOES", 150.00, "NMD City Stock 2", "/images/img3.png", "Adidas"));
        products.add(new Product("NMD CITY STOCK 2", 160.00, "NMD City Stock 2", "/images/img4.png", "Adidas"));
        products.add(new Product("Adidas", 120.00, "NMD City Stock 2", "/images/img5.png", "Adidas"));
        products.add(new Product("4DFWD PULSE SHOES", 160.00, "This product is excluded from all promotional discounts and offers.", "/images/img6.png", "Adidas"));
        products.add(new Product("4DFWD PULSE SHOES", 160.00, "This product is excluded from all promotional discounts and offers.", "/images/img1.png", "Adidas"));
        products.add(new Product("FORUM MID SHOES", 100.00, "This product is excluded from all promotional discounts and offers.", "/images/img2.png", "Adidas"));
    }

    private GridPane createProductGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        int col = 0;
        int row = 0;
        
        for (Product product : products) {
            VBox productCard = createProductCard(product);
            grid.add(productCard, col, row);
            
            col++;
            if (col == 4) { // 4 items per row
                col = 0;
                row++;
            }
        }
        
        return grid;
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(10);
        card.getStyleClass().add("product-card");
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.TOP_LEFT);

        // Name
        Label nameLabel = new Label(product.getName());
        nameLabel.getStyleClass().add("product-name");
        nameLabel.setWrapText(false);
        nameLabel.setTextOverrun(javafx.scene.control.OverrunStyle.ELLIPSIS);
        nameLabel.setMaxWidth(160);
        
        // Description
        Label descLabel = new Label(product.getDescription());
        descLabel.getStyleClass().add("product-description");
        descLabel.setWrapText(false);
        descLabel.setTextOverrun(javafx.scene.control.OverrunStyle.ELLIPSIS);
        descLabel.setMaxWidth(160);
        
        // Image
        ImageView imageView = new ImageView();
        try {
            Image img = new Image(getClass().getResource(product.getImageUrl()).toExternalForm());
            imageView.setImage(img);
            // Crop image to landscape format (16:9 ratio)
            double width = img.getWidth();
            double height = img.getHeight();
            double cropHeight = height * 0.8; // Take 80% of height for taller view
            double y = (height - cropHeight) / 2; // Center the crop vertically
            imageView.setViewport(new javafx.geometry.Rectangle2D(0, y, width, cropHeight));
        } catch (Exception e) {
            imageView.setImage(createDefaultImage());
        }
        
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(true);

        // Center the image horizontally
        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.CENTER);

        // Brand and Price HBox
        HBox brandPriceBox = new HBox();
        brandPriceBox.setAlignment(Pos.CENTER);
        brandPriceBox.setSpacing(10);

        Label brandLabel = new Label(product.getBrand());
        brandLabel.getStyleClass().add("brand-label");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label priceLabel = new Label(String.format("$%.2f", product.getPrice()));
        priceLabel.getStyleClass().add("product-price");
        
        brandPriceBox.getChildren().addAll(brandLabel, spacer, priceLabel);
        
        card.getChildren().addAll(nameLabel, descLabel, imageContainer, brandPriceBox);
        
        // Click handler
        card.setOnMouseClicked(e -> {
            // Remove selection from all cards
            card.getScene().getRoot().lookupAll(".product-card").forEach(node -> 
                node.getStyleClass().remove("product-card-selected")
            );
            // Add selection to clicked card
            card.getStyleClass().add("product-card-selected");
            showProductWithAnimation(product);
        });
        
        return card;
    }

    private Image createDefaultImage() {
        javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(80, 80);
        javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(javafx.scene.paint.Color.LIGHTGRAY);
        gc.fillRect(0, 0, 80, 80);
        gc.setStroke(javafx.scene.paint.Color.GRAY);
        gc.strokeRect(0, 0, 80, 80);
        
        javafx.scene.image.WritableImage image = new javafx.scene.image.WritableImage(80, 80);
        canvas.snapshot(null, image);
        return image;
    }

    private void showProductWithAnimation(Product product) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), productDetailsPane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        
        fadeOut.setOnFinished(e -> {
            showProduct(product);
            
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), productDetailsPane);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        
        fadeOut.play();
    }

    private void showProduct(Product product) {
        try {
            Image img = new Image(getClass().getResource(product.getImageUrl()).toExternalForm());
            mainImageView.setImage(img);
            // Crop image to landscape format
            double width = img.getWidth();
            double height = img.getHeight();
            double cropHeight = height * 0.8; // Take 80% of height for taller view
            double y = (height - cropHeight) / 2; // Center the crop vertically
            mainImageView.setViewport(new javafx.geometry.Rectangle2D(0, y, width, cropHeight));
        } catch (Exception e) {
            mainImageView.setImage(createDefaultImage());
        }
        
        productBrandLabel.setText(product.getBrand());
        productNameLabel.setText(product.getName());
        productPriceLabel.setText(String.format("$%.2f", product.getPrice()));
        productDescriptionLabel.setText(product.getDescription());
    }

    public static void main(String[] args) {
        launch(args);
    }
} 