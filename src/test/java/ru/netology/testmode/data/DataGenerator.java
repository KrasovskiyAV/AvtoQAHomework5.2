package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import lombok.val;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    //private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static void sendRequest(RegistrationDto user) { //метод, который отправляет запрос. Принимает объект с типом нашего дата класса
        given()
                .spec(requestSpec) //использует спецификацию requestSpec
                .body(user) //передаем пользователя, которого принял метод
                .when()
                .post("api/system/users")
                .then()
                .statusCode(200);
    }

    public static String getRandomLogin() {
        var faker = new Faker(new Locale("en"));
        return faker.name().username();
    }

    public static String getRandomPassword() {
        var faker = new Faker(new Locale("en"));
        return faker.internet().password();
    }

    public static class Registration { //внутри статичного класса создаем, метод, который создает пользователя
        private Registration() {
        }

        public static RegistrationDto getUser(String status) { // создали юзера
            return new RegistrationDto(getRandomLogin(), getRandomPassword(), status);
        }

        public static RegistrationDto getRegisteredUser(String status) { // зарегистрировали эзера
            var registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}
