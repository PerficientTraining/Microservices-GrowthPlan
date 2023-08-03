package co.com.perficient.project3.model.dto;

import java.io.Serializable;

public record MatchDTO(String date, String stadium, String round, String score, String homeTeam,
                       String awayTeam) implements Serializable {
}
