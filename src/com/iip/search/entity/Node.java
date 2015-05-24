package com.iip.search.entity;

import java.util.Map;

/**
 * 节点数据结构
 * @author 别笑我呆
 */
public class Node {
	private String name;	// 节点名
	private Map<String, Integer> neighbour;		// 邻居节点信息
	private int pathCost;	// 路径消耗
	
	public Node() {
	}
	
	public Node(String name, Map<String, Integer> map) {
		this.name = name;
		this.neighbour = map;
		pathCost = 0;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, Integer> getNeighbour() {
		return neighbour;
	}
	
	public void setNeighbour(Map<String, Integer> neighbour) {
		this.neighbour = neighbour;
	}

	public int getPathCost() {
		return pathCost;
	}

	public void setPathCost(int pathCost) {
		this.pathCost = pathCost;
	}
}
