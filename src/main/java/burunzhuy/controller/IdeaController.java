package burunzhuy.controller;

import burunzhuy.dto.http.ApiResponse;
import burunzhuy.dto.idea.CreateRequest;
import burunzhuy.dto.idea.UpdateRequest;
import burunzhuy.exception.common.EntityNotFound;
import burunzhuy.resource.idea.FullResource;
import burunzhuy.service.IdeaService;
import burunzhuy.tool.Logger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/idea/")
@RequiredArgsConstructor
public class IdeaController
{
    private final IdeaService ideaService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<?>>> getForCurrentUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int perPage
    )
    {
        try {
            return ResponseEntity.ok(
                    ApiResponse.ok(ideaService.getForCurrentUser(page, perPage))
            );
        } catch (Throwable e) {
            Logger.logToFile("idea.txt", e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(ApiResponse.error("Ошибка сервера, попробуйте позже"));
        }
    }

    @GetMapping("{id}/")
    public ResponseEntity<ApiResponse<FullResource>> getById(
        @PathVariable("id") Long id
    )
    {
        try {
            return ResponseEntity.ok(
                    ApiResponse.ok(ideaService.getById(id))
            );
        } catch (Throwable e) {
            Logger.logToFile("idea.txt", e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(ApiResponse.error("Ошибка сервера, попробуйте позже"));
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FullResource>> create(
        @ModelAttribute @Valid CreateRequest request,
        @RequestPart(value = "preview", required = false) MultipartFile preview,
        @RequestPart(value = "attaches", required = false) MultipartFile[] attaches
    )
    {
        try {
            return ResponseEntity.ok(
                    ApiResponse.ok(ideaService.create(request, preview, attaches))
            );
        } catch (Throwable e) {
            // сделать спец ошибки по файлам
            Logger.logToFile("idea.txt", e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(ApiResponse.error("Ошибка сервера, попробуйте позже"));
        }
    }

    @PatchMapping(value = "{id}/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FullResource>> update(
            @PathVariable("id") Long id,
            @ModelAttribute @Valid UpdateRequest request,
            @RequestPart(value = "preview", required = false) MultipartFile preview,
            @RequestPart(value = "attaches", required = false) MultipartFile[] attaches
    )
    {
        try {
            return ResponseEntity.ok(
                    ApiResponse.ok(ideaService.update(id, request, preview, attaches))
            );
        } catch (Throwable e) {
            // сделать спец ошибки по файлам
            Logger.logToFile("idea.txt", e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(ApiResponse.error("Ошибка сервера, попробуйте позже"));
        }
    }

    @DeleteMapping("{id}/")
    public ResponseEntity<?> delete(
        @PathVariable("id") Long id
    )
    {
        try {
            ideaService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (EntityNotFound e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Throwable e) {
            Logger.logToFile("idea.txt", e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(ApiResponse.error("Ошибка сервера, попробуйте позже"));
        }
    }
}
