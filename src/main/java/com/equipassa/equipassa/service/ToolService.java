package com.equipassa.equipassa.service;

import com.equipassa.equipassa.dto.ToolRequest;
import com.equipassa.equipassa.dto.ToolResponse;
import com.equipassa.equipassa.model.Organization;
import com.equipassa.equipassa.model.Tool;
import com.equipassa.equipassa.model.ToolImage;
import com.equipassa.equipassa.repository.ToolRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Service
public class ToolService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ToolService.class);

    private final ToolRepository toolRepository;
    private final S3Service s3Service;

    public ToolService(final ToolRepository toolRepository, final S3Service s3Service) {
        this.toolRepository = toolRepository;
        this.s3Service = s3Service;
    }

    @Transactional
    public List<ToolResponse> getTools() {
        return toolRepository.findAll()
                .stream()
                .map(this::toToolResponse)
                .toList();
    }

    public List<ToolResponse> getToolsByOrganization(final Long organizationId) {
        return toolRepository.findByOrganizationId(organizationId)
                .stream()
                .map(this::toToolResponse)
                .toList();
    }

    public ToolResponse getToolById(final Long id) {
        final Tool tool = toolRepository.findById(id).orElseThrow(() -> new RuntimeException("Tool not found"));
        return toToolResponse(tool);
    }

    @Transactional
    public ToolResponse createTool(final ToolRequest toolRequest, final Organization organization, final List<MultipartFile> files) throws BadRequestException {
        if (files != null && files.size() > 3) {
            throw new BadRequestException("Maximum 3 images allowed");
        }

        final Tool tool = mapToTool(toolRequest);
        tool.setOrganization(organization);

        if (files != null) {
            for (final MultipartFile file : files) {
                validateFile(file);
                try {
                    final String key = s3Service.uploadObject(file.getOriginalFilename(), file.getInputStream());
                    final ToolImage img = new ToolImage();
                    img.setS3Key(key);
                    img.setTool(tool);
                    tool.getImages().add(img);
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        final Tool savedTool = toolRepository.save(tool);
        return toToolResponse(savedTool);
    }

    @Transactional
    public ToolResponse updateTool(final Long id, final ToolRequest toolRequest, final List<MultipartFile> files) throws BadRequestException {
        final Tool existingTool = toolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tool not found with id: " + id));

        existingTool.setName(toolRequest.name());
        existingTool.setDescription(toolRequest.description());
        existingTool.setCategory(toolRequest.category());
        existingTool.setConditionStatus(toolRequest.conditionStatus());
        existingTool.setQuantityAvailable(toolRequest.quantityAvailable());

        if (files != null) {
            if (files.size() > 3) {
                throw new BadRequestException("Maximum 3 images allowed");
            }

            // delete existing images if new ones supplied
            if (!files.isEmpty()) {
                final List<String> keys = existingTool.getImages().stream()
                        .map(ToolImage::getS3Key)
                        .toList();
                if (!keys.isEmpty()) {
                    s3Service.deleteObjects(keys);
                }
                existingTool.getImages().clear();
            }

            for (final MultipartFile file : files) {
                validateFile(file);
                try {
                    final String key = s3Service.uploadObject(file.getOriginalFilename(), file.getInputStream());
                    final ToolImage img = new ToolImage();
                    img.setS3Key(key);
                    img.setTool(existingTool);
                    existingTool.getImages().add(img);
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        final Tool savedTool = toolRepository.save(existingTool);
        return toToolResponse(savedTool);
    }

    public void deleteTool(final Long id) {
        final Tool tool = toolRepository.getReferenceById(id);
        final List<String> keys = tool.getImages().stream()
                .map(ToolImage::getS3Key)
                .toList();
        if (!keys.isEmpty()) {
            s3Service.deleteObjects(keys);
        }
        toolRepository.delete(tool);
    }

    private Tool mapToTool(final ToolRequest toolRequest) {
        final Tool tool = new Tool();
        tool.setName(toolRequest.name());
        tool.setDescription(toolRequest.description());
        tool.setCategory(toolRequest.category());
        tool.setConditionStatus(toolRequest.conditionStatus());
        tool.setQuantityAvailable(toolRequest.quantityAvailable());
        return tool;
    }

    private ToolResponse toToolResponse(final Tool tool) {
        final List<String> imageUrls = tool.getImages().stream()
                .map(ToolImage::getS3Key)
                .map(k -> s3Service.presignedGetUrl("equipassa", k, Duration.ofMinutes(15)))
                .toList();
        return new ToolResponse(
                tool.getId(),
                tool.getName(),
                tool.getDescription(),
                tool.getCategory(),
                tool.getConditionStatus(),
                tool.getQuantityAvailable(),
                imageUrls
        );
    }

    private void validateFile(final MultipartFile file) throws BadRequestException {
        if (file.getSize() > 3 * 1024 * 1024) {
            throw new BadRequestException("File too large: " + file.getOriginalFilename());
        }
        final String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("Invalid file type: " + file.getOriginalFilename());
        }
    }
}
