package co.com.perficient.project3.model.entity;

import co.com.perficient.project3.utils.UseIdOrGenerate;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
public class Match {

    @Id
    @GeneratedValue(generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", type = UseIdOrGenerate.class)
    private UUID id;
    @Past
    private LocalDate date;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_STADIUM_ID"))
    private Stadium stadium;
    private String round;
    @NotNull
    @Pattern(regexp = "^\\d-\\d$", message = "must match [0-9]-[0-9]")
    private String score;
    @NotNull
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_HOME_TEAM_ID"))
    private Team homeTeam;
    @NotNull
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_AWAY_TEAM_ID"))
    private Team awayTeam;

    @PrePersist
    @PreUpdate
    private void setStadium() {
        if (Objects.isNull(stadium) && Objects.nonNull(homeTeam)) {
            stadium = homeTeam.getStadium();
        }
    }
}
