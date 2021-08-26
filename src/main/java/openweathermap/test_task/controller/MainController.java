package openweathermap.test_task.controller;

import javassist.NotFoundException;
import openweathermap.test_task.service.WeatherService;
import openweathermap.test_task.utils.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class MainController {

    private Utils utils;
    private WeatherService weatherService;

    @Autowired
    public MainController(Utils utils, WeatherService weatherService) {
        this.utils = utils;
        this.weatherService = weatherService;
    }

    @GetMapping("")
    public ModelAndView getIndexPage(ModelAndView modelAndView){
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
        String time = LocalDateTime.now().format(formatter1);
        modelAndView.addObject("date", time);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @PostMapping(value = "/city/weather", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ModelAndView getWeatherInfo(@RequestParam(name = "enter", required = false) String city,
                                       ModelAndView modelAndView) throws IOException, NotFoundException {

        String util = utils.parseUrl(city);
        JSONObject jsonObject = utils.createJSONObject(util);

        if(weatherService.getByCityName(jsonObject.getString("name")) == null)
            weatherService.save(jsonObject);
        else weatherService.update(jsonObject);

        actionModelAndView(jsonObject,modelAndView);

        return modelAndView;
    }

    private void actionModelAndView(JSONObject jsonObject, ModelAndView modelAndView) {

        modelAndView.addObject("name", jsonObject.getString("name"));
        modelAndView.addObject("date", jsonObject.getString("time"));
        modelAndView.addObject("lon", String.valueOf(jsonObject.getFloat("lon")));
        modelAndView.addObject("lat", String.valueOf(jsonObject.getFloat("lat")));
        modelAndView.addObject("main", jsonObject.getString("main"));
        modelAndView.addObject("desc", jsonObject.getString("description"));
        modelAndView.addObject("icon", jsonObject.getString("icon"));
        modelAndView.setViewName("index");
    }
}
