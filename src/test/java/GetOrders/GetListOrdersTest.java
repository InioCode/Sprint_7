package GetOrders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GetListOrdersTest {

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("В теле ответа Списка заказов есть список заказов")
    public void getListOrdersReturnListOfOrders(){
        GetListOrdersData listOrders = RestAssured.given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders?limit=3&page=0").as(GetListOrdersData.class);

        Assert.assertTrue(listOrders.getOrders().size()>1);
    }
}
