package com.aminnorouzi.ms.testing;

import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.util.ComponentUtils;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import org.controlsfx.control.GridView;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@Component
@RequiredArgsConstructor
public class ViewLoader {

    public static final ExecutorService executor = Executors.newFixedThreadPool(10);

    private final ComponentUtils componentUtils;

//    public GridView getGridView(List<Movie> movies) {
//        ObservableList<Movie> observableList = FXCollections.observableArrayList(movies);
//        GridView<Movie> myGrid = new GridView<>(observableList);
//        myGrid.setCache(true);
//        myGrid.setCacheHint(CacheHint.SPEED);
//        myGrid.setHorizontalCellSpacing(20);
//        myGrid.setVerticalCellSpacing(20);
//        myGrid.setCellWidth(200);
//        myGrid.setCellWidth(300);
//        myGrid.setCellFactory(gridView -> new GridCell<>() {
//
//            final ImageView imageView = new ImageView();
//
//            @Override
//            public void updateItem(Movie item, boolean empty) {
//                if (empty || item == null) {
//                    setText(null);
//                    setGraphic(null);
//                } else {
//                    Thread thread = new Thread(() -> {
//                        //imageView.setImage(new Image("https://image.tmdb.org/t/p/original" + item.getPoster()));
//
//                        Platform.runLater(() -> {
//                            //componentUtils.roundImage(imageView);
//                            setGraphic(new Label("text"));
//                            System.out.println("Running");
//                        });
//                    });
//
//                    thread.setDaemon(true);
//                    thread.start();
//                }
//            }
//        });
//
//        return myGrid;
//    }

    public void getView(List<Movie> movies, GridView gridView) {
        executor.submit(() -> addImagesToGrid(gridView, movies));
    }

    private static final ImageView POISON_PILL = new ImageView(new Image("/templates/image/monke.png"));

    private void addImagesToGrid(GridView<ImageView> gridView, List<Movie> movies) {
        final Queue<ImageView> imageQueue = new ConcurrentLinkedQueue<>();
        executor.submit(() -> deliverImagesToGrid(gridView, imageQueue));
        for (Movie movie : movies) {
            imageQueue.add(new ImageView(new Image("https://image.tmdb.org/t/p/original" + movie.getPoster())));
        }
        // Add poison image to signal the end of the queue.
        imageQueue.add(POISON_PILL);
    }

    private void deliverImagesToGrid(GridView<ImageView> gridView, Queue<ImageView> imageQueue) {
        try {
            Semaphore semaphore = new Semaphore(1);
            semaphore.acquire(); // Get the one and only permit
            boolean done = false;
            while (!done) {
                List<ImageView> imagesToAdd = new ArrayList<>();
                for (int i = 0; i < 1000; i++) {
                    final ImageView image = imageQueue.poll();
                    if (image == null) {
                        break; // Queue is now empty, so quit adding any to the list
                    } else if (image == POISON_PILL) {
                        done = true;
                    } else {
                        imagesToAdd.add(image);
                    }
                }

                if (!imagesToAdd.isEmpty()) {
                    Platform.runLater(() ->
                    {
                        try {
                            System.out.println("Running");
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
}
