package co.com.perficient.project3.model.dto;

import org.springframework.hateoas.Links;

import java.io.Serializable;

public record TeamDTO(String name, String country, String stadium, String president,
                      String coach, Links links) implements Serializable {
}
