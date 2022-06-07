package in.reqres.userinfo;

import in.reqres.contsatns.EndPoints;
import in.reqres.model.ReqresPojo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import jline.internal.TestAccessible;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import io.restassured.response.Response;
import net.thucydides.core.annotations.TestsRequirement;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class ReqresSteps {
    @Step("Creating User with name : {0}, Job: {1}")
    public ValidatableResponse createUser(String name,
                                          String job) {
        ReqresPojo reqresPojo = new ReqresPojo();
        reqresPojo.setName(name);
        reqresPojo.setJob(job);
        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(reqresPojo)
                .when()
                .post(EndPoints.GET_ALL_USERS)
                .then();
    }

    @Step("Getting All Users from Page 2")
    public ValidatableResponse getAllUserFromPageTwo() {
        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("page", 2)
                .when()
                .get(EndPoints.GET_ALL_USERS)
                .then();
    }

    @Step("Getting the User information with ID: {0}")
    public HashMap<String, ?> getProductInfoByName(String userID) {
        HashMap<String, ?> userMap = SerenityRest.given().log().all()
                .when()
                .pathParam("userID", userID)
                .get(EndPoints.GET_USER_BY_ID)
                .then()
                .extract().path("");

        return userMap;
    }

    @Step("Login User with email : {0}, password: {1}")
    public HashMap<String, ?> loginUser(String email,
                                        String password) {
        ReqresPojo reqresPojo = new ReqresPojo();
        reqresPojo.setEmail(email);
        reqresPojo.setPassword(password);
        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(reqresPojo)
                .when()
                .post(EndPoints.USER_LOGIN)
                .then()
                .statusCode(200)
                .extract()
                .path("");
    }

    @Step("Update User with name : {0}, Job: {1}")
    public ValidatableResponse updateUser(String userID, String job) {
        ReqresPojo reqresPojo = new ReqresPojo();
        reqresPojo.setJob(job);
        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("userID", userID)
                .body(reqresPojo)
                .when()
                .patch(EndPoints.UPDATE_USER_BY_ID)
                .then();
    }

    @Step("Deleting user with ID {0}")
    public ValidatableResponse deleteProduct(String userID) {
        return SerenityRest.given().log().all()
                .pathParam("userID", userID)
                .when()
                .delete(EndPoints.DELETE_USER_BY_ID)
                .then();
    }
}
