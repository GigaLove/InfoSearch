package com.iip.search.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iip.search.tool.ReadData;

/**
 * ������������ݽṹ
 * @author ��Ц�Ҵ�
 *
 */
public class Problem {
	private Map<String, Node> nodeMap;		// �����������������нڵ����Ϣ
	private String fileName;				// ������Ϣ�Ĵ洢�ļ�
	private String initalState;				// �����ʼ�ڵ�
	private String goalState;				// ����Ŀ��ڵ�
	
	public Problem() {
		
	}
	
	public Problem(String fileName, String initalState, String goalState) {
		this.fileName = fileName;
		this.initalState = initalState;
		this.goalState = goalState;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getInitalState() {
		return initalState;
	}
	
	public void setInitalState(String initalState) {
		this.initalState = initalState;
	}
	
	public String getGoalState() {
		return goalState;
	}
	
	public void setGoalState(String goalState) {
		this.goalState = goalState;
	}
	
	public Map<String, Node> getNodeMap() {
		return nodeMap;
	}

	public void setNodeMap(Map<String, Node> nodeMap) {
		this.nodeMap = nodeMap;
	}
	
	/**
	 * �жϵ�ǰ״̬�Ƿ���Ŀ��״̬
	 * @param state ��ǰ״̬
	 * @return true or false
	 */
	public boolean goalTest(String state) {
		if (state.equals(goalState)) {
			return true;
		}
		return false;
	}
	
	public Node childNode(String name) {
		return nodeMap.get(name);
	}
	
	/**
	 * ��txt�ļ��������룬����ӵ�hashmap��
	 */
	public void readProblem() {		
		Map<String, Integer> map = null;
		nodeMap = new HashMap<String, Node>();
		Node node = null;
		Node neiNode = null;
		
		List<String[]> dataList = ReadData.readCommaFile(fileName);
		
		for (String[] dataArray : dataList) {
			String nodeName = dataArray[0];
			String neiNodeName = dataArray[1];
			int distance = Integer.parseInt(dataArray[2]);
			
			node = nodeMap.get(nodeName);
			if (node != null) {
				map = node.getNeighbour();
				map.put(neiNodeName, distance);
			}
			else {
				map = new HashMap<String, Integer>();
				map.put(neiNodeName, distance);
				node = new Node(nodeName, map);
				nodeMap.put(nodeName, node);
			}
			
			neiNode = nodeMap.get(neiNodeName);
			if (neiNode != null) {
				map = neiNode.getNeighbour();
				map.put(nodeName, distance);
			}
			else {
				map = new HashMap<String, Integer>();
				map.put(nodeName, distance);
				neiNode = new Node(neiNodeName, map);
				nodeMap.put(neiNodeName, neiNode);
			}
		}
	}
	
	/**
	 * ��ȡ������Ϣ
	 * @param fileName
	 * @return
	 */
	public static Map<String, Integer> readAssistInfo(String fileName) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		List<String[]> dataList = ReadData.readCommaFile(fileName);
		
		for (String[] dataArray : dataList) {
			map.put(dataArray[0], Integer.parseInt(dataArray[1]));
		}
		
		return map;
	}
	
	public static void main(String[] args) {
	}
}
