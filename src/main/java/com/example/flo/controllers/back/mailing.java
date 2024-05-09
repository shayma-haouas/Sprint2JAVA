package com.example.flo.controllers.back;

public class mailing {
    public class EmailSenderApp extends Application {

        private static final String EMAIL_USERNAME = "your_email@gmail.com";
        private static final String EMAIL_PASSWORD = "your_password";

        @Override
        public void start(Stage primaryStage) {
            primaryStage.setTitle(
                    primaryStage.setTitle(
                            "Email Sender");



            GridP
            GridPane grid = new GridPane();
            grid.setPadding(

                    new Insets(10, 10, 10, 10));
            grid.setVgap(

                    8);
            grid.setHgap(

                    10);




            Label toLabel = new Label("To:");
            GridPane.setConstraints(toLabel,
                    GridPane.setConstraints(toLabe
                            0, 0);



            TextFie
            TextField toField = new TextField();
            toField.setPromptText(

                    "Recipient's Email");
            GridPane.setConstraints(toField,
                    Gri
                    1, 0);



            Labe
            Label subjectLabel = new Label("Subject:");
            GridPane.setConstraints(subjectLabel,
                    GridPane.setConstraint

                    GridPa
                    0, 1);



            TextFiel
            TextField subjectField = new TextField();
            subjectField.setPromptText(
                    subjectField.setProm
                    "Email Subject");
            GridPane.setConstraints(subjectField,
                    GridPane.setConstraints(subject


                            1, 1);



            Label mes
            Label messageLabel = new Label("Message:");
            GridPane.setConstraints(messageLabel,
                    GridPane.setConstrain
                    0, 2);




            TextArea messageArea = new TextArea();
            messageArea.setPromptText(
                    messageArea.setP
                    "Type your message here...");
            GridPane.setConstraints(messageArea,
                    GridPane.setConstraints(messag
                            1, 2);



            Butt
            Button sendButton = new Button("Send");
            GridPane.setConstraints(sendButton,
                    GridPane.setC
                    1, 3);
            sendButton.setOnAction(e -> {
                sendEmail(toField.getText(), subjectField.getText(), messageArea.getText());
                clearFields(toField, subjectField, messageArea);
            });

            grid.getChildren().addAll(toLabel, toField, subjectLabel, subjectField, messageLabel, messageArea, sendButton);


            sendButton.setOnAction(e -> {
                sendEmail(toField.getText(), subjectField.getText(), messageArea.getText());
                clearFields(toField, subjectField, messageArea);
            });

            grid.getChildren().addAll(toLabel, toField, subjectLabel, subjectField, messageLabel, messageArea, sendButton);



            sendButton.setOnAction(e -> {
                sendEmail(toField.getText(), subjectField.getText(), messageArea.getText());
                clearFields(toField, subjectField, messageArea);
            });

            grid.getChildren().addAll(toLabel, toField, subjectLabel, subjectField, messageLabel, messageArea, sendButton

                    sendButton.setOnAction(e -> {
                        sendEmail(toField.getText(), subjectField.getText(), messageArea.getText());
                        clearFields(toField, subjectField, messageArea);
                    });

            grid.getChildren().addAll(toLabel, toField, subjectLabel, subjectField, messageLabel, messageArea, sendBut

                    sendButton.setOnAction(e -> {
                        sendEmail(toField.getText(), subjectField.getText(), messageArea.getText());
                        clearFields(toField, subjectField, messageArea);
                    });

            grid.getChildren().addAll(toLabel, toField, subjectLabel, subjectField, messageLabel, messageArea, send

                    sendButton.setOnAction(e -> {
                        sendEmail(toField.getText(), subjectField.getText(), messageArea.getText());
                        clearFields(toField, subjectField, messageArea);
                    });

            grid.getChildren().addAll(toLabel, toField, subjectLabel, subjectField, messageLabel, message

                    sendButton.setOnAction(e -> {
                        sendEmail(toField.getText(), subjectField.getText(), messageArea.getText());
                        clearFields(toField, subjectField, messageArea);
                    });

            grid.getChildren().addAll(toLabel, toField, subjectLabel, subjectField,

                    sendButton.setOnAction(e -> {
                        sendEmail(toField.getText(), subjectField.getText(), messageArea.getText());
                        clearFields(toField, subjectField, messageArea);
                    });

            grid.getChildren().addAll(toLabel, toFie

                    sendButton.setOnAction(e -> {
                        sendEmail(toField.getText(), subjectField.getText(), messageArea.getText());
                        clearFields(toField, subjectField, messageArea);
                    });

            grid.getC

            sendButton.setOnAction(e -> {
                sendEmail(toField.getText(), subjectField.getText(), messageArea.getText());
                clearFields(toField, subjectField, messageArea);

                sendButton.setOnAction(e -> {
                    sendEmail(toField.getText(), subjectField.getText(), messageArea.getText());
                    clearFields(toFie

                            sendButton.setOnAction(e -> {
                                sendEmail(toField.getText(), subjectField.getText(), messageArea.getText())

                                sendButton.setOnAction(e -> {
                                            sendEmail(toField.getText(), subjectField.ge

                                                    sendButton.setOnAction(e -> {
                                                                sendEmail(toF

                                                                        sendButton.setOnAction(e


                                                                                Scene scene = new Scene(grid, 400, 300);
                                                                primaryStage.setScene(scene);
                                                                primaryStage.show();
                                                            }


                                                            primaryStage.setScene(scene);
                                            primaryStage.show();
                                        }

                                        primaryStage.setScene(scene);
                                pri

                                primaryStage.setSc


                                private void sendEmail(String to, String subject, String message) {

                                    Properties
                                    Properties props = new Properties();
                                    props.put("mail.smtp.auth", "true");
                                    props.put(
                                            props
                                            "mail.smtp.starttls.enable", "true");
                                    props.put(
                                            props.p


                                            "mail.smtp.host", "smtp.gmail.com");
                                    props.put(
                                            props.pu


                                            "mail.smtp.port", "587");




                                    Session session = Session.getInstance(props, new Authenticator() {
                                        @Override


                                        protected PasswordAuthentication getPasswordAuthentication() {
                                            return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                                        }
                                    });


                                }

                                try {

                                    Message emailM
                                    Message emailMessage = new MimeMessage(session);
                                    emailMessage.setFrom(

                                            new InternetAddress(EMAIL_USERNAME));
                                    emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                                    emailMessage.setSubject(subject);
                                    emailMessage.setText(message);

                                    Transport.send(emailMessage);
                                    System.out.println(
                                            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                                    emailMessage.setSubject(subject);
                                    emailMessage.setText(message);

                                    Transport.send(emailMessage);


                                    emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                                    emailMessage.setSubject(subject);
                                    emailMessage.setText(m

                                            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                                    emailMessage

                                    emailMessage.setRecipients(Message.RecipientType.TO, I

                                            emailMess
                                            "Email sent successfully.");
                                } catch (MessagingException e) {
                                    System.err.println(
                                            System.err.pr
                                            "Failed to send email: " + e.getMessage());
                                }
                            }


                }
            }
            private void clearFields(TextField toField, TextField subjectField, TextArea messageArea) {
                toField.clear();
                subjectField.clear();
                messageArea.clear();
            }


            toField.clear();
            subjectFi
            public static void main(String[] args) {
                launch(args);
            }
        }

