package in.reqres.crudtest;

import in.reqres.testbase.TestBase;
import in.reqres.userinfo.ReqresSteps;
import in.reqres.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class TestCRUD extends TestBase {
    static String name = "Jason" + TestUtils.getRandomValue();
    static String job = "Electrician" + TestUtils.getRandomValue();
    static String email = "eve.holt@reqres.in";
    static String password = "cityslicka";
    static String userID;

    @Steps
    ReqresSteps reqresSteps;

    @Title("This test will create a user")
    @Test
    public void test001() {
        ValidatableResponse response = reqresSteps.createUser(name, job);
        response.log().all().statusCode(201);
        userID = response.log().all().extract().path("id");
        System.out.println(userID);
    }

    @Title("Verify is User is added or not(*** unable to fetch data of new user ***)")
    @Test
    public void test002() {
        HashMap<String, ?> userMap = reqresSteps.getProductInfoByName(userID);
        System.out.println(userMap);
        Assert.assertThat(userMap, hasValue(name));
        System.out.println(userID);
    }

    @Title("This test will login a user")
    @Test
    public void test003() {
        HashMap<String, ?> tokenMap = reqresSteps.loginUser(email, password);
        Assert.assertThat(tokenMap, hasKey("token"));

    }

    @Title("This test will update a user")
    @Test
    public void test004() {
        job = job + "_updated";
        ValidatableResponse response = reqresSteps.updateUser(userID, job);
        response.log().all().statusCode(200);
    }

    @Title("This test will delete user")
    @Test
    public void test005() {
        ValidatableResponse response = reqresSteps.deleteProduct(userID);
        response.log().all().statusCode(204);
    }

    @Title("1. This will verify all details")
    @Test
    public void test006() {
        ValidatableResponse response = reqresSteps.getAllUserFromPageTwo();
        int page = response.log().all().extract().path("page");
        Assert.assertEquals(2, page);

        int per_page = response.log().all().extract().path("per_page");
        Assert.assertEquals(6, per_page);

        int data = response.log().all().extract().path("data[1].id");
        Assert.assertEquals(8, data);

        String firstname = response.log().all().extract().path("data[3].first_name");
        Assert.assertEquals("Byron", firstname);

        List<?> listOfData = response.log().all().extract().path("data");
        Assert.assertEquals(6, listOfData.size());

        String imageUrl = response.log().all().extract().path("data[5].avatar");
        Assert.assertEquals("https://reqres.in/img/faces/12-image.jpg", imageUrl);

        String supportHeading = response.log().all().extract().path("support.url");
        Assert.assertEquals("https://reqres.in/#support-heading", supportHeading);

        String supportText = response.log().all().extract().path("support.text");
        Assert.assertEquals("To keep ReqRes free, contributions towards server costs are appreciated!", supportText);

    }
}
