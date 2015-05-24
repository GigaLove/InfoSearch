package com.iip.search.entity;

import java.util.List;

/**
 * ����������ݽṹ
 * @author ��Ц�Ҵ�
 *
 */
public class SearchResult {
	private String initState;		// �����ĳ�ʼ�ڵ�
	private String goalState;		// ������Ŀ��ڵ�
	private List<PathInfo> pathList;	// ���������о�����·��
	private String res;				// �������: success, failure, cutoff
	private String searchMethod;	// ���õ���������
	
	public SearchResult() {
	}
	
	public SearchResult(String initState, String goalState, String searchMethod) {
		this.initState = initState;
		this.goalState = goalState;
		this.searchMethod = searchMethod;
	}
	

	public String getInitState() {
		return initState;
	}

	public void setInitState(String initState) {
		this.initState = initState;
	}

	public String getGoalState() {
		return goalState;
	}

	public void setGoalState(String goalState) {
		this.goalState = goalState;
	}

	public List<PathInfo> getPathList() {
		return pathList;
	}
	
	public void setPathList(List<PathInfo> pathList) {
		this.pathList = pathList;
	}
	
	public String getRes() {
		return res;
	}
	
	public void setRes(String res) {
		this.res = res;
	}
	
	public String getSearchMethod() {
		return searchMethod;
	}

	public void setSearchMethod(String searchMethod) {
		this.searchMethod = searchMethod;
	}

	/**
	 * ������������Ϣ
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = "Search Goal: " + initState + " --> " + goalState + "\n";
		str += "Search Method: " + searchMethod + "\n";
		str += "Search Result: " + res + "\n\n";
		
		if (pathList != null && pathList.size() != 0) {
			str += "Search Path: \n";
			for (PathInfo pInfo : pathList) {
				str += pInfo.toString() + "\n";
			}
		}
		
		return str;
	}
}
