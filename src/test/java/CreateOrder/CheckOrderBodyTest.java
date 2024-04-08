package CreateOrder;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CheckOrderBodyTest {
    OrderData testOrder;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        testOrder = new OrderData(
                "Naruto",
                "Uchiha",
                "Konoha 142 apt.",
                "4",
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                new String[]{"BLACK"});

    }


    @Test
    @DisplayName("В теле ответа при создании заказа есть трэк номер")
    public void OrderBodyContainsTrack(){
        ValidatableResponse response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(testOrder)
                .post("/api/v1/orders").then().log().all();

        Integer track = response.extract().jsonPath().getInt("track");
        Assert.assertNotNull(track);
    }
}
