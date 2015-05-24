package com.iip.search.entity;

/**
 * 搜索过程路径信息数据结构
 * @author 别笑我呆
 *
 */
public class PathInfo {
	private String start;	// 出发点
	private String end;		// 终点
	private int distance;	// 两点距离
	private int pathCost;	// 到达终点，目前总的路径开销
	
	public PathInfo() {
		
	}
	
	public PathInfo(String start, String end, int distance, int pathCost) {
		this.start = start;
		this.end = end;
		this.distance = distance;
		this.pathCost = pathCost;
	}

	public String getStart() {
		return start;
	}
	
	public void setStart(String start) {
		this.start = start;
	}
	
	public String getEnd() {
		return end;
	}
	
	public void setEnd(String end) {
		this.end = end;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public int getPathCost() {
		return pathCost;
	}

	public void setPathCost(int pathCost) {
		this.pathCost = pathCost;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return start + " --> " + end + "; distance: " + distance + "; pathCost: " + 
				pathCost;
	}
	
}
