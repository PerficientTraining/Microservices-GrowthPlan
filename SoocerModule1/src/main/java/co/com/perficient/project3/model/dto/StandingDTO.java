package co.com.perficient.project3.model.dto;

import org.springframework.hateoas.Links;

import java.io.Serializable;

public record StandingDTO(Integer matchesPlayed, Integer wins, Integer draws, Integer losses,
                          Integer points, String competition, String team, Links links) implements Serializable {
}
