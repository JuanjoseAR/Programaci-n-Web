package com.unimag.web.api.dto;

import com.unimag.web.domain.Passenger;

import java.io.Serializable;

public class PassengerDto {

    public record PassengerCreateRequest(
            String fullname,
            String email,
            PassengerProfile profile
    ) implements Serializable{}

    public record PassengerProfile(
            Passenger passenger
    ) implements Serializable{}

    public record PassengerUpdateRequest(
            String fullname,
            String email,
            PassengerProfile profile
    ) implements Serializable{}

    public record PassengerResponse(
            String email,
            Passenger profile
    ) implements Serializable{}
}