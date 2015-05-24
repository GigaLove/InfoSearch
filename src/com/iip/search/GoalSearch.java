package com.iip.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import com.iip.search.entity.Node;
import com.iip.search.entity.PathInfo;
import com.iip.search.entity.Problem;
import com.iip.search.entity.SearchResult;
import com.iip.search.tool.ReadData;

public class GoalSearch {
	
	private static final String SUCCESS = "success";
	private static final String FAILURE = "failure";
	private static final String CUTOFF = "cutoff"; 
	
	public static SearchResult BFS(Problem problem) {		
		Map<String, Node> nodeMap = problem.getNodeMap();
		Node initNode = nodeMap.get(problem.getInitalState());
		Node node = null;
		Map<String, Integer> neighbourMap = null;
		
		SearchResult seResult = new SearchResult(problem.getInitalState(), 
				problem.getGoalState(), "BFS");
		List<PathInfo> pathList = new ArrayList<PathInfo>();
		
		if (problem.goalTest(initNode.getName())) {
			seResult.setRes(SUCCESS);
			return seResult;
		}
		
		Queue<Node> frontier = new LinkedList<Node>();
		frontier.add(initNode);
		Set<String> explored = new HashSet<String>();
		
		while (true) {
			if (frontier.isEmpty()) {
				seResult.setRes(FAILURE);
				seResult.setPathList(pathList);
				return seResult;
			}
			
			node = frontier.poll();
			explored.add(node.getName());
			
			neighbourMap = node.getNeighbour();
			
			Iterator<Entry<String, Integer>> iter = neighbourMap.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Integer> entry = (Entry<String, Integer>) iter.next();
				String neighName = entry.getKey();
				int distance = entry.getValue();
				Node cNode = problem.childNode(neighName);
				cNode.setPathCost(node.getPathCost() + distance);
				
				if (!frontier.contains(cNode) && !explored.contains(cNode.getName())) {
					PathInfo pathInfo = new PathInfo(node.getName(), neighName, distance, 
							cNode.getPathCost());
					pathList.add(pathInfo);
					if (problem.goalTest(cNode.getName())) {
						seResult.setRes(SUCCESS);
						seResult.setPathList(pathList);
						return seResult;
					}
					frontier.add(cNode);
				}
			}
		}
	}
	
	public static SearchResult IDS(Problem problem) {
		Map<String, Node> nodeMap = problem.getNodeMap();
		Node initNode = nodeMap.get(problem.getInitalState());
		Node node = null;
		Map<String, Integer> neighbourMap = null;
		
		// 初始化返回结果数据结构
		SearchResult seResult = new SearchResult(problem.getInitalState(), 
				problem.getGoalState(), "IDS");
		List<PathInfo> pathList = new ArrayList<PathInfo>();
		
		LinkedList<Node> frontier = new LinkedList<Node>();
		frontier.add(initNode);
		Set<String> explored = new HashSet<String>();
		
		while (true) {
			if (frontier.isEmpty()) {
				seResult.setRes(FAILURE);
				seResult.setPathList(pathList);
				return seResult;
			}
			
			node = frontier.poll();
			
			if (problem.goalTest(node.getName())) {
				seResult.setRes(SUCCESS);
				seResult.setPathList(pathList);
				return seResult;
			}
			
			explored.add(node.getName());
			
			neighbourMap = node.getNeighbour();
			
			Iterator<Entry<String, Integer>> iter = neighbourMap.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Integer> entry = (Entry<String, Integer>) iter.next();
				String neighName = entry.getKey();
				int distance = entry.getValue();
				Node cNode = problem.childNode(neighName);
				cNode.setPathCost(node.getPathCost() + distance);
				
				if (!frontier.contains(cNode) && !explored.contains(cNode.getName())) {	
					insert2Frontier(frontier, cNode);
					PathInfo pathInfo = new PathInfo(node.getName(), neighName, distance, 
							cNode.getPathCost());
					pathList.add(pathInfo);
				}
				else {
					int index;
					boolean flag = false;
					for (index = 0; index < frontier.size(); index++) {
						if (frontier.get(index).getName().equals(cNode.getName())) {
							flag = true;
							break;
						}
					}
					
					if (flag && frontier.get(index).getPathCost() > cNode.getPathCost()) {
						frontier.remove(index);
						insert2Frontier(frontier, cNode);
						PathInfo pathInfo = new PathInfo(node.getName(), neighName, distance, 
								cNode.getPathCost());
						pathList.add(pathInfo);
					}
				}
			}
		}
	}
	
	private static void insert2Frontier(LinkedList<Node> frontier, Node node) {
		int index;
		
		for (index = 0; index < frontier.size(); index++) {
			if (frontier.get(index).getPathCost() > node.getPathCost()) {
				break;
			}
		}
		frontier.add(index, node);
	}
	
	public static SearchResult DLS(Problem problem, int limit) {
		Node initNode = problem.getNodeMap().get(problem.getInitalState());
		
		SearchResult seResult = new SearchResult(problem.getInitalState(), 
				problem.getGoalState(), "DLS, limit: " + limit);
		List<PathInfo> pathList = new ArrayList<PathInfo>();
		
		String res = recursiveDLS(initNode, problem, limit, pathList);
		
		seResult.setRes(res);
		seResult.setPathList(pathList);
		return seResult;
	}
	
	private static String recursiveDLS(Node node, Problem problem, int limit, 
			List<PathInfo> pathList) {
		
		if (problem.goalTest(node.getName())) {
			return SUCCESS;
		}
		else if (limit == 0) {
			return CUTOFF;
		}
		else {
			boolean cutoff_occurred = false;
			Map<String, Integer> neighbourMap = node.getNeighbour();
			
			Iterator<Entry<String, Integer>> iter = neighbourMap.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Integer> entry = (Entry<String, Integer>) iter.next();
				String neighName = entry.getKey();
				int distance = entry.getValue();
				Node cNode = problem.childNode(neighName);
				cNode.setPathCost(node.getPathCost() + distance);
				
				pathList.add(new PathInfo(node.getName(), cNode.getName(), distance, 
						cNode.getPathCost()));
				
				String res = recursiveDLS(cNode, problem, limit - 1, 
						pathList);
				if (res.equals(CUTOFF)) {
					cutoff_occurred = true;
				}
				else if (!res.equals(FAILURE)) {
					return res;
				}
			}
			if (cutoff_occurred) {
				return CUTOFF;
			}
			else {
				return FAILURE;
			}
		}
	}
	
	private static Map<String, Integer> readAssistInfo(String fileName) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		List<String[]> dataList = ReadData.readCommaFile(fileName);
		
		for (String[] dataArray : dataList) {
			map.put(dataArray[0], Integer.parseInt(dataArray[1]));
		}
		
		return map;
	}
	
	public static SearchResult AStarSearch(Problem problem, String fileName) {		
		Map<String, Node> nodeMap = problem.getNodeMap();
		Map<String, Integer> neighbourMap = null;
		Map<String, Integer> hMap = readAssistInfo(fileName);
		Node initNode = nodeMap.get(problem.getInitalState());
		Node node = null;
		
		
		// 初始化返回结果数据结构
		SearchResult seResult = new SearchResult(problem.getInitalState(), 
				problem.getGoalState(), "A* Search");
		List<PathInfo> pathList = new ArrayList<PathInfo>();
		
		LinkedList<Node> frontier = new LinkedList<Node>();
		frontier.add(initNode);
		Set<String> explored = new HashSet<String>();
		
		while (true) {
			if (frontier.isEmpty()) {
				seResult.setRes(FAILURE);
				seResult.setPathList(pathList);
				return seResult;
			}
			
			node = frontier.poll();
			
			if (problem.goalTest(node.getName())) {
				seResult.setRes(SUCCESS);
				seResult.setPathList(pathList);
				return seResult;
			}
			
			explored.add(node.getName());
			neighbourMap = node.getNeighbour();
			
			Iterator<Entry<String, Integer>> iter = neighbourMap.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Integer> entry = (Entry<String, Integer>) iter.next();
				String neighName = entry.getKey();
				int distance = entry.getValue();
				Node cNode = problem.childNode(neighName);
				cNode.setPathCost(node.getPathCost() + distance);
				
				if (!frontier.contains(cNode) && !explored.contains(cNode.getName())) {	
					insert2Frontier(frontier, cNode, hMap);
					PathInfo pathInfo = new PathInfo(node.getName(), neighName, distance, 
							cNode.getPathCost());
					pathList.add(pathInfo);
				}
				else {
					int index;
					boolean flag = false;
					for (index = 0; index < frontier.size(); index++) {
						if (frontier.get(index).getName().equals(cNode.getName())) {
							flag = true;
							break;
						}
					}
					
					if (flag && frontier.get(index).getPathCost() > cNode.getPathCost()) {
						frontier.remove(index);
						insert2Frontier(frontier, cNode, hMap);
						PathInfo pathInfo = new PathInfo(node.getName(), neighName, distance, 
								cNode.getPathCost());
						pathList.add(pathInfo);
					}
				}
			}
		}	
	}
	
	private static void insert2Frontier(LinkedList<Node> frontier, Node node, 
			Map<String, Integer> map) {
		int index;
		
		for (index = 0; index < frontier.size(); index++) {
			Node fNode = frontier.get(index);
			int fNodeCost = fNode.getPathCost() + map.get(fNode.getName());
			int nodeCost = node.getPathCost() + map.get(node.getName());
			if (fNodeCost > nodeCost) {
				break;
			}
		}
		frontier.add(index, node);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Problem p = new Problem("Rumania-map.txt", "Arad", "Bucharest");
		p.readProblem();
		SearchResult searchResult = GoalSearch.IDS(p);
		System.out.println(searchResult);
	}
}
