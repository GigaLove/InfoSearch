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
	// �����������
	private static final String SUCCESS = "success";
	private static final String FAILURE = "failure";
	private static final String CUTOFF = "cutoff"; 
	
	/**
	 * �����������
	 * @param problem ��������
	 * @return �������
	 */
	public static SearchResult BFS(Problem problem) {
		Map<String, Node> nodeMap = problem.getNodeMap();	// ��ȡ�ڵ�map
		Node initNode = nodeMap.get(problem.getInitalState());	// ��ȡ��ʶ�ڵ�
		Node node = null;
		Map<String, Integer> neighbourMap = null;	// �����ھӽڵ�map
		
		SearchResult seResult = new SearchResult(problem.getInitalState(), 
				problem.getGoalState(), "BFS");	// ��ʼ������������ݽṹ
		List<PathInfo> pathList = new ArrayList<PathInfo>();
		
		// Ŀ�����
		if (problem.goalTest(initNode.getName())) {
			seResult.setRes(SUCCESS);
			return seResult;
		}
		
		// �����߽�FIFO����frontier��explored��չ��
		Queue<Node> frontier = new LinkedList<Node>();
		frontier.add(initNode);
		Set<String> explored = new HashSet<String>();
		
		while (true) {
			// �жϱ߽缯�Ƿ������ݣ���������ʧ��
			if (frontier.isEmpty()) {
				seResult.setRes(FAILURE);
				seResult.setPathList(pathList);
				return seResult;
			}
			
			// ��ȡͷ�ڵ㣬��ӵ���չ��explored��
			node = frontier.poll();
			explored.add(node.getName());
			
			neighbourMap = node.getNeighbour();
			
			// �����ھӽڵ�
			Iterator<Entry<String, Integer>> iter = neighbourMap.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Integer> entry = (Entry<String, Integer>) iter.next();
				String neighName = entry.getKey();
				int distance = entry.getValue();
				Node cNode = problem.childNode(neighName);
				cNode.setPathCost(node.getPathCost() + distance);
				
				// �ж��ھӽڵ��Ƿ���frontier�л���explored�У�����ӵ�frontier��
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
	 * ����һ�´��۲��Խ���Ŀ������
	 * @param problem
	 * @return
	 */
	public static SearchResult IDS(Problem problem) {
		Map<String, Node> nodeMap = problem.getNodeMap();
		Node initNode = nodeMap.get(problem.getInitalState());
		Node node = null;
		Map<String, Integer> neighbourMap = null;
		
		// ��ʼ�����ؽ�����ݽṹ
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
	 * һ�´�����������frontier���еĽڵ����ʵ��
	 * @param frontier	�߽����
	 * @param node	����ڵ�
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
	 * �������������������
	 * @param problem	��������
	 * @param limit		�������
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
	 * �ݹ�ʵ�������������
	 * @param node		�����ڵ�
	 * @param problem	����
	 * @param limit		�������
	 * @param pathList	·����Ϣ
	 * @return
	 */
	private static String recursiveDLS(Node node, Problem problem, int limit, 
			List<PathInfo> pathList) {
		
		// Ŀ�����
		if (problem.goalTest(node.getName())) {
			return SUCCESS;
		}
		else if (limit == 0) {
			// limit��Ϊ0��������������
			return CUTOFF;
		}
		else {
			boolean cutoff_occurred = false;
			Map<String, Integer> neighbourMap = node.getNeighbour();
			
			// �����ھӽڵ�
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
	 * A* �㷨����ʵ��
	 * @param problem	��������
	 * @param fileName  ������Ϣ�ļ��������Ե���h*����
	 * @return
	 */
	public static SearchResult AStarSearch(Problem problem, String fileName) {	
		// ��ʼ���ڵ���Ϣ
		Map<String, Node> nodeMap = problem.getNodeMap();
		Map<String, Integer> neighbourMap = null;
		Map<String, Integer> hMap = Problem.readAssistInfo(fileName);
		Node initNode = nodeMap.get(problem.getInitalState());
		Node node = null;
		
		
		// ��ʼ�����ؽ�����ݽṹ
		SearchResult seResult = new SearchResult(problem.getInitalState(), 
				problem.getGoalState(), "A* Search");
		List<PathInfo> pathList = new ArrayList<PathInfo>();
		
		LinkedList<Node> frontier = new LinkedList<Node>();
		frontier.add(initNode);
		Set<String> explored = new HashSet<String>();
		
		while (true) {
			// �ж�frontier�Ƿ�Ϊ�գ��Ƿ��޷�������չ���Ƿ���failure
			if (frontier.isEmpty()) {
				seResult.setRes(FAILURE);
				seResult.setPathList(pathList);
				return seResult;
			}
			
			node = frontier.poll();
			// Ŀ�����
			if (problem.goalTest(node.getName())) {
				seResult.setRes(SUCCESS);
				seResult.setPathList(pathList);
				return seResult;
			}
			
			explored.add(node.getName());
			neighbourMap = node.getNeighbour();
			
			// �����ھӽڵ�
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
	 * ��A*�㷨frontier���нڵ����f(n)�Ĳ���ʵ��
	 * @param frontier	�߽缯
	 * @param node	����ڵ�
	 * @param map	������Ϣ���䵱h*(n)
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
