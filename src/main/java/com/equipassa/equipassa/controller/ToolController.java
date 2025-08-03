package com.equipassa.equipassa.controller;

import com.equipassa.equipassa.dto.ToolRequest;
import com.equipassa.equipassa.dto.ToolResponse;
import com.equipassa.equipassa.model.Organization;
import com.equipassa.equipassa.repository.OrganizationRepository;
import com.equipassa.equipassa.security.CurrentUser;
import com.equipassa.equipassa.security.CustomUserDetails;
import com.equipassa.equipassa.service.ToolService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/tools")
public class ToolController {

    private final ToolService toolService;
    private final OrganizationRepository organizationRepository;

    public ToolController(final ToolService toolService, final OrganizationRepository organizationRepository) {
        this.toolService = toolService;
        this.organizationRepository = organizationRepository;
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'ORG_ADMIN')")
    public ResponseEntity<ToolResponse> createTool(
            @CurrentUser final CustomUserDetails userDetails,
            @RequestPart("toolRequest") @Valid final ToolRequest toolRequest,
            @RequestPart(value = "files", required = false) final List<MultipartFile> files
    ) throws BadRequestException {
        final Long organizationId = userDetails.getOrganizationId();
        final Organization organization = organizationRepository.getReferenceById(organizationId);
        final ToolResponse toolResponse = toolService.createTool(toolRequest, organization, files);
        return ResponseEntity.ok(toolResponse);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORG_ADMIN')")
    public ResponseEntity<List<ToolResponse>> getTools() {
        final List<ToolResponse> toolResponses = toolService.getTools();
        return ResponseEntity.ok(toolResponses);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORG_ADMIN')")
    public ResponseEntity<ToolResponse> getToolById(@PathVariable final Long id) {
        final ToolResponse toolResponse = toolService.getToolById(id);
        return ResponseEntity.ok(toolResponse);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasAnyRole('ADMIN', 'ORG_ADMIN')")
    public ResponseEntity<ToolResponse> updateTool(
            @PathVariable final Long id,
            @RequestPart("toolRequest") @Valid final ToolRequest toolRequest,
            @RequestPart(value = "files", required = false) final List<MultipartFile> files
    ) throws BadRequestException {
        final ToolResponse toolResponse = toolService.updateTool(id, toolRequest, files);
        return ResponseEntity.ok(toolResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORG_ADMIN')")
    public ResponseEntity<Void> deleteTool(@PathVariable final Long id) {
        toolService.deleteTool(id);
        return ResponseEntity.noContent().build();
    }
}
