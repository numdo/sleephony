package com.c208.sleephony.domain.theme.service;

import com.c208.sleephony.domain.theme.entity.Theme;
import com.c208.sleephony.domain.theme.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;

    // 테마 전체 목록 조회
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

}
