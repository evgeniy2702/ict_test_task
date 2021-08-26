package openweathermap.test_task.utils;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@PropertySource("classpath:weather.properties")
public class Utils {

    @Value("${endpoint_part_one}")
    String endpointPartOne;

    @Value("${endpoint_part_two}")
    String endpointPartTwo;

    @Value("${endpoint_token}")
    String endpointToken;

    public String parseUrl(String nameCity) throws IOException {


        URL url = new URL(endpointPartOne + nameCity + endpointPartTwo + endpointToken);

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setRequestMethod("GET");

        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public JSONObject createJSONObject(String stringJSON) {

        JsonParser parser = new JsonParser();

        JsonObject jsonObject = parser.parse(stringJSON).getAsJsonObject();

        JSONObject jsonNewObject = new JSONObject();
        String nameCity = jsonObject.get("name").getAsString();

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
        String time = LocalDateTime.now().format(formatter1);

        Float lon = Float.valueOf(jsonObject.get("coord").getAsJsonObject().get("lon").getAsString());
        Float lat = Float.valueOf(jsonObject.get("coord").getAsJsonObject().get("lat").getAsString());

        String weather_main = jsonObject.getAsJsonArray("weather").get(0)
                .getAsJsonObject().get("main").getAsString();

        String weather_description = jsonObject.getAsJsonArray("weather").get(0)
                .getAsJsonObject().get("description").getAsString();

        String weather_icon = "http://openweathermap.org/img/wn/" + jsonObject.getAsJsonArray("weather").get(0)
                .getAsJsonObject().get("icon").getAsString() + "@2x.png";

        jsonNewObject.put("name", nameCity);
        jsonNewObject.put("time", time);
        jsonNewObject.put("lon", lon);
        jsonNewObject.put("lat", lat);
        jsonNewObject.put("main",weather_main);
        jsonNewObject.put("description",weather_description);
        jsonNewObject.put("icon",weather_icon);

        return jsonNewObject;
    }
}
