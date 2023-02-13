package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.MovieRecord;
import com.aminnorouzi.ms.node.SliderNode;
import com.aminnorouzi.ms.node.WatchedNode;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.tool.notification.NotificationService;
import com.aminnorouzi.ms.tool.image.ImageLoader;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@FxmlView("/templates/view/home-view.fxml")
public class HomeController extends Controller {

    @FXML
    private Label favouriteGenre;
    @FXML
    private Label latestMovie;
    @FXML
    private TilePane recentMovies;
    @FXML
    private LineChart<String, Integer> chart;
    @FXML
    private Label totalCount;
    @FXML
    private Label watchedCount;
    @FXML
    private ScrollPane pane;
    @FXML
    private HBox sliderContainer;

    public HomeController(ViewSwitcher switcher, NotificationService notification, LibraryService library, ActivityService activity, ImageLoader image) {
        super(switcher, notification, library, activity, image);
    }

    @Override
    public void configure() {
        if (getUser().getMovies().isEmpty()) {
            return;
        }

        MovieRecord data = library.report(getUser());

        int count = 1;
        List<Movie> sliders = new ArrayList<>();
        for (Movie movie : data.getPlaylist()) {
            if (count <= 5) {
                sliders.add(movie);
            }

            count++;
        }
        sliderContainer.getChildren().add(new SliderNode(sliders, this));

        // TODO
//        if (!data.getIsAvailable()) {
//            return;
//        }

        initWatchedChart(getUser().getMovies());
        initRecentWatched(data.getPlaylist());

        totalCount.setText(String.valueOf(data.getTotal()));
        watchedCount.setText(String.valueOf(data.getWatched()));
        latestMovie.setText(data.getLatest().getTitle());
        favouriteGenre.setText(data.getGenre());
    }

    private void initRecentWatched(List<Movie> movies) {
        AtomicInteger count = new AtomicInteger(1);
        movies.forEach(movie -> {
            WatchedNode node = new WatchedNode(movie, count.getAndIncrement(), this);
            recentMovies.getChildren().add(node);
        });
    }

    private void initWatchedChart(List<Movie> movies) {
        XYChart.Series<String, Integer> watched = new XYChart.Series<>();
        watched.setName("Watched");

        XYChart.Series<String, Integer> added = new XYChart.Series<>();
        added.setName("Added");

        LocalDate now = LocalDate.now().plusDays(1);

        for (int i = 30; i >= 1; i--) {
            LocalDate date = now.minusDays(i);

            AtomicInteger watchedCount = new AtomicInteger(0);
            movies.forEach(m -> {
                if (m.getWatchedAt() != null) {
                    LocalDate watchedDate = m.getWatchedAt().toLocalDate();
                    if (watchedDate.equals(date)) {
                        watchedCount.getAndIncrement();
                    }
                }
            });

            AtomicInteger addedCount = new AtomicInteger(0);
            movies.forEach(m -> {
                LocalDate addedDate = m.getCreatedAt().toLocalDate();
                if (addedDate.equals(date)) {
                    addedCount.getAndIncrement();
                }
            });

            watched.getData().add(new XYChart.Data<>(date.toString(), watchedCount.get()));
            added.getData().add(new XYChart.Data<>(date.toString(), addedCount.get()));
        }

        chart.getData().addAll(added, watched);
    }

    @FXML
    private void onSeeAll(MouseEvent event) {
        switchTo(View.LIBRARY);
    }
}
