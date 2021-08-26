package openweathermap.test_task.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import javassist.NotFoundException;
import openweathermap.test_task.model.WeatherInfoByCity;
import openweathermap.test_task.repository.WeatherRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private final WeatherRepository  weatherRepository;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public WeatherInfoByCity getByCityName(String nameCity){
        System.out.println(weatherRepository.getByName(nameCity));
        if(weatherRepository.getByName(nameCity) == null)
            return null;
        return weatherRepository.getByName(nameCity);
    }

    public void save(JSONObject jsonObject) {
        Gson gson = new Gson();
        WeatherInfoByCity weatherInfoByCity = gson.fromJson(jsonObject.toString(), WeatherInfoByCity.class);
        weatherRepository.save(weatherInfoByCity);
    }

    public boolean update(JSONObject jsonObject) throws NotFoundException, JsonProcessingException {

        WeatherInfoByCity weatherInfoByCityDB = weatherRepository.getById(getByCityName(jsonObject.getString("name")).getId());
        if(weatherInfoByCityDB == null) {
            throw new NotFoundException("Такого объекта не существует !");
        }

        WeatherInfoByCity weatherInfoByCity = new ObjectMapper().readValue(jsonObject.toString(), WeatherInfoByCity.class);
        weatherInfoByCity.setId(weatherInfoByCityDB.getId());
        weatherRepository.save(weatherInfoByCity);
        return  true;
    }
}
