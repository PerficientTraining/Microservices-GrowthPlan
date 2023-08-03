package co.com.perficient.project3.model.dto;

import org.springframework.hateoas.Links;

import java.io.Serializable;

public record PlayerDTO(String name, String nationality, String birthDate, String number, String position,
                        String team, Links links) implements Serializable {
}
