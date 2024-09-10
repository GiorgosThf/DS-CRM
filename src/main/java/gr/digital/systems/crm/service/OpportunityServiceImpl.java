package gr.digital.systems.crm.service;

import gr.digital.systems.crm.model.Opportunity;
import gr.digital.systems.crm.repository.OpportunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class OpportunityServiceImpl extends BaseServiceImpl<Opportunity>
		implements OpportunityService {
	private final OpportunityRepository opportunityRepository;

	@Autowired
	public OpportunityServiceImpl(final OpportunityRepository opportunityRepository) {
		this.opportunityRepository = opportunityRepository;
	}

	@Override
	public JpaRepository<Opportunity, Long> getRepository() {
		return this.opportunityRepository;
	}
}
