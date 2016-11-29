package com.web.common;

public class PageForQuery {
	private int page, size;
	
	public PageForQuery(int page, int size) {
		this.page = page;
		if (size > 10) {
			size = 10;
		}
		this.size = size;
	}

	public int getPage() {
		return page;
	}


	public int getSize() {
		return size;
	}
}
