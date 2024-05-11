package Controllers.UserController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class FacialeController {

    @FXML
    private Button cameraView;

    private VideoCapture capture;

    public FacialeController() {
        // Chargement de la bibliothèque OpenCV
        String libraryPath = "C:/Users/siwar/Downloads/opencv/build/java/x64";
        System.setProperty("java.library.path", libraryPath);
       // System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void startCamera() {
        capture = new VideoCapture(0); // Ouvre la webcam par défaut

        if (capture.isOpened()) {
            // Démarrage de la capture vidéo
            new Thread(() -> {
                Mat frame = new Mat();
                while (true) {
                    if (capture.read(frame)) {
                        // Conversion du cadre OpenCV en image JavaFX
                        Image imageToShow = Utils.mat2Image(frame);
                        if (imageToShow != null) {
                            // Affichage de l'image dans l'interface utilisateur
                            Image scaledImage = scaleImage(imageToShow, 100, 100); // Réduire la taille de l'image si nécessaire
                            ImageView imageView = new ImageView(scaledImage);
                            cameraView.setGraphic(imageView); // Définir l'image sur le bouton
                        }
                    }
                }
            }).start();
        }
    }

    public void stopCamera() {
        if (capture != null && capture.isOpened()) {
            // Arrêt de la capture vidéo
            capture.release();
        }
    }

    // Méthode pour démarrer la caméra depuis l'interface utilisateur
    @FXML
    public void startCameraFromUI() {
        startCamera();
    }

    // Méthode pour arrêter la caméra depuis l'interface utilisateur
    @FXML
    public void stopCameraFromUI() {
        stopCamera();
    }

    // Méthode pour redimensionner une image
    private Image scaleImage(Image image, int width, int height) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView.snapshot(null, null);
    }
}
