package com.example.flo.utils;

import org.controlsfx.control.Notifications;

public class AlertUtils {

    public static void informationNotification(String message) {
        Notifications.create()
                .title("Erreur de saisie")
                .text(message)
                .showInformation();
    }

    public static void errorNotification(String message) {
        Notifications.create()
                .title("Erreur")
                .text(message)
                .showError();
    }

    public static void successNotification(String message) {
        Notifications.create()
                .title("Succes")
                .text(message)
                .showInformation();
    }

}
