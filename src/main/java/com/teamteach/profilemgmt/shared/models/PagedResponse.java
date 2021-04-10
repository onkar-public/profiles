package com.teamteach.profilemgmt.shared.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@NoArgsConstructor
public class PagedResponse<T> {
    Map<String,String> links;
    Collection<T> data;
    int size;

    private PagedResponse(Map<String,String> links, Collection<T> data) {
        this.links = links;
        this.data = data;
        this.size = data.size();
    }

    public static <T> PagedResponse<T> getPagedResponse(Collection<T> data) {
        return new PagedResponse(Collections.emptyMap(), data);
    }

    public static <T> PagedResponse<T> getPagedResponse(int totalPages, int curPage, long size, Collection<T> data) {
        String next = "";
        String prev = "";
        String sizeParam = "&size=" + size;
        if (curPage < totalPages) {
            next = "page=" + (curPage + 1) + sizeParam;
        }
        if (curPage > 1) {
            prev = "page=" + (curPage - 1) + sizeParam;
        }
        return new PagedResponse(Map.of("next", next, "prev", prev), data);
    }
}
