package co.com.perficient.project3.model.entity;

import co.com.perficient.project3.utils.UseIdOrGenerate;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
public class UserP3 {

    @Id
    @GeneratedValue(generator = "myGenerator")
    @GenericGenerator(name = "myGenerator", type = UseIdOrGenerate.class)
    private UUID id;
    @NotBlank
    private String username;
    @Email
    private String email;
    private String password;
    @OneToMany(mappedBy = "userP3", fetch = FetchType.EAGER)
    private Set<Authority> authorities;
}
