package com.mitchinmat.global.logback.discord.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Field {

	private final String name;
	private final String value;
	private final boolean inline;
}
