package ui.design_autotest;

public class TransferUITest {
    /*
        Название: Успешный перевод

        Предусловие теста:
        Создать юзера user#1
        Создать юзера user#2
        Создать у user#1 аккаунт account#1_1
        Создать у user#1 аккаунт account#1_2
        Создать у user#2 аккаунт account#2_1
        Пополнить account#1_1 на сумму N
        Залогиниться под user#1
        Перейти на страницу перевода /transfer

        Шаги выполнения:
        1) в поле Select Your Account выбрать account#1_1
        2) в поле Recipient Name: Вписать username account#1_1
        3) в поле Recipient Account Number: вписать accountNumber account#1_2
        4) в поле Amount: вставить число от 0.01 до N
        5) Отметить чекбокс Confirm details are correct
        6) Нажать на кнопку Send Transfer
        Появилось push-уведомление с заголовком: Уведомление от сайта localhost
        Появилось push-уведомление с текстом: Successfully transfer ${сумма перевода} to account account#1_2
        7) Нажать на кнопку "закрыть" -> перенаправили на главную страницу /dashboard
        8) Проверить баланс через backend

        постусловие:
        Удалить аккаунт account#1_1
        Удалить аккаунт account#1_2
        Удалить аккаунт account#2_1
        Удалить юзера user#1
        Удалить юзера user#2



        Название: Ошибка при переводе

        Предусловие теста:
        Создать юзера user#1
        Создать юзера user#2
        Создать у user#1 аккаунт account#1_1
        Создать у user#1 аккаунт account#1_2
        Создать у user#2 аккаунт account#2_1
        Пополнить account#1_1 на сумму N
        Залогиниться под user#1
        Перейти на страницу перевода /transfer

        Шаги выполнения:
        1) в поле Select Your Account выбрать account#1_1
        2) в поле Recipient Name: Вписать username account#1_1
        3) в поле Recipient Account Number: вписать некорректный accountNumber
        4) в поле Amount: вставить число от 0.01 до N
        5) Отметить чекбокс Confirm details are correct
        6) Нажать на кнопку Send Transfer
        Появилось push-уведомление с заголовком: Уведомление от сайта localhost
        Появилось push-уведомление с текстом: No user found with this account number
        7) Нажать на кнопку "закрыть" -> остались на странице перевода /transfer
        8) Проверить баланс через backend

        постусловие:
        Удалить аккаунт account#1_1
        Удалить аккаунт account#1_2
        Удалить аккаунт account#2_1
        Удалить юзера user#1
        Удалить юзера user#2
     */
}
