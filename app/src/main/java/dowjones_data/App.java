/*
 * This Java source file was generated by the Gradle 'init' task.
 */

// Note: Yahoo Finance API has been disbaled as that was the first API i tried
// using before i resorted to using Finage since it gives real time stock data
// Finage API key: API_KEY246OEREO7FCXK3FU4QIZ72HO174X5UDD
package dowjones_data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class App extends Application {
    private static final String API_KEY = "API_KEY246OEREO7FCXK3FU4QIZ72HO174X5UDD";
    private static final String SYMBOL = "AAPL"; // The stock symbol can be replaced with any other symbol

    private Queue<Double> stockPrices = new LinkedList<>();
    private double sum = 0.0;

    private LineChart<Number, Number> lineChart;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // This will Create X and Y axes for the line chart
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Average Stock Price Graph");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        lineChart.getData().add(series);

        Scene scene = new Scene(lineChart, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Stock Price Graph");
        primaryStage.show();

        new Thread(this::updateStockPrices).start();
    }

    private void updateStockPrices() {
        while (true) {
            double currentPrice = fetchStockPrice();
            stockPrices.add(currentPrice);
            sum += currentPrice;

            if (stockPrices.size() > 5) {
                double removedPrice = stockPrices.poll();
                sum -= removedPrice;
            }

            double averagePrice = sum / stockPrices.size();
            System.out.println("Stock Price: " + currentPrice);
            System.out.println("Average Price: " + averagePrice);

            Platform.runLater(() -> {
                XYChart.Series<Number, Number> series = lineChart.getData().get(0);
                series.getData().add(new XYChart.Data<>(series.getData().size() + 1, averagePrice));
            });

            try {
                Thread.sleep(5000); // Wait for 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static double fetchStockPrice() {
        String url = "https://api.finage.co.uk/last/stock/" + SYMBOL + "?apikey=" + API_KEY;

        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(url);
            String response = EntityUtils.toString(httpClient.execute(request).getEntity());
            System.out.println("Response: " + response); // Print the raw response
            
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            double currentPrice = jsonObject.get("ask").getAsDouble(); // Use "ask" instead of "price"
            return currentPrice;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0.0;
    }
}
