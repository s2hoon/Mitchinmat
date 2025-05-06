package com.mitchinmat.global.common.pagination;

import org.springframework.data.domain.Pageable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PageDetail {
	private long totalElements;
	private int totalPages;
	private boolean isLast;
	private int currPage;

	public PageDetail(long totalElements, Pageable pageable) {
		this.totalElements = totalElements;
		this.totalPages = (int)Math.ceil((double)totalElements / pageable.getPageSize());
		this.isLast = pageable.getPageNumber() + 1 >= totalPages;
		this.currPage = pageable.getPageNumber();
	}

}
