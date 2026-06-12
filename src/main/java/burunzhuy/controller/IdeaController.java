package burunzhuy.controller;

import burunzhuy.dto.http.ApiResponse;
import burunzhuy.service.IdeaService;
import burunzhuy.tool.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/idea/")
@RequiredArgsConstructor
public class IdeaController
{
    private final IdeaService ideaService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<?>>> getForCurrentUser(
            @RequestParam(defaultValue = "1") int page,
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
}
