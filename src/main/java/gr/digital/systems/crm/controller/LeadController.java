package gr.digital.systems.crm.controller;

import gr.digital.systems.crm.model.Lead;
import gr.digital.systems.crm.service.LeadService;
import gr.digital.systems.crm.transfer.ApiResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/leads")
public class LeadController {
	private final LeadService leadService;

	@Autowired
	public LeadController(final LeadService leadService) {
		this.leadService = leadService;
	}

	@GetMapping("/findAll")
	public ResponseEntity<ApiResponse<List<Lead>>> getAllLeads() {
		return ResponseEntity.ok(
				ApiResponse.<List<Lead>>builder().data(this.leadService.findAll()).build());
	}

	@GetMapping("find/{id}")
	public ResponseEntity<ApiResponse<Lead>> getLeadById(@PathVariable final Long id) {
		return ResponseEntity.ok(ApiResponse.<Lead>builder().data(this.leadService.get(id)).build());
	}

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<Lead>> createLead(@RequestBody final Lead lead) {
		return ResponseEntity.ok(
				ApiResponse.<Lead>builder().data(this.leadService.create(lead)).build());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse<Lead>> deleteLead(@PathVariable final Long id) {
		return ResponseEntity.ok(
				ApiResponse.<Lead>builder().data(this.leadService.deleteById(id)).build());
	}

	@PutMapping("/update")
	public ResponseEntity<ApiResponse<Lead>> updateLead(@RequestBody final Lead lead) {
		return ResponseEntity.ok(
				ApiResponse.<Lead>builder().data(this.leadService.update(lead)).build());
	}
}
