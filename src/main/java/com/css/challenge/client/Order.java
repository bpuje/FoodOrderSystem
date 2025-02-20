package com.css.challenge.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;

/**
 * Order is a json-friendly representation of an order.
 */
public class Order {
  private final String id; // order id
  private final String name; // food name
  private final String temp; // ideal temperature
  private final int freshness; // freshness in seconds
  private Instant placementTime; // time when the order was placed

  public Order(
          @JsonProperty("id") String id,
          @JsonProperty("name") String name,
          @JsonProperty("temp") String temp,
          @JsonProperty("freshness") int freshness) {
    this.id = id;
    this.name = name;
    this.temp = temp;
    this.freshness = freshness;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getTemp() {
    return temp;
  }

  public int getFreshness() {
    return freshness;
  }

  public Instant getPlacementTime() {
    return placementTime;
  }

  public void setPlacementTime(Instant placementTime) {
    this.placementTime = placementTime;
  }

  @Override
  public String toString() {
    return "{id: " + id + ", name: " + name + ", temp: " + temp + ", freshness: " + freshness + " }";
  }

  /**
   * Parses a JSON string into a list of orders.
   *
   * @param json The JSON string to parse.
   * @return A list of orders.
   * @throws JsonProcessingException If the JSON string cannot be parsed.
   */
  public static List<Order> parse(String json) throws JsonProcessingException {
    return new ObjectMapper().readValue(json, new TypeReference<List<Order>>() {});
  }
}
