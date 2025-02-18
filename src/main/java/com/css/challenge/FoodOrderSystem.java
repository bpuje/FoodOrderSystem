package com.css.challenge;

import com.css.challenge.client.*;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "challenge", showDefaultValues = true)
public class FoodOrderSystem implements Runnable {
  private static final Logger LOGGER = LoggerFactory.getLogger(FoodOrderSystem.class);
  private final Storage storage = new Storage();
  private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

  static {
    org.apache.log4j.Logger.getRootLogger().setLevel(Level.OFF);
    System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT: %5$s %n");
  }

  @Option(names = "--endpoint", description = "Problem server endpoint")
  String endpoint = "https://api.cloudkitchens.com";

  @Option(names = "--auth", description = "Authentication token (required)")
  String auth = "";

  @Option(names = "--name", description = "Problem name. Leave blank (optional)")
  String name = "";

  @Option(names = "--seed", description = "Problem seed (random if zero)")
  long seed = 0;

  @Option(names = "--rate", description = "Inverse order rate")
  Duration rate = Duration.ofMillis(500);

  @Option(names = "--min", description = "Minimum pickup time")
  Duration min = Duration.ofSeconds(4);

  @Option(names = "--max", description = "Maximum pickup time")
  Duration max = Duration.ofSeconds(8);

  @Override
  public void run() {
    try {
      Client client = new Client(endpoint, auth);
      Problem problem = client.newProblem(name, seed);

      // ------ Simulation harness logic goes here using rate, min and max ----

      List<Action> actions = new ArrayList<>();
      for (Order order : problem.getOrders()) {
        LOGGER.info("Received: {}", order);

        storage.placeOrder(order);

        actions.add(new Action(Instant.now(), order.getId(), Action.PLACE));
        Thread.sleep(rate.toMillis());
      }

      executor.scheduleAtFixedRate(() -> {
        storage.processOrders();
        storage.removeExpiredOrders();
      }, 0, 500, TimeUnit.MILLISECONDS);
//      executor.scheduleAtFixedRate(storage::moveOrders, 0, 1, TimeUnit.SECONDS);

      // ----------------------------------------------------------------------

      String result = client.solveProblem(problem.getTestId(), rate, min, max, actions);
      LOGGER.info("Result: {}", result);

    } catch (IOException | InterruptedException e) {
      LOGGER.error("Simulation failed: {}", e.getMessage());
    } finally {
      executor.shutdown();
    }
  }

  public static void main(String[] args) {
    new CommandLine(new FoodOrderSystem()).execute(args);
    long timestamp = Instant.now().getEpochSecond();
    System.out.println("Timestamp: " + timestamp);
  }
}
