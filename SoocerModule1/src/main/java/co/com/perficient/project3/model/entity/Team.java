package co.com.perficient.project3.model.entity;

import co.com.perficient.project3.utils.UseIdOrGenerate;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team extends RepresentationModel<Team> {

    @Id
    @GeneratedValue(generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", type = UseIdOrGenerate.class)
    private UUID id;
    private String name;
    private String country;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_STADIUM_ID"))
    private Stadium stadium;
    @OneToOne(mappedBy = "team")
    private President president;
    @OneToOne(mappedBy = "team")
    private Coach coach;
    @OneToMany(mappedBy = "team")
    private Set<Player> players;
    @OneToMany(mappedBy = "team")
    private Set<Standing> standings;

    @PrePersist
    @PreUpdate
    private void setCountry() {
        if (Objects.nonNull(stadium)) {
            country = stadium.getCountry();
        }
    }
}
