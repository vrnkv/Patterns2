package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;


class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login'] .button__text").click();
        $(byText("Личный кабинет")).shouldBe(visible, Duration.ofSeconds(15000));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id='action-login'] .button__text").click();
        $(byText("Ошибка")).shouldBe(visible);
        $(withText("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(15000));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login'] .button__text").click();
        $(byText("Ошибка")).shouldBe(visible);
        $(byText("Ошибка!")).shouldBe(visible);
        $(byText("Пользователь заблокирован")).shouldBe(visible, Duration.ofSeconds(15000));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login'] .button__text").click();
        $(byText("Ошибка")).shouldBe(visible);
        $(byText("Ошибка!")).shouldBe(visible);
        $(byText("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(15000));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("[data-test-id='action-login'] .button__text").click();
        $(byText("Ошибка")).shouldBe(visible);
        $(byText("Ошибка!")).shouldBe(visible);
        $(byText("Неверно указан логин или пароль")).shouldBe(visible, Duration.ofSeconds(15000));
    }
}