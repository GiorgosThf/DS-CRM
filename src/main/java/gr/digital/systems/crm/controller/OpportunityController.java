package gr.digital.systems.crm.controller;

import gr.digital.systems.crm.model.Opportunity;
import gr.digital.systems.crm.service.OpportunityService;
import gr.digital.systems.crm.transfer.ApiResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/opportunities")
public class OpportunityController {
	private final OpportunityService opportunityService;

	@Autowired
	public OpportunityController(final OpportunityService opportunityService) {
		this.opportunityService = opportunityService;
	}

	@GetMapping("/findAll")
	public ResponseEntity<ApiResponse<List<Opportunity>>> getAllOpportunities() {
		return ResponseEntity.ok(
				ApiResponse.<List<Opportunity>>builder().data(this.opportunityService.findAll()).build());
	}

	@GetMapping("find/{id}")
	public ResponseEntity<ApiResponse<Opportunity>> getOpportunityById(@PathVariable final Long id) {
		return ResponseEntity.ok(
				ApiResponse.<Opportunity>builder().data(this.opportunityService.get(id)).build());
	}

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<Opportunity>> createOpportunity(
			@RequestBody final Opportunity opportunity) {
		return ResponseEntity.ok(
				ApiResponse.<Opportunity>builder()
						.data(this.opportunityService.create(opportunity))
						.build());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse<Opportunity>> deleteOpportunity(@PathVariable final Long id) {
		return ResponseEntity.ok(
				ApiResponse.<Opportunity>builder().data(this.opportunityService.deleteById(id)).build());
	}

	@PutMapping("/update")
	public ResponseEntity<ApiResponse<Opportunity>> updateOpportunity(
			@RequestBody final Opportunity opportunity) {
		return ResponseEntity.ok(
				ApiResponse.<Opportunity>builder()
						.data(this.opportunityService.update(opportunity))
						.build());
	}
}
