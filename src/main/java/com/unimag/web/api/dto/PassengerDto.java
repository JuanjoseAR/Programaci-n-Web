package com.unimag.web.api.dto;

import com.unimag.web.domain.Passenger;

import java.io.Serializable;

public class PassengerDto {

    public record PassengerCreateRequest(
            String fullname,
            String email,
            PassengerProfileDto profile
    ) implements Serializable{}

    public record PassengerProfileDto(
            String phone, String countryCode
    ) implements Serializable{}

    public record PassengerUpdateRequest(
            String fullname,
            String email,
            PassengerProfileDto profile
    ) implements Serializable{}

    public record PassengerResponse(
            Long id,
            String fullname,
            String email,
            PassengerProfileDto profile
    ) implements Serializable{}
}