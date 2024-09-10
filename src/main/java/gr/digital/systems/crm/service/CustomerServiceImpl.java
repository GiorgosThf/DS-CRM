package gr.digital.systems.crm.service;

import gr.digital.systems.crm.model.Customer;
import gr.digital.systems.crm.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer> implements CustomerService {

	private final CustomerRepository customerRepository;

	@Autowired
	public CustomerServiceImpl(final CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public JpaRepository<Customer, Long> getRepository() {
		return this.customerRepository;
	}
}
