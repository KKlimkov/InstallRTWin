import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class TestInstallRTWIN {

    File file = new File("C:\\Users\\kiril\\Desktop\\Autotests\\InstallDirectory\\MasterSCADA4DRT_x64_DEMO.exe");
    Boolean Ex = false;
    @DisplayName("Download RT from FTP")
    @Test
    @Tag("DownloadFTP")
    @Order(1)
    public void Check() throws IOException {

        String command = "cmd /c curl.exe -o C:\\Users\\kiril\\Desktop\\Autotests\\InstallDirectory\\MasterSCADA4DRT_x64_DEMO.exe ftp://ftpGuestDemo:8AA55D8A@support.insat.ru/MasterSCADA4D/1.2/MasterSCADA4DRT_x64_DEMO.exe ";
        String line = "";


        Process p1 = Runtime.getRuntime().exec(command);
        InputStream stderr = p1.getErrorStream ();
        InputStream stdout = p1.getInputStream ();

        BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
        BufferedReader error = new BufferedReader (new InputStreamReader(stderr));


        new Thread() {
            public void run() {
                try {
                    String err;

                    while ((err = error.readLine ()) != null) {
                        System.out.println ("[Stderr] " + err);
                        if (err.contains("Could not resolve host") || err.contains("Recv failure") )
                        { Ex = true; }
                        else
                        { Ex = false;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }}.start();

        while ((line = reader.readLine ()) != null)
            System.out.println ("[Stdout] " + line);
        try {
            System.out.println(file.getCanonicalPath() + " " + file.exists());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println (Ex);
        assertFalse(Ex);
        Boolean Exs =  file.exists();
        assertTrue(Exs);
    }

    public static WindowsDriver driver1 = null;
    public static WindowsDriver driver2 = null;

    //@BeforeEach

    @DisplayName("Uninstall old version")
    @Test
    @Tag("Uninstall")
    @Order(2)
    public void setUp() {

        DesiredCapabilities cap1 = new DesiredCapabilities();
        cap1.setCapability("platformName", "Windows");
        cap1.setCapability("deviceName", "WindowsPC");
        cap1.setCapability("app", "C:\\Users\\kiril\\Desktop\\Autotests\\InstallDirectory\\MasterSCADA4DRT_x64_DEMO.exe");
        cap1.setCapability("ms:waitForAppLaunch", "10");
        try {
            driver1 = new WindowsDriver(new URL("http://127.0.0.1:4723"), cap1);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName", "Windows");
        cap.setCapability("deviceName", "WindowsPC");
        cap.setCapability("app", "Root");
        cap.setCapability("ms:waitForAppLaunch", "10");
        try {
            driver2 = new WindowsDriver(new URL("http://127.0.0.1:4723"), cap);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    //}
    //    public void Del() {
        Boolean Exs = false;
        if (driver2.findElementByName("Да").isDisplayed()) Exs = true;

        assertTrue(Exs);

        driver2.findElementByName("Да").click();
        ;
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Boolean Exs2 = false;
        if ( driver2.findElementByName("Вперед").isDisplayed()) Exs2 = true;

        assertTrue(Exs2);

    }

    @DisplayName("Install new version")
    @Test
    @Tag("Install")
    @Order(3)
    public void Install() {

        driver2.findElementByName("Вперед").click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver2.findElementByName("Я принимаю условия лицензионного соглашения").click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver2.findElementByName("Вперед").click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver2.findElementByName("Вперед").click();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver2.findElementByName("Завершить").click();
    }
}


