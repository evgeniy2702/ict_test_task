package openweathermap.test_task.repository;

import openweathermap.test_task.model.WeatherInfoByCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherInfoByCity, Long> {

    WeatherInfoByCity getByName(String nameCity);
}
