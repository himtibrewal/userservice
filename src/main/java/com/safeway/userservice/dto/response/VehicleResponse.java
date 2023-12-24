package com.safeway.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VehicleResponse {

    private String image;

    private String type;

    private String brand;

    private String model;

    @JsonProperty("registration_no")
    private String registrationNo;

    public VehicleResponse(String type, String brand, String model, String registrationNo) {
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.registrationNo = registrationNo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }
}
