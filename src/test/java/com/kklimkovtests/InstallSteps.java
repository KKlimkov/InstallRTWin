package com.kklimkovtests;
import io.appium.java_client.windows.WindowsDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class InstallSteps {

    public static String command;
    public static String path;
    public static File file;
    public static Boolean Ex = false;
    public static WindowsDriver driver1 = null;
    public static WindowsDriver driver2 = null;


    @Step("Создание файла")
    public static void CreateFile(String FileName) throws InterruptedException {
        path = "C:\\Users\\Public\\Autotests\\"+FileName;
        file = new File(path);
    }

    @Step("Загрузка файла")
    public static void DownloadFile(String UrlFTP) throws InterruptedException, IOException {

        command = "cmd /c curl.exe -o "+path+" "+ UrlFTP;
        String line = "";

        Process p1 = Runtime.getRuntime().exec(command);

        InputStream stdlog = p1.getErrorStream ();
        InputStream stdout = p1.getInputStream ();

        BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
        BufferedReader error = new BufferedReader (new InputStreamReader(stdlog));


        new Thread() {
            public void run() {
                try {
                    String log;

                    while ((log = error.readLine ()) != null) {
                        System.out.println ("[Stdlog] " + log);
                        if (log.contains("Could not resolve host") || log.contains("Recv failure") )
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
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        System.out.println (Ex);

    }

    @Step("Проверка файла")
    public static void CheckFile() throws InterruptedException {
        assertFalse(Ex);
        Boolean Exs =  file.exists();
        assertTrue(Exs);
    }

    @Step("Запуск инсталлятора на удаление")
    public static void LaunchFile() throws InterruptedException {
        DesiredCapabilities cap1 = new DesiredCapabilities();
        cap1.setCapability("platformName", "Windows");
        cap1.setCapability("deviceName", "WindowsPC");
        cap1.setCapability("app", path);
        cap1.setCapability("ms:waitForAppLaunch", "10");
        try {
            driver1 = new WindowsDriver(new URL("http://127.0.0.1:4723"), cap1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Step("Запуск драйвера root")
    public static void LaunchRoot() throws InterruptedException {
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
    }
    @Step("Удаление MS4D RT")
    public static void DelMs4d(Integer WaitingTime) throws InterruptedException {
        Boolean Exs = false;
        if (driver2.findElementByName("Да").isDisplayed()) Exs = true;
        assertTrue(Exs);
        driver2.findElementByName("Да").click();
        ;
        try {
            Thread.sleep(WaitingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Boolean Exs2 = false;
        if ( driver2.findElementByName("Вперед").isDisplayed()) Exs2 = true;
        assertTrue(Exs2);
    }


    @Step("Установка. Нажатие кнопки")
    public static void InstallClick(String Name, Integer WaitingTime) throws InterruptedException {

        driver2.findElementByName(Name).click();
        try {
            Thread.sleep(WaitingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
