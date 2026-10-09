package com.sdet.assessment.mobile.driver;

import com.sdet.assessment.mobile.config.ConfigReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;

public final class AppiumDriverManager {

    private static final Logger log = LoggerFactory.getLogger(AppiumDriverManager.class);

    private static final ThreadLocal<AndroidDriver> DRIVER = new ThreadLocal<>();

    private AppiumDriverManager() {
    }

    public static void start() {
        String serverUrl = ConfigReader.get("appium.server.url");
        String deviceName = ConfigReader.get("appium.device.name");
        String apkPath = resolveApkPath(ConfigReader.get("appium.apk.path"));

        UiAutomator2Options options = new UiAutomator2Options();
        options.setCapability("platformName", "Android");
        options.setCapability("appium:automationName", "UiAutomator2");
        options.setCapability("appium:deviceName", deviceName);
        options.setCapability("appium:app", apkPath);
        options.setCapability("appium:autoGrantPermissions", true);
        options.setCapability("appium:newCommandTimeout", 120);
        options.setCapability("appium:noReset", true);
        options.setCapability("appium:autoLaunch", false);
        options.setCapability("appium:chromedriverAutodownload", true);

        log.info("Starting AndroidDriver (device={}, app={})", deviceName, apkPath);
        try {
            URL url = URI.create(serverUrl).toURL();
            DRIVER.set(new AndroidDriver(url, options));
        } catch (Exception e) {
            throw new IllegalStateException("Could not start Appium session at " + serverUrl, e);
        }
    }

    public static AndroidDriver get() {
        return DRIVER.get();
    }

    public static void quit() {
        AndroidDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
        }
        DRIVER.remove();
    }

    private static String resolveApkPath(String resourcePath) {
        try {
            URL url = AppiumDriverManager.class.getClassLoader().getResource(resourcePath);
            if (url == null) {
                throw new IllegalStateException("APK resource not found on classpath: " + resourcePath);
            }
            return Paths.get(url.toURI()).toAbsolutePath().toString();
        } catch (Exception e) {
            throw new IllegalStateException("Could not resolve APK path: " + resourcePath, e);
        }
    }
}
