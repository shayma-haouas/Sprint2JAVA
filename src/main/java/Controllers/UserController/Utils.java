package Controllers.UserController;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.ByteArrayInputStream;
public class Utils {





        public static Image mat2Image(Mat frame) {
            int width = frame.width();
            int height = frame.height();
            int channels = frame.channels();
            byte[] buffer = new byte[width * height * channels];
            frame.get(0, 0, buffer);
            WritableImage image = new WritableImage(width, height);
            image.getPixelWriter().setPixels(0, 0, width, height, PixelFormat.getByteRgbInstance(), buffer, 0, width * channels);
            return image;
        }
    }





