package gr.digital.systems.crm.controller;

import gr.digital.systems.crm.model.Customer;
import gr.digital.systems.crm.service.CustomerService;
import gr.digital.systems.crm.transfer.ApiResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
	private final CustomerService customerService;

	@Autowired
	public CustomerController(final CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping("/findAll")
	public ResponseEntity<ApiResponse<List<Customer>>> getAllCustomers() {
		return ResponseEntity.ok(
				ApiResponse.<List<Customer>>builder().data(this.customerService.findAll()).build());
	}

	@GetMapping("find/{id}")
	public ResponseEntity<ApiResponse<Customer>> getCustomerById(@PathVariable final Long id) {
		return ResponseEntity.ok(
				ApiResponse.<Customer>builder().data(this.customerService.get(id)).build());
	}

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<Customer>> createCustomer(
			@RequestBody final Customer customer) {
		return ResponseEntity.ok(
				ApiResponse.<Customer>builder().data(this.customerService.create(customer)).build());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse<Customer>> deleteCustomer(@PathVariable final Long id) {
		return ResponseEntity.ok(
				ApiResponse.<Customer>builder().data(this.customerService.deleteById(id)).build());
	}

	@PutMapping("/update")
	public ResponseEntity<ApiResponse<Customer>> updateCustomer(
			@RequestBody final Customer customer) {
		return ResponseEntity.ok(
				ApiResponse.<Customer>builder().data(this.customerService.update(customer)).build());
	}
}
