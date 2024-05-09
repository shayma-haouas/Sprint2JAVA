package com.example.flo.controllers.back;

public class QR {
    public class QRCodeGenerator extends Application {

        @Override
        public void start(Stage primaryStage) {
            // Generate QR code image

            ByteArrayOutputStream outpu

            By
            ByteArrayOutputStream outputStream = QRCode.from("https://example.com").to(ImageType.PNG).stream();

            ByteArrayInput
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            Image image = new Image(inputStream);



        /
// Display QR code image in ImageView
            ImageView imageView = new ImageView(image);

            StackPane root = new StackPane();
            root.getChildren().add(imageView);


            root.getChi
            Scene scene = new Scene(root, 300, 300);
            primaryStage.setTitle(
                    pri
                    "QR Code Generator");
            primaryStage.setScene(scene);
            primaryStage.show();
        }


        primaryStage.setScene(scene);
        public static void main(String[] args) {
            launch(args);
        }

}
