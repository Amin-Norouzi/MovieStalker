package com.aminnorouzi.ms.testing;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.controlsfx.control.cell.ImageGridCell;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ThumbnailGridViewBrowser extends Application {
    private static final int CELL_SIZE = 200;
    public static final ExecutorService executor = Executors.newFixedThreadPool(10);

//    public static void main(String[] args) {
//        launch(args);
//    }

    @Override
    public void start(Stage primaryStage) {
        // Create a Scene with a ScrollPane that contains a TilePane.
        GridView<Image> gridView = new GridView<>();
        gridView.setCellFactory(gridView1 -> new ImageGridCell());
        gridView.getStyleClass().add("pane");
        gridView.setCache(true);
        gridView.setCacheHint(CacheHint.SPEED);
        gridView.setCellWidth(CELL_SIZE);
        gridView.setCellHeight(CELL_SIZE);
        gridView.setHorizontalCellSpacing(10);
        gridView.setVerticalCellSpacing(10);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(gridView);

        primaryStage.setScene(new Scene(scrollPane, 1000, 600));

        // Start showing the UI before taking time to load any images
        primaryStage.show();

        // Load images in the background so the UI stays responsive.
        executor.submit(() -> addImagesToGrid(gridView));

        // Quit the application when the window is closed.
        primaryStage.setOnCloseRequest(x -> {
            executor.shutdown();
            Platform.exit();
            System.exit(0);
        });
    }

    public GridView getGridView() {
        GridView<Image> gridView = new GridView<>();
        gridView.setCellFactory(gridView1 -> new ImageGridCell());
        gridView.getStyleClass().add("pane");
        gridView.setCache(true);
        gridView.setCacheHint(CacheHint.SPEED);
        gridView.setCellWidth(CELL_SIZE);
        gridView.setCellHeight(CELL_SIZE);
        gridView.setHorizontalCellSpacing(10);
        gridView.setVerticalCellSpacing(10);

        // Load images in the background so the UI stays responsive.
        executor.submit(() -> addImagesToGrid(gridView));

        return gridView;
    }

    private static final Image POISON_PILL = createFakeImage(1, 1);

    private void addImagesToGrid(GridView<Image> gridView) {
        int numCells = 100;
        final Queue<Image> imageQueue = new ConcurrentLinkedQueue<>();
        executor.submit(() -> deliverImagesToGrid(gridView, imageQueue));
        for (int i = 0; i < numCells; i++) {
            // (In the real application, get a list of image filenames, read each image's thumbnail, generating it if needed.
            // (In this minimal reproducible code, we'll just create a new dummy image for each ImageView)
            imageQueue.add(createFakeImage(i, CELL_SIZE));
        }
        // Add poison image to signal the end of the queue.
        imageQueue.add(POISON_PILL);
    }

    private void deliverImagesToGrid(GridView<Image> gridView, Queue<Image> imageQueue) {
        try {
            Semaphore semaphore = new Semaphore(1);
            semaphore.acquire(); // Get the one and only permit
            boolean done = false;
            while (!done) {
                List<Image> imagesToAdd = new ArrayList<>();
                for (int i = 0; i < 1000; i++) {
                    final Image image = imageQueue.poll();
                    if (image == null) {
                        break; // Queue is now empty, so quit adding any to the list
                    } else if (image == POISON_PILL) {
                        done = true;
                    } else {
                        imagesToAdd.add(image);
                    }
                }

                if (imagesToAdd.size() > 0) {
                    Platform.runLater(() ->
                    {
                        try {
                            gridView.getItems().addAll(imagesToAdd);
                        } finally {
                            semaphore.release();
                        }
                    });
                    // Block until the items queued up via Platform.runLater() have been processed by the UI thread and release() has been called.
                    semaphore.acquire();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Create an image with a bunch of rectangles in it just to have something to display.
    private static Image createFakeImage(int imageIndex, int size) {
//        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
//        Graphics g = image.getGraphics();
//        for (int i = 1; i < size; i ++) {
//            g.setColor(new Color(i * imageIndex % 256, i * 2 * (imageIndex + 40) % 256, i * 3 * (imageIndex + 60) % 256));
//            g.drawRect(i, i, size - i * 2, size - i * 2);
//        }
//        return SwingFXUtils.toFXImage(image, null);
        return new Image("https://i.pinimg.com/736x/4e/46/61/4e4661f7899ea2a246dd55fbbea0319a.jpg", 200, 150, true, true, true);
    }
}
