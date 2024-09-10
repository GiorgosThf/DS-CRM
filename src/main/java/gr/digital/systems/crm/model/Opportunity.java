package gr.digital.systems.crm.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Opportunity extends BaseEntity {
	private String description;
	private double value;
	private String status;
	@ManyToOne private Customer customer;
}
