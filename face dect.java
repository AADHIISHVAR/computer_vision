import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.highgui.HighGui;

public class Main {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        // Load Haar Cascade for face detection
        CascadeClassifier faceCascade = new CascadeClassifier("C:\\Users\\Admin\\Downloads\\opencv\\sources\\data\\haarcascades_cuda\\haarcascade_frontalface_default.xml");

        // Open the webcam (0 = default camera)
        VideoCapture camera = new VideoCapture(0);

        if (!camera.isOpened()) {
            System.out.println("Error: Camera is not accessible.");
            return;
        }

        Mat frame = new Mat();
        Mat grayFrame = new Mat();

        while (true) {
            // Capture a frame
            if (camera.read(frame)) {
                // Convert the frame to grayscale
                //Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);

                // Detect faces in the grayscale frame
                MatOfRect faceDetections = new MatOfRect();
                faceCascade.detectMultiScale(frame, faceDetections);

                // Draw green rectangles around detected faces
                for (Rect rect : faceDetections.toArray()) {
                    Imgproc.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 2);
                }

                // Display the frame with face detections
                HighGui.imshow("Face Detection", frame);

                // Break the loop if 'q' is pressed
                if (HighGui.waitKey(30) == 'q') {
                    break;
                }
            }
        }

        camera.release();  // Release the camera
        HighGui.destroyAllWindows(); // Close any open windows
    }
}
