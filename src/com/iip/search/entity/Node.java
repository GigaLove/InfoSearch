package com.iip.search.entity;

import java.util.Map;

/**
 * �ڵ����ݽṹ
 * @author ��Ц�Ҵ�
 */
public class Node {
	private String name;	// �ڵ���
	private Map<String, Integer> neighbour;		// �ھӽڵ���Ϣ
	private int pathCost;	// ·������
	
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
