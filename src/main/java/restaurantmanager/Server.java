package restaurantmanager;

import connexion.ConnGen;
import restaurantmanager.table.CategoryTab;
import restaurantmanager.table.ProductTab;
import restaurantmanager.table.StockTab;

import static spark.Spark.*;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class Server {

    public void serverListen(int port) throws Exception {
        String imageFolder = "";

        try (InputStream input = ConnGen.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            prop.load(input);
            imageFolder = prop.getProperty("db.image_folder");
        }
        if (imageFolder.trim().isEmpty()) {
            throw new Exception("Veuillez spécifier le dossier de stockage des images");
        }

        System.out.println(imageFolder);

        File uploadDir = new File(imageFolder);
        if (uploadDir.exists() == false) {
            uploadDir.mkdir();
        }
        staticFiles.externalLocation(imageFolder);

        get("/api/statistics/:month/:year", (req, res) -> {
            JSONData jsonData = new JSONData();
            try {

                return jsonData.getBigStatistic(Integer.parseInt(req.params(":month")), Integer.parseInt(req.params(":year")));
            } catch (Exception ex) {
                return jsonData.getError(ex.getMessage());
            }
        });

        get("/api/stocks", (req, res) -> {
            JSONData jsonData = new JSONData();
            return jsonData.getAllStocks();
        });

        put("/api/stocks/:updatedStock", (req, res) -> {
            JSONData jsonData = new JSONData();
            ConnGen connGen = null;
            try {
                StockTab stockTab = jsonData.getStock(req.params(":updatedStock"));
                AppData appData = new AppData();
                connGen = new ConnGen();

                appData.updateStock(connGen, stockTab.getStockID(), stockTab.getInitialQuantity(), stockTab.getRealQuantityLeft());
                connGen.getConn().commit();
                connGen.close();
                return jsonData.getSucess("Stock mis �jour avec succ�s");
            } catch (Exception ex) {
                if (connGen != null) {
                    connGen.getConn().rollback();
                    connGen.close();
                }
                return jsonData.getError(ex.getMessage());
            }
        });

        get("/serverTest", (req, res) -> "HARD");

        get("/api/products/:categoryID", (req, res) -> {
            JSONData jsonData = new JSONData();
            return jsonData.getAllProductsJSON(req.params(":categoryID"));
        });

        get("/api/places", (req, res) -> {
            JSONData jsonData = new JSONData();
            return jsonData.getAllPlaces();
        });

        get("/api/products", (req, res) -> {
            JSONData jsonData = new JSONData();
            return jsonData.getAllProductsJSON("");
        });

        get("/api/categories", (req, res) -> {
            JSONData jsonData = new JSONData();
            return jsonData.getAllCategoriesJSON();
        });

        get("/api/payment/:allProducts", (req, res) -> {
            //{userID, customerID, products[], isPayment, amontReceived, totalAmount}
            JSONData jsonData = new JSONData();
            ConnGen connGen = null;
            try {
                Order order = new Order();
                connGen = new ConnGen();
                connGen.getConn().setAutoCommit(false);

                order.makeOrder(connGen, req.params((":allProducts")));

                connGen.getConn().commit();
                connGen.close();
                return jsonData.getSucess("Payement effectué avec succès");
            } catch (Exception ex) {
                ex.printStackTrace();
                if (connGen != null) {
                    connGen.getConn().rollback();
                    connGen.close();
                }

                return jsonData.getError(ex.getMessage());
            }

        });

        get("/api/printOnly/:order", (req, res) -> {
            //{userID, customerID, products[], isPayment, amontReceived, totalAmount}
            JSONData jsonData = new JSONData();
            ConnGen connGen = null;
            try {

                Order order = new Order();
                connGen = new ConnGen();
                connGen.getConn().setAutoCommit(false);

                order.payOrder(connGen, req.params((":order")), false);

                connGen.getConn().commit();
                connGen.close();
                return jsonData.getSucess("Payement effectué avec succès");
            } catch (Exception ex) {
                ex.printStackTrace();
                if (connGen != null) {
                    connGen.getConn().rollback();
                    connGen.close();
                }

                return jsonData.getError(ex.getMessage());
            }
        });

        post("/api/paymentOnly/:order", (req, res) -> {
            //{userID, customerID, products[], isPayment, amontReceived, totalAmount}
            JSONData jsonData = new JSONData();
            ConnGen connGen = null;
            try {
                Order order = new Order();
                connGen = new ConnGen();
                connGen.getConn().setAutoCommit(false);

                order.payOrder(connGen, req.params((":order")), true);

                connGen.getConn().commit();
                connGen.close();
                return jsonData.getSucess("Payement effectué avec succès");
            } catch (Exception ex) {
                ex.printStackTrace();
                if (connGen != null) {
                    connGen.getConn().rollback();
                    connGen.close();
                }

                return jsonData.getError(ex.getMessage());
            }
        });

        post("/api/orderOnly/:newOrder", (req, res) -> {
            JSONData jsonData = new JSONData();

            // CREATE ORDER ONLY
            ConnGen connGen = null;
            try {
                connGen = new ConnGen();
                connGen.getConn().setAutoCommit(false);
                Order order = new Order();
                order.makeOrder(connGen, req.params("newOrder"));

                connGen.getConn().commit();
                connGen.close();
                return jsonData.getSucess("Commande insérer avec succès");
            } catch (Exception ex) {
                if (connGen != null) {
                    connGen.getConn().rollback();
                    connGen.close();
                }
                ex.printStackTrace();
                return jsonData.getError(ex.getMessage());
            }
        });

        put("/api/orderOnly/:updatedOrder", (req, res) -> {

            JSONData jsonData = new JSONData();
            ConnGen connGen = null;
            try {
                connGen = new ConnGen();
                connGen.getConn().setAutoCommit(false);

                Order order = new Order();
                order.updateOrder(connGen, req.params(":updatedOrder"));
                connGen.getConn().commit();
                connGen.close();
                return jsonData.getSucess("Commande mis à jour avec succès");
            } catch (Exception ex) {
                if (connGen != null) {
                    connGen.getConn().rollback();
                    connGen.close();
                }
                ex.printStackTrace();
                return jsonData.getError(ex.getMessage());
            }
        });

        get("/api/unpaidorders/:placeID", (req, res) -> {
            JSONData jsonData = new JSONData();
            try {
                return jsonData.getAllUnpaidOrders(req.params(":placeID"));
            } catch (Exception ex) {
                return jsonData.getError(ex.getMessage());
            }
        });

        get("/api/unpaidorders", (req, res) -> {
            JSONData jsonData = new JSONData();
            try {
                return jsonData.getAllUnpaidOrders();
            } catch (Exception ex) {
                return jsonData.getError(ex.getMessage());
            }
        });

        post("/api/stocks/:newStock", (req, res) -> {
            JSONData jsonData = new JSONData();
            try {
                StockTab stockTab = jsonData.getStock(req.params(":newStock"));
                AppData appData = new AppData();
                appData.insertStock(stockTab);
                return jsonData.getSucess("Stock ins�rer avec succ�s");
            } catch (Exception ex) {
                return jsonData.getError(ex.getMessage());
            }

        });

        post("/api/products/:newProduct", (req, res) -> {
            JSONData jsonData = new JSONData();
            try {

                ProductTab productTab = jsonData.getProduct(req.params(":newProduct"));

                AppData appData = new AppData();
                appData.insertProduct(productTab);
                return jsonData.getSucess("Produit insérer avec succès");
            } catch (Exception ex) {
                return jsonData.getError(ex.getMessage());
            }

        });

        put("/api/products/:newProduct", (req, res) -> {
            JSONData jsonData = new JSONData();
            ConnGen connGen = null;
            try {
                ProductTab productTab = jsonData.getProduct(req.params(":newProduct"));
                AppData appData = new AppData();
                connGen = new ConnGen();
                appData.updateProduct(connGen, productTab);
                connGen.getConn().commit();
                connGen.close();
                return jsonData.getSucess("Produit mis à jour avec succès");
            } catch (Exception ex) {
                if (connGen != null) {
                    connGen.getConn().rollback();
                    connGen.close();
                }
                return jsonData.getError(ex.getMessage());
            }

        });

        delete("api/categories/:categoryID", (req, res) -> {
            ConnGen connGen = null;
            JSONData jsonData = new JSONData();
            try {
                connGen = new ConnGen();
                AppData appData = new AppData();
                appData.deleteCategory(connGen, req.params(":categoryID"));
                connGen.getConn().commit();
                connGen.close();
                return jsonData.getSucess("Catégorie supprimer avec succès");
            } catch (Exception ex) {
                return jsonData.getError(ex.getMessage());
            }
        });

        delete("/api/products/:productID", (req, res) -> {
            ConnGen connGen = null;
            JSONData jsonData = new JSONData();
            try {
                connGen = new ConnGen();
                AppData appData = new AppData();
                appData.deleteProduct(connGen, req.params(":productID"));
                connGen.getConn().commit();
                connGen.close();
                return jsonData.getSucess("Produit supprimer avec succès");
            } catch (Exception ex) {
                return jsonData.getError(ex.getMessage());
            }
        });

        post("/api/categories/:newCategory", (req, res) -> {
            JSONData jsonData = new JSONData();
            try {
                CategoryTab categoryTab = jsonData.getCategory(req.params(":newCategory"));

                AppData appData = new AppData();
                appData.insertCategory(categoryTab);
                return jsonData.getSucess("Catégorie insérer avec succès");
            } catch (Exception ex) {
                return jsonData.getError(ex.getMessage());
            }

        });

        post("/api/imageUpload/:imageName", (req, res) -> {
            String imageName = req.params(":imageName");

            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            JSONData jsonData = new JSONData();
            try {
                InputStream is = req.raw().getPart("product_file").getInputStream();

                File targetFile = new File(uploadDir.getAbsolutePath() + "/"
                        + new FileManipulation().getImageName(imageName)
                        + new FileManipulation().getExtension(imageName));
                Files.copy(is, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return jsonData.getSucess("Image uploader avec succès");
            } catch (Exception ex) {
                return jsonData.getError(ex.getMessage());
            }

        });

        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    }
}
