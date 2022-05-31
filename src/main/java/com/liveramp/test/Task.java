package com.liveramp.test;

import java.util.HashMap;

public class Task extends Thread {
	/**
	 * 任务状态
	 */
	private TaskState taskState;
	private Integer taskId;
	private static HashMap<Integer, String> stateMap = null;

	public Task() {
		taskState = TaskState.NOT_STARTED;
		if (stateMap == null) {
			stateMap = new HashMap();
			stateMap.put(0, "未开始");
			stateMap.put(1, "运行中");
			stateMap.put(2, "执行失败");
			stateMap.put(3, "执行成功");
		}
	}

	@Override
	public void run() {
		try {
			System.out.println("任务" + taskId + "开始执行");
			taskState = TaskState.RUNNING;
			Thread.sleep(taskId * 4 * 1000);
			taskState = TaskState.values()[Random.getRandom(2, 3)];
			// taskState = TaskState.SUCCESS;
			System.out.println("任务" + taskId + "执行结束，结果=" + getTaskState());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getTaskState() {
		return stateMap.get(taskState.ordinal());
	}

	public TaskState getTaskStateEnum() {
		return taskState;
	}

	public Task setTaskState(TaskState taskState) {
		this.taskState = taskState;
		return this;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public Task setTaskId(Integer taskId) {
		this.taskId = taskId;
		return this;
	}

	/**
	 * 任务状态
	 */
	public enum TaskState {
		/**
		 * 未开始
		 */
		NOT_STARTED,
		/**
		 * 运行中
		 */
		RUNNING,
		/**
		 * 执行失败
		 */
		ERROR,
		/**
		 * 执行成功
		 */
		SUCCESS
	}
}
