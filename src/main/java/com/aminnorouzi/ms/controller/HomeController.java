package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.MovieRecord;
import com.aminnorouzi.ms.node.WatchedNode;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.tool.image.ImageInfo;
import com.aminnorouzi.ms.tool.image.ImageService;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@FxmlView("/templates/view/home-view.fxml")
public class HomeController extends Controller {

    @FXML
    private Rectangle bannerPic;
    @FXML
    private Label favouriteGenre;
    @FXML
    private Label fullName;
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

    public HomeController(ViewSwitcher switcher, NotificationService notification, LibraryService library, ActivityService activity, ImageService image) {
        super(switcher, notification, library, activity, image);
    }

    @Override
    public void configure() {
        MovieRecord data = library.report(getUser());

        // TODO
//        if (!data.getIsAvailable()) {
//            return;
//        }

        ImageInfo bannerInfo = new ImageInfo("/templates/image/home-banner.png",
                300, 960, true);
        image.load(bannerInfo).thenAccept(image -> {
            ImagePattern pattern = new ImagePattern(image);
            bannerPic.setFill(pattern);
        });

        initWatchedChart(getUser().getMovies());
        initRecentWatched(data.getPlaylist());

        fullName.setText(getUser().getFullName());

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
