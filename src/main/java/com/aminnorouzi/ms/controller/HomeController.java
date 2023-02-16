package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.MovieRecord;
import com.aminnorouzi.ms.node.SectionNode;
import com.aminnorouzi.ms.node.SliderNode;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.tool.image.ImageLoader;
import com.aminnorouzi.ms.tool.notification.NotificationService;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
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
    private Label todayLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Label watchedLabel;
    @FXML
    private VBox contentPane;
    @FXML
    private VBox sectionPane;

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

        contentPane.getChildren().add(1, new SliderNode(sliders, this));

        sectionPane.getChildren().add(new SectionNode(this, "Recently Watched", sliders));
        sectionPane.getChildren().add(new SectionNode(this, "Recently Watched", sliders));
        sectionPane.getChildren().add(new SectionNode(this, "Recently Watched", sliders));
        sectionPane.getChildren().add(new SectionNode(this, "Recently Watched", sliders));

        totalLabel.setText(String.valueOf(data.getTotal()));
        watchedLabel.setText(String.valueOf(data.getWatched()));
        todayLabel.setText(String.valueOf(0));
    }

//    private void initWatchedChart(List<Movie> movies) {
//        XYChart.Series<String, Integer> watched = new XYChart.Series<>();
//        watched.setName("Watched");
//
//        XYChart.Series<String, Integer> added = new XYChart.Series<>();
//        added.setName("Added");
//
//        LocalDate now = LocalDate.now().plusDays(1);
//
//        for (int i = 30; i >= 1; i--) {
//            LocalDate date = now.minusDays(i);
//
//            AtomicInteger watchedCount = new AtomicInteger(0);
//            movies.forEach(m -> {
//                if (m.getWatchedAt() != null) {
//                    LocalDate watchedDate = m.getWatchedAt().toLocalDate();
//                    if (watchedDate.equals(date)) {
//                        watchedCount.getAndIncrement();
//                    }
//                }
//            });
//
//            AtomicInteger addedCount = new AtomicInteger(0);
//            movies.forEach(m -> {
//                LocalDate addedDate = m.getCreatedAt().toLocalDate();
//                if (addedDate.equals(date)) {
//                    addedCount.getAndIncrement();
//                }
//            });
//
//            watched.getData().add(new XYChart.Data<>(date.toString(), watchedCount.get()));
//            added.getData().add(new XYChart.Data<>(date.toString(), addedCount.get()));
//        }
//
//        chart.getData().addAll(added, watched);
//    }
}
