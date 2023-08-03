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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Standing extends RepresentationModel<Standing> {

    @Id
    @GeneratedValue(generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", type = UseIdOrGenerate.class)
    private UUID id;
    private Integer matchesPlayed;
    private Integer wins;
    private Integer draws;
    private Integer losses;
    private Integer points;
    @NotNull
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_COMPETITION_ID"))
    private Competition competition;
    @NotNull
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_TEAM_ID"))
    private Team team;

    @PrePersist
    @PreUpdate
    private void setData() {
        points = 0;
        points += wins * 3;
        points += draws;

        matchesPlayed = wins + draws + losses;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !obj.getClass().equals(this.getClass())) return false;

        Standing standing = (Standing) obj;
        return competition.equals(standing.getCompetition()) && team.equals(standing.getTeam());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + competition.hashCode();
        result = 31 * result + team.hashCode();
        return result;
    }
}
