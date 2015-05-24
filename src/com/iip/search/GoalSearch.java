package com.iip.search;

import java.util.ArrayList;
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

public class GoalSearch {
	// 声明搜索结果
	private static final String SUCCESS = "success";
	private static final String FAILURE = "failure";
	private static final String CUTOFF = "cutoff"; 
	
	/**
	 * 宽度优先搜索
	 * @param problem 问题描述
	 * @return 搜索结果
	 */
	public static SearchResult BFS(Problem problem) {
		Map<String, Node> nodeMap = problem.getNodeMap();	// 获取节点map
		Node initNode = nodeMap.get(problem.getInitalState());	// 获取初识节点
		Node node = null;
		Map<String, Integer> neighbourMap = null;	// 声明邻居节点map
		
		SearchResult seResult = new SearchResult(problem.getInitalState(), 
				problem.getGoalState(), "BFS");	// 初始化搜索结果数据结构
		List<PathInfo> pathList = new ArrayList<PathInfo>();
		
		// 目标测试
		if (problem.goalTest(initNode.getName())) {
			seResult.setRes(SUCCESS);
			return seResult;
		}
		
		// 声明边界FIFO队列frontier和explored扩展集
		Queue<Node> frontier = new LinkedList<Node>();
		frontier.add(initNode);
		Set<String> explored = new HashSet<String>();
		
		while (true) {
			// 判断边界集是否还有内容，无则搜索失败
			if (frontier.isEmpty()) {
				seResult.setRes(FAILURE);
				seResult.setPathList(pathList);
				return seResult;
			}
			
			// 获取头节点，添加到扩展集explored中
			node = frontier.poll();
			explored.add(node.getName());
			
			neighbourMap = node.getNeighbour();
			
			// 迭代邻居节点
			Iterator<Entry<String, Integer>> iter = neighbourMap.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Integer> entry = (Entry<String, Integer>) iter.next();
				String neighName = entry.getKey();
				int distance = entry.getValue();
				Node cNode = problem.childNode(neighName);
				cNode.setPathCost(node.getPathCost() + distance);
				
				// 判断邻居节点是否在frontier中或者explored中，无添加到frontier中
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
	
	/**
	 * 采用一致代价策略进行目标搜索
	 * @param problem
	 * @return
	 */
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
	
	/**
	 * 一致代价搜索策略frontier队列的节点插入实现
	 * @param frontier	边界队列
	 * @param node	插入节点
	 */
	private static void insert2Frontier(LinkedList<Node> frontier, Node node) {
		int index;
		
		for (index = 0; index < frontier.size(); index++) {
			if (frontier.get(index).getPathCost() > node.getPathCost()) {
				break;
			}
		}
		frontier.add(index, node);
	}
	
	/**
	 * 受限深度优先搜索策略
	 * @param problem	问题描述
	 * @param limit		限制深度
	 * @return
	 */
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
	
	/**
	 * 递归实现深度优先搜索
	 * @param node		搜索节点
	 * @param problem	问题
	 * @param limit		限制深度
	 * @param pathList	路径信息
	 * @return
	 */
	private static String recursiveDLS(Node node, Problem problem, int limit, 
			List<PathInfo> pathList) {
		
		// 目标测试
		if (problem.goalTest(node.getName())) {
			return SUCCESS;
		}
		else if (limit == 0) {
			// limit变为0，则不再向下搜索
			return CUTOFF;
		}
		else {
			boolean cutoff_occurred = false;
			Map<String, Integer> neighbourMap = node.getNeighbour();
			
			// 迭代邻居节点
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
	
	/**
	 * A* 算法搜索实现
	 * @param problem	问题描述
	 * @param fileName  辅助信息文件名，用以当作h*函数
	 * @return
	 */
	public static SearchResult AStarSearch(Problem problem, String fileName) {	
		// 初始化节点信息
		Map<String, Node> nodeMap = problem.getNodeMap();
		Map<String, Integer> neighbourMap = null;
		Map<String, Integer> hMap = Problem.readAssistInfo(fileName);
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
			// 判断frontier是否为空，是否无法继续扩展，是返回failure
			if (frontier.isEmpty()) {
				seResult.setRes(FAILURE);
				seResult.setPathList(pathList);
				return seResult;
			}
			
			node = frontier.poll();
			// 目标测试
			if (problem.goalTest(node.getName())) {
				seResult.setRes(SUCCESS);
				seResult.setPathList(pathList);
				return seResult;
			}
			
			explored.add(node.getName());
			neighbourMap = node.getNeighbour();
			
			// 迭代邻居节点
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
	
	/**
	 * 向A*算法frontier队列节点基于f(n)的插入实现
	 * @param frontier	边界集
	 * @param node	插入节点
	 * @param map	辅助信息，充当h*(n)
	 */
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
