package com.deepflow.app.domain.settings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FontFamily {
    NANUM_MYEONGJO("나눔 명조"),
    KOPUB_BATANG("KoPub 바탕체"),
    NOTO_SANS("본고딕");

    private final String displayName;
}
