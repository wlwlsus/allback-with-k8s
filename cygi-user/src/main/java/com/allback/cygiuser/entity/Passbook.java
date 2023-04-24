package com.allback.cygiuser.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "passbook")
public class Passbook {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "passbook_id", nullable = false, columnDefinition = "BIGINT UNSIGNED")
	private Long passbookId;

	@Column(name = "cash", length = 20, nullable = false)
	private Long cash;

	@Column(name = "account_number", nullable = false)
	private String accountNumber;
}
