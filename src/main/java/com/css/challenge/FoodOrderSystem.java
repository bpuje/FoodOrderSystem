package com.css.challenge;

import com.css.challenge.client.*;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "challenge", showDefaultValues = true)
public class FoodOrderSystem implements Runnable {
  private static final Logger LOGGER = LoggerFactory.getLogger(FoodOrderSystem.class);

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

      StorageSystem storageSystem = new StorageSystem();
      List<Action> actions = new ArrayList<>();
      Random random = new Random();

      // Start a background thread to optimize storage periodically
      Thread storageOptimizer = new Thread(() -> {
        while (true) {
          try {
            Thread.sleep(1000); // Optimize storage every second
            storageSystem.optimizeStorage(actions);
          } catch (InterruptedException e) {
            LOGGER.error("Storage optimization interrupted: {}", e.getMessage());
          }
        }
      });
      storageOptimizer.setDaemon(true); // Daemon thread to stop when FoodOrderSystem thread exits
      storageOptimizer.start();

      for (Order order : problem.getOrders()) {
        LOGGER.info("Received: {}", order);

        Instant placementTime = Instant.now();
        storageSystem.placeOrder(order, actions);
        LOGGER.info("Placed order: id={}, temp={}, placementTime={}", order.getId(), order.getTemp(), placementTime);

        Thread.sleep(rate.toMillis());

        // Schedule pickup
        long pickupDelay = min.toMillis() + (long) (random.nextDouble() * (max.toMillis() - min.toMillis()));
        Instant pickupTime = placementTime.plusMillis(pickupDelay);

        new Thread(() -> {
          try {
            long delay = Duration.between(Instant.now(), pickupTime).toMillis();
            if (delay > 0) {
              Thread.sleep(delay);
            }
            storageSystem.pickupOrder(order.getId(), actions);
            LOGGER.info("Picked up order: id={}, pickupTime={}", order.getId(), Instant.now());
          } catch (InterruptedException e) {
            LOGGER.error("Pickup failed: {}", e.getMessage());
          }
        }).start();
      }

      // Wait for all pickups to complete
      Thread.sleep(max.toMillis());

      String result = client.solveProblem(problem.getTestId(), rate, min, max, actions);
      LOGGER.info("Result: {}", result);

      String actionsJson = ActionFormatter.formatActions(actions);
      System.out.println("Actions JSON:\n" + actionsJson);

    } catch (IOException | InterruptedException e) {
      LOGGER.error("Simulation failed: {}", e.getMessage());
    }
  }

  public static void main(String[] args) {
    new CommandLine(new FoodOrderSystem()).execute(args);
  }
}
