package co.com.perficient.project3.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
public class President extends Person {

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_TEAM_ID"))
    private Team team;
}
