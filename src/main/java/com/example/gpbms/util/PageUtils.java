package com.example.gpbms.util;

/**
 * 分页工具
 */
public class PageUtils {

    private int pageSize;       //每页大小
    private int currentPage;    //当前页数
    private int totalPages;     //总页数
    private int totalRecords;   //数据总条数

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    /*知道了数据总条数，每页的大小，就可以算出总页数*/
    public void setTotalRecords(int totalRecords) {
        this.totalPages = totalRecords % pageSize == 0 ? totalRecords / pageSize : (totalRecords / pageSize) + 1;
        this.totalRecords = totalRecords;
    }

}
