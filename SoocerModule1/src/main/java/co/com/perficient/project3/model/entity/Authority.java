package co.com.perficient.project3.model.entity;

import co.com.perficient.project3.utils.UseIdOrGenerate;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Authority {

    @Id
    @GeneratedValue(generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", type = UseIdOrGenerate.class)
    private UUID id;
    private String name;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_USER_P3_ID"))
    private UserP3 userP3;
}
