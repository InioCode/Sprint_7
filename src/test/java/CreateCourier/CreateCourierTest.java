package CreateCourier;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

public class CreateCourierTest {
    private String randomLogin;
    private String randomPassword;
    private String randomName;
    private CourierData testCourier;

    @Rule
    public Timeout globalTimeout = Timeout.seconds(10);
    @Before
    public void setUp(){
        RestAssured.baseURI =  "https://qa-scooter.praktikum-services.ru";
        randomLogin = RandomStringUtils.random(7,true,false);
        randomPassword = RandomStringUtils.random(4,true,true);
        randomName = RandomStringUtils.random(4,true,false);

        //System.out.println("Login:" + randomLogin);
        //System.out.println("Password:" + randomPassword);
        //System.out.println("Name:" + randomName);

        testCourier = new CourierData(randomLogin,randomPassword, randomName);
    }

    @After
    public void ending(){
        String userId = "";

        Response response = RestAssured.given()
                    .header("Content-type", "application/json")
                    .body("{\"login\": \"" + randomLogin + "\", \"password\": \"" + randomPassword + "\"}")
                    .and()
                    .post("/api/v1/courier/login");

        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(response.asString());
        while (matcher.find()){
            userId +=  matcher.group();
        }
        //System.out.println("response: " + response.asString());
        //System.out.println("UserId: " + userId);

        RestAssured.given()
                .delete("/api/v1/courier/" + userId);
    }

    @Test
    @DisplayName("Создание курьера возвращает код 201")
    public void createCourierReturnCode201(){
            RestAssured.given()
                .header("Content-type", "application/json")
                .body(testCourier)
                .and()
                .post("/api/v1/courier")
                .then().statusCode(201);
    }

    @Test
    @DisplayName("Созданание дубликата курьера возвращает код 409")
    @Description("Тест создает курьера после чего снова пытается создать курьера с тем же набором данных")
    public void createDuplicateCourierReturnCode409(){

        Response response = given()
                .header("Content-type", "application/json")
                .body(testCourier)
                .and()
                .post("/api/v1/courier");

        if (response.asString().contains("\"ok\":true")){
            RestAssured.given()
                    .header("Content-type", "application/json")
                    .body(testCourier)
                    .and()
                    .post("/api/v1/courier").then().statusCode(409);
        } else {
            System.out.println("При попытке создать нового курьера для проверки на дубликат произошла ошибка: " + response.asString());
        }
    }

    @Test
    @DisplayName("Создание курьера без поля login возвращает ошибку 400")
    public void createCourierWithoutFieldLogin(){
        CreateCourierErrorCode errorCode = RestAssured.given()
                .header("Content-type", "application/json")
                .body("{\"password\": \"" + randomPassword + "\", \"firstName\": \"" + randomName + "\"}")
                .and()
                .post("/api/v1/courier").as(CreateCourierErrorCode.class);
        Assert.assertEquals(400 ,errorCode.getCode());
        Assert.assertEquals("Недостаточно данных для создания учетной записи" ,errorCode.getMessage());
    }

    @Test
    @DisplayName("Создание курьера без поля password возвращает ошибку 400")
    public void createCourierWithoutFieldPassword(){
        CreateCourierErrorCode errorCode = RestAssured.given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"" + randomLogin + "\", \"firstName\": \"" + randomName + "\"}")
                .and()
                .post("/api/v1/courier").as(CreateCourierErrorCode.class);
        Assert.assertEquals(400 ,errorCode.getCode());
        Assert.assertEquals("Недостаточно данных для создания учетной записи" ,errorCode.getMessage());
    }

    @Test
    @DisplayName("Создание курьера без поля firstName возвращает ошибку 400")
    public void createCourierWithoutFieldFirstName(){
        CreateCourierErrorCode errorCode = RestAssured.given()
                .header("Content-type", "application/json")
                .body("{\"login\": \"" + randomLogin + "\", \"password\": \"" + randomPassword + "\"}")
                .and()
                .post("/api/v1/courier").as(CreateCourierErrorCode.class);
        Assert.assertEquals(400 ,errorCode.getCode());
        Assert.assertEquals("Недостаточно данных для создания учетной записи" ,errorCode.getMessage());
    }

    @Test
    @DisplayName("Удачное создание курьера возвращает в ответе ok:true")
    public void successCreatingReturnTrue() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(testCourier)
                .and()
                .post("/api/v1/courier");
        Assert.assertEquals("{\"ok\":true}", response.asString());
    }
}
