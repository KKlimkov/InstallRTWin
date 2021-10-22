package com.kklimkovtests;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.io.IOException;

@Owner("KKlimkov")
@Layer("install")
@Feature("InstallWin")

@TestMethodOrder(OrderAnnotation.class)
class TestInstallRTWIN {

        @BeforeAll
        static void PathForm() throws IOException, InterruptedException {
            InstallSteps.PathFile(System.getProperty("Brunch") + "MasterSCADA4DRT_" + System.getProperty("BitMode").substring(4) + "_DEMO.exe");
        }

        @DisplayName("Download RT from FTP")
        @Test
        @EnabledIfSystemProperty(named = "DownloadFile", matches = "True")
        @Story("Install MS4D RT")
        @Tags({@Tag("Install"), @Tag("Win")})
        @Order(1)
        public void Check () throws IOException, InterruptedException {

        String FtpUrl = null;
        if (System.getProperty("Brunch").equals("1.2")) {
            FtpUrl =
                    "ftp://ftpGuestDemo:8AA55D8A@support.insat.ru/MasterSCADA4D/1.2/MasterSCADA4DRT_" +
                            System.getProperty("BitMode").substring(4) + "_DEMO.exe";
        } else if (System.getProperty("Brunch").equals("RC")) {
            FtpUrl =
                    "ftp://ftpGuestSupport:21B74F6E@support.insat.ru/Dev/MasterSCADA4D/1.2.RC/MasterSCADA4DRT_" +
                            System.getProperty("BitMode").substring(4) + "_DEMO.exe";
        } else if (System.getProperty("Brunch").equals("Beta")) {
            FtpUrl =
                    "ftp://ftpGuestSupport:21B74F6E@support.insat.ru/Dev/MasterSCADA4D/Beta/MasterSCADA4DRT_" +
                            System.getProperty("BitMode").substring(4) + "_DEMO.exe";
        }

        InstallSteps.CreateFile();
        InstallSteps.DownloadFile(FtpUrl);
        InstallSteps.CheckFile();
    }

    @DisplayName("Uninstall old version")
    @Test
    @Story("Install MS4D RT")
    @Tags({@Tag("Install"),@Tag("Win"),@Tag("Del")})
    @Order(2)
    public void Del() throws InterruptedException {
      InstallSteps.LaunchFile();
      InstallSteps.LaunchRoot();
      InstallSteps.InstallClick("Да", 100);
    }

    @DisplayName("Install new version")
    @Test
    @Story("Install MS4D RT")
    @Tags({@Tag("Install"),@Tag("Win")})
    @Order(3)
    public void Install() throws InterruptedException {
       InstallSteps.FindElement("Установка ",100);
       InstallSteps.InstallClick("Вперед",100);
       InstallSteps.InstallClick("Я принимаю условия лицензионного соглашения",100);
       InstallSteps.InstallClick("Вперед",100);
       InstallSteps.InstallClick("Вперед",100);
       InstallSteps.InstallClick("Завершить",100);
    }

}


