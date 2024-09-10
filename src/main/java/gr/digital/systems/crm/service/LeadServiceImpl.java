package gr.digital.systems.crm.service;

import gr.digital.systems.crm.model.Lead;
import gr.digital.systems.crm.repository.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LeadServiceImpl extends BaseServiceImpl<Lead> implements LeadService {
	private final LeadRepository leadRepository;

	@Autowired
	public LeadServiceImpl(final LeadRepository leadRepository) {
		this.leadRepository = leadRepository;
	}

	@Override
	public JpaRepository<Lead, Long> getRepository() {
		return this.leadRepository;
	}
}
