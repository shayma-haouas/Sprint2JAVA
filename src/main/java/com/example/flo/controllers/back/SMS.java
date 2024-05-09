package com.example.flo.controllers.back;

public class SMS {
    public class SMSSenderApp extends Application {

        // Your Twilio Account SID and Auth Token
        private static final String ACCOUNT_SID = "your_account_sid";
        private static final String AUTH_TOKEN = "your_auth_token";

        @Override
        public void start(Stage primaryStage) {
            primaryStage.setTitle("SMS Sender");

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(10, 10, 10, 10));
            grid.setVgap(
                    grid.setVgap
                    8);
            grid.setHgap(10);

            Label toLabel = new Label("To:");
            GridPane.setConstraints(toLabel, 0, 0);

            TextField toField = new TextField();
            toField.setPromptText("Recipient's Phone Number");
            GridPane.setConstraints(toField, 1, 0);

            Label messageLabel = new Label("Message:");
            GridPane.setConstraints(messageLabel, 0, 1);

            TextField messageField = new TextField();
            messageField.setPromptText("Type your message here...");
            GridPane.setConstraints(messageField,
                    Grid
                    1, 1);

            Button sendButton = new Button("Send");
            GridPane.setConstraints(sendButton, 1, 2);
            sendButton.setOnAction(e -> {
                sendSMS(toField.getText(), messageField.getText());
                toField.clear();
                messageField.clear();
            });

            grid.getChildren().addAll(toLabel, toField, messageLabel, messageField, sendButton);


            sendButton.setOnAction(e -> {
                sendSMS(toField.getText(), messageField.getText());
                toField.clear();
                messageField.clear();
            });


            Scene scene = new Scene(grid, 400, 200);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        private void sendSMS(String to, String message) {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);


            Twilio
            Message twilioMessage = Message.creator(
                            new PhoneNumber(to),
                            new PhoneNumber("+your_twilio_phone_number"),
                            message)
                    .create();

            System.out.println("SMS sent successfully. SID: " + twilioMessage.getSid());
        }

        public static void main(String[] args) {
            launch(args);
        }
    
}
