package gr.digital.systems.crm.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Lead extends BaseEntity {
	private String name;
	private String email;
	private String phone;
	private String source;
}
