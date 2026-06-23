package burunzhuy.controller;

import burunzhuy.dto.auction.CreateRequest;
import burunzhuy.dto.http.ApiResponse;
import burunzhuy.resource.auction.AuctionResource;
import burunzhuy.resource.auction.HistoryResource;
import burunzhuy.service.AuctionHistoryService;
import burunzhuy.service.AuctionService;
import burunzhuy.tool.Logger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auction/")
@RequiredArgsConstructor
public class AuctionController {
    private final AuctionService auctionService;
    private final AuctionHistoryService historyService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<?>>> getForCurrentUser(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int perPage
    ) {
        try {
            return ResponseEntity.ok(
                    ApiResponse.ok(auctionService.getForCurrentUser(page, perPage))
            );
        } catch (Throwable e) {
            Logger.logToFile("auction.txt", e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(ApiResponse.error("Ошибка сервера, попробуйте позже"));
        }
    }

    @GetMapping("list/")
    public ResponseEntity<ApiResponse<Page<?>>> getList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int perPage
    ) {
        try {
            return ResponseEntity.ok(
                    ApiResponse.ok(auctionService.getList(page, perPage))
            );
        } catch (Throwable e) {
            Logger.logToFile("auction.txt", e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(ApiResponse.error("Ошибка сервера, попробуйте позже"));
        }
    }

    @GetMapping("{id}/")
    public ResponseEntity<ApiResponse<AuctionResource>> getById(
            @PathVariable("id") Long id
    ) {
        try {
            return ResponseEntity.ok(
                    ApiResponse.ok(auctionService.getById(id))
            );
        } catch (Throwable e) {
            Logger.logToFile("auction.txt", e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(ApiResponse.error("Ошибка сервера, попробуйте позже"));
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AuctionResource>> create(
        @ModelAttribute @Valid CreateRequest request
    ) {
        try {
            return ResponseEntity.ok(
                    ApiResponse.ok(auctionService.create(request))
            );
        } catch (Throwable e) {
            // сделать спец ошибки по файлам
            Logger.logToFile("auction.txt", e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(ApiResponse.error("Ошибка сервера, попробуйте позже"));
        }
    }

    @PatchMapping(value = "{id}/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<HistoryResource>> setBid(
        @PathVariable("id") Long auctionId,
        @ModelAttribute BigDecimal bid
    ) {
        try {
            return ResponseEntity.ok(
                ApiResponse.ok(historyService.setBid(auctionId, bid))
            );
        } catch (Throwable e) {
            // сделать спец ошибки по файлам
            Logger.logToFile("auction.txt", e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(ApiResponse.error("Ошибка сервера, попробуйте позже"));
        }
    }
//
//    @DeleteMapping("{id}/")
//    public ResponseEntity<?> delete(
//            @PathVariable("id") Long id
//    ) {
//        try {
//            ideaService.delete(id);
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        } catch (EntityNotFound e) {
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(ApiResponse.error(e.getMessage()));
//        } catch (Throwable e) {
//            Logger.logToFile("auction.txt", e.getMessage());
//            return ResponseEntity
//                    .internalServerError()
//                    .body(ApiResponse.error("Ошибка сервера, попробуйте позже"));
//        }
//    }
}
