package com.delicloud.app.miniprint.core.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页统一出参对象
 * @param <T>
 */
@Data
public class PageVo<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4758713041361214023L;
	
	/**
     * 数据
     */
    private List<T> content = new ArrayList<T>();
    
    /**
     * 每页大小
     */
    private int size;

    /**
     * 当前页为第几页
     */
    private int number;

    /**
     * 是否为第一页
     */
    private boolean first = false;

    /**
     * 是否为最后一页
     */
    private boolean last = false;

    /**
     * 总共有多少页
     */
    private int totalPages;

    /**
     * 总共有多少条数据
     */
    private int totalElements;

    /**
     * 当前页一共有多少条数据
     */
    private int numberOfElements;
    
    /**
     * 根据传入的当前多少页
     * @param size
     * @param number
     * @param totalElements
     */
    public PageVo(int number, int size, int totalElements) {

        this.size = size;

        this.number = number;

        this.totalElements = totalElements;

        this.totalPages = totalElements % size == 0 ? totalElements/size : (totalElements/size) + 1;

        this.first = number == 1 ? true : false;

        this.last = number == this.totalPages ? true : false;

    }
    
}
