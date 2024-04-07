package CreateOrder;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateOrderTestParam {
    private String[] color;
    private OrderData testOrder;

    public CreateOrderTestParam(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[] getProperty(){
        return new Object[][]{
                {new String[] {"BLACK"}},
                {new String[] {"GREY"}},
                {new String[] {"BLACK", "GREY"}},
                {new String[] {""}}
        };
    }

    @Before
    public void setUp(){
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
                color);
    }

    @Test
    @DisplayName("Создание заказа с разными значениями цвета")
    public void createOrderWithDifferentColor(){
        RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(testOrder)
                .post("/api/v1/orders")
                .then().statusCode(201);
    }
}
