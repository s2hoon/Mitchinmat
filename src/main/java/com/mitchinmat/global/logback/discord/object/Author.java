package com.mitchinmat.global.logback.discord.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Author {
	private final String name;
	private final String url;
	private final String iconUrl;
}
