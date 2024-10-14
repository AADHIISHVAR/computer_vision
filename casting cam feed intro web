import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class MjpegStreamController {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @GetMapping(value = "/mjpeg-stream", produces = "multipart/x-mixed-replace;boundary=frame")
    public void streamMjpeg(HttpServletResponse response) throws IOException {
        // Setup camera capture
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot open the camera");
            return;
        }

        response.setContentType("multipart/x-mixed-replace;boundary=frame");

        // Continuously stream frames
        executor.submit(() -> {
            Mat frame = new Mat();
            while (true) {
                // Read the camera frame
                camera.read(frame);
                if (!frame.empty()) {
                    try {
                        // Convert the frame to JPEG
                        byte[] imageBytes = matToJpeg(frame);
                        response.getOutputStream().write(("--frame\r\n" +
                                "Content-Type: image/jpeg\r\n\r\n").getBytes());
                        response.getOutputStream().write(imageBytes);
                        response.getOutputStream().write("\r\n".getBytes());
                        response.flushBuffer();

                        // Optional: Introduce a small delay to reduce CPU usage
                        Thread.sleep(50);  // Stream at approximately 20 fps
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
            camera.release();
        });
    }

    // Helper method to convert a Mat object to JPEG byte array
    private byte[] matToJpeg(Mat frame) throws IOException {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", frame, matOfByte);
        return matOfByte.toArray();
    }
}



//            http://localhost:8080/mjpeg-stream      for accessing purpose

