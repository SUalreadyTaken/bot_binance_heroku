package su.binance_bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Component;

import su.binance_bot.Model.AllSleep;
import su.binance_bot.Runnable.BinanceChecker;
import su.binance_bot.Runnable.PreventIdling;
import su.binance_bot.Service.IdleService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

import static java.lang.Thread.*;

@Component
public class Switch {

  @Value("${heroku.alternative.website}")
  private String herokuAlternativeWebsite;

  @Value("${switchapp}")
  private boolean switchApp;

  int tryCount = 10;
  int sleepBetween = 30000;
  private final ScheduledAnnotationBeanPostProcessor postProcessor;

  private final PreventIdling preventIdling;
  private final IdleService idleService;
  private final BinanceChecker binanceChecker;
  private final AllSleep allSleep;

  private static final String binanceCheckerTask = "binanceChecker";
  private static final String preventIdlingTask = "preventIdling";

  private final Init init;

  private final ObjectMapper objectMapper;

  @Autowired
  public Switch(ScheduledAnnotationBeanPostProcessor postProcessor, BinanceChecker binanceChecker,
      PreventIdling preventIdling, IdleService idleService, Init init, ObjectMapper objectMapper, AllSleep allSleep) {
    this.allSleep = allSleep;
    this.postProcessor = postProcessor;
    this.binanceChecker = binanceChecker;
    this.preventIdling = preventIdling;
    this.idleService = idleService;
    this.init = init;
    this.objectMapper = objectMapper;
  }

  private void stopSchedule() {
    postProcessor.postProcessBeforeDestruction(binanceChecker, binanceCheckerTask);
    postProcessor.postProcessBeforeDestruction(preventIdling, preventIdlingTask);
  }

  private void startSchedule() {
    postProcessor.postProcessAfterInitialization(binanceChecker, binanceCheckerTask);
    postProcessor.postProcessAfterInitialization(preventIdling, preventIdlingTask);
  }

  public String listSchedules() throws JsonProcessingException {
    Set<ScheduledTask> setTasks = postProcessor.getScheduledTasks();
    if (!setTasks.isEmpty()) {
      return objectMapper.writeValueAsString(setTasks);
    } else {
      return "No running tasks !";
    }
  }

  @Scheduled(cron = "0 0 4 * * WED")
  private void switchApp() throws InterruptedException, IOException {
    if (switchApp) {
      // stop alertbot
      this.init.registerOrStop();
      idleService.switchAlternativeBoolean();
      stopSchedule();
      allSleep.setIsSleep(true);

      System.out.println("trying to wake up alternative");
      // try to wake the sleeping alternative
      for (int i = 0; i < tryCount; i++) {
        URL u = new URL(herokuAlternativeWebsite);
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        connection.setRequestMethod("HEAD");
        connection.disconnect();
        // alternative app woke up
        if (connection.getResponseCode() == 200) {
          System.out.println("Alternative woke up.. heroku will soon put me to sleep");
          return;
        }
        sleep(sleepBetween);
      }

      // if it got this far then the alternative app failed to wake up
      System.out.println("alternative didn't wake up.. start up again");
      idleService.switchAlternativeBoolean();
      this.init.registerOrStop();
      startSchedule();
      allSleep.setIsSleep(false);
    }
  }

}