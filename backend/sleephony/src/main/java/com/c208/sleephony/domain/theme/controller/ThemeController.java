package com.c208.sleephony.domain.theme.controller;

import com.c208.sleephony.domain.theme.dto.response.ThemeDetailResponse;
import com.c208.sleephony.domain.theme.entity.Theme;
import com.c208.sleephony.domain.theme.service.ThemeService;
import com.c208.sleephony.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
@AllArgsConstructor
@Tag(name = "Theme", description = "테마 관련 API")
public class ThemeController {

    private final ThemeService themeService;

    @Operation(summary = "테마 목록 조회")
    @GetMapping
    public ApiResponse<List<Theme>> getAllThemes() {
        List<Theme> themes = themeService.getAllThemes();
        return ApiResponse.success(HttpStatus.OK, themes);
    }

    @Operation(summary = "테마 상세 조회")
    @GetMapping("/{themeId}")
    public ApiResponse<ThemeDetailResponse> getThemeDetail(@PathVariable Integer themeId) {
        return ApiResponse.success(HttpStatus.OK, themeService.getThemeDetail(themeId));
    }

}
