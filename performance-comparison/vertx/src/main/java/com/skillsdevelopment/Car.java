package com.skillsdevelopment;

public class Car {
    private final String brand;
    private final String model;
    private final int numberOfSeats;
    private final int numberOfDoors;
    private final int weightInKg;


    Car(final String brand, final String model, final int numberOfSeats, final int numberOfDoors, final int weightInKg) {
        this.brand = brand;
        this.model = model;
        this.numberOfSeats = numberOfSeats;
        this.numberOfDoors = numberOfDoors;
        this.weightInKg = weightInKg;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    public int getWeightInKg() {
        return weightInKg;
    }
}
