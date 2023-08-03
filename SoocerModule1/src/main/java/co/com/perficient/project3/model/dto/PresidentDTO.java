package co.com.perficient.project3.model.dto;

import org.springframework.hateoas.Links;

import java.io.Serializable;

public record PresidentDTO(String name, String nationality, String birthDate, String team,
                           Links links) implements Serializable {
}
