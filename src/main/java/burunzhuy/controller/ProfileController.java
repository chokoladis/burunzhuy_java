package burunzhuy.controller;

import burunzhuy.dto.http.ApiResponse;
import burunzhuy.exception.auth.UserException;
import burunzhuy.resource.user.UserResource;
import burunzhuy.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @RequestMapping("")
    public ResponseEntity<ApiResponse<UserResource>> getCurrentUser()
    {
        try {
            return ResponseEntity.ok(
                ApiResponse.ok(profileService.getCurrentUser())
            );
        } catch (UserException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
}
