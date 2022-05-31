package com.liveramp.test;

public class TaskDemoMain {
	public static void main(String[] args) throws InterruptedException {
		Context context = new Context();

		// 添加4个节点
		// 在最后一个节点之后添加节点
		System.out.println("在最后一个节点之后添加node");
		context.addTaskNode();
		context.addTaskNode();

		// 添加第四个任务节点
		// 给某一个节点之后插入一个节点，如果输入的节点层级不存在会报错
		System.out.println("在索引第2个节点增加一个node");
		context.addTaskNode(2);

		// 给节点3增加2个任务
		// 给某一个节点增加任务
		System.out.println("给索引为2的node节点增加一个任务1");
		context.addTask(2, new Task().setTaskId(1));
		System.out.println("给索引为2的node节点增加一个任务2");
		context.addTask(2, new Task().setTaskId(2));
		System.out.println("给索引为0的node节点增加一个任务1");
		context.addTask(0, new Task().setTaskId(1));

		// 执行任务链
		System.out.println("\r\n");
		System.out.println("执行任务链：\r\n");
		context.executeTask();

		// 重试任务链
		System.out.println("\r\n");
		System.out.println("重试任务链：\r\n");
		context.retryExecuteTask();

		// 打印当前的任务链
		System.out.println("\r\n");
		System.out.println("打印任务链信息：\r\n");
		context.showTaskState();
		System.out.println("任务链演示完成...");
	}
}
