package com.scrb.baselib.entity;

import java.util.List;

public class DataBean<T> {

    private int currentPage;
    private boolean hasMore;
    private int pageSize;
    private int totalPage;
    private int totalSize;
    private List<T> list;

    public DataBean(int currentPage, boolean hasMore, int pageSize, int totalPage, int totalSize, List<T> list) {
        this.currentPage = currentPage;
        this.hasMore = hasMore;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalSize = totalSize;
        this.list = list;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}