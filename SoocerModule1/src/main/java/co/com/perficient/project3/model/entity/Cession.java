package co.com.perficient.project3.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Cession {

    @Id
    private Long id;
    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_PLAYER_ID"))
    private Player player;
    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_OWNER_TEAM_ID"))
    private Team ownerTeam;
    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_TEMPORAL_TEAM_ID"))
    private Team temporalTeam;
}
