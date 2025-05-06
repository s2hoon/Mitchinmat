package com.mitchinmat.global.logback.discord.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Footer {
	private final String text;
	private final String iconUrl;
}
