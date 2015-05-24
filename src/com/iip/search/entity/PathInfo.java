package com.iip.search.entity;

public class PathInfo {
	private String start;
	private String end;
	private int distance;
	private int pathCost;
	
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
