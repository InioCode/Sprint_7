package CreateOrder;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckOrderBody {
    OrderData testOrderWithGrey;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("В теле ответа при создании заказа есть трэк номер")
    public void OrderBodyContainsTrack(){
        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(testOrderWithGrey)
                .post("/api/v1/orders");
        Pattern pattern = Pattern.compile(".*track.*\\d+.*");
        Matcher matcher = pattern.matcher(response.asString());
        Assert.assertTrue(matcher.matches());
    }
}
