package LoginCourier;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

public class LoginTest {
    private String login;
    private String password;
    private String notExistLogin;
    private String wrongPassword;

    @Rule
    public Timeout globalTimeout = Timeout.seconds(10);
    @Before
    public void setUp(){
        RestAssured.baseURI =  "https://qa-scooter.praktikum-services.ru";
        login = "courierTest0001";
        password = "1234";
        notExistLogin = "NotExist";
        wrongPassword = "0000";
    }

    @Test
    @DisplayName("Успешный вход возвращает код 200")
    public void successLogin() {
        RestAssured.given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"" + login + "\",\"password\": \"" + password + "\"}")
                .and()
                .post("/api/v1/courier/login")
                .then().statusCode(200);
    }

    @Test
    @DisplayName("Вход без поля login возвращает код 400")
    public void loginWithoutFieldLogin(){
        LoginCourierErrorCode errorCode = RestAssured.given()
                .header("Content-type", "application/json")
                .body("{\"password\": \"" + password + "\"}")
                .and()
                .post("/api/v1/courier/login").as(LoginCourierErrorCode.class);
        Assert.assertEquals(400, errorCode.getCode());
        Assert.assertEquals("Недостаточно данных для входа", errorCode.getMessage());
    }

    @Test
    @DisplayName("Вход без поля password возвращает код 400")
    public void loginWithoutFieldPassword(){
        LoginCourierErrorCode errorCode = RestAssured.given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"" + login + "\"}")
                .and()
                .post("/api/v1/courier/login").as(LoginCourierErrorCode.class);
        Assert.assertEquals(400, errorCode.getCode());
        Assert.assertEquals("Недостаточно данных для входа", errorCode.getMessage());
    }

    @Test
    @DisplayName("Вход с несуществующим логином возвращает код 404")
    public void loginWithNotExistentLogin(){
        LoginCourierErrorCode errorCode = RestAssured.given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"" + notExistLogin + "\",\"password\": \"" + password + "\"}")
                .and()
                .post("/api/v1/courier/login").as(LoginCourierErrorCode.class);
        Assert.assertEquals(404, errorCode.getCode());
        Assert.assertEquals("Учетная запись не найдена", errorCode.getMessage());
    }

    @Test
    @DisplayName("Вход с неверным паролем возвращает код 404")
    public void loginWithWrongPassword(){
        LoginCourierErrorCode errorCode = RestAssured.given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"" + login + "\",\"password\": \"" + wrongPassword + "\"}")
                .and()
                .post("/api/v1/courier/login").as(LoginCourierErrorCode.class);
        Assert.assertEquals(404, errorCode.getCode());
        Assert.assertEquals("Учетная запись не найдена", errorCode.getMessage());
    }

    @Test
    @DisplayName("Успешный вход в теле ответа возвращает id курьера")
    public void successLoginReturnId(){
        ValidatableResponse response = RestAssured.given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"courierTest0001\",\"password\": \"1234\"}")
                .and()
                .post("/api/v1/courier/login").then();
        Integer id = response.extract().jsonPath().getInt("id");
        Assert.assertNotNull(id);
    }

}
