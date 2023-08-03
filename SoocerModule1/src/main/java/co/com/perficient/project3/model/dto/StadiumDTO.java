package co.com.perficient.project3.model.dto;

import org.springframework.hateoas.Links;

import java.io.Serializable;

public record StadiumDTO(String name, String country, String city, String capacity,
                         Links links) implements Serializable {
}
