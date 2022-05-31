package com.liveramp.test;

import java.util.ArrayList;
import java.util.List;

public class Context {
    public static TaskNode taskNode = new TaskNode();
    private List<String> list = new ArrayList<>();

    /**
     * 在最后一个节点之后添加节点
     */
    public void addTaskNode () {
        // 找到最后一个节点
        TaskNode lastTaskNode = getLastTaskNode(taskNode);
        // 给最后一个节点之后加一个节点
        lastTaskNode.setNext(new TaskNode());
        // 重新递归链表，设置层级
        traverseSetNodeLevel(0, taskNode);
    }

    private TaskNode getLastTaskNode (TaskNode node) {
        if ( node == null ) {
            // 遍历到末尾退出递归
            return null;
        }
        if ( node.getNext() == null ) {
            // 下一个节点没了，当前节点就是最后一个
            return node;
        }
        return getLastTaskNode(node.getNext());
    }

    /**
     * 在第{level}层之后添加一个新的node
     *
     * @param level 层级下标从0开始
     */
    public void addTaskNode (int level) {
        // 重新递归链表，设置层级
        traverseSetNodeLevel(0, taskNode);

        // 找到层级增加任务
        TaskNode node = findTaskNode(level, 0, taskNode);
        // 原本的第二个节点
        TaskNode temp = node.getNext();
        // 原来第二个节点现在放到第二个节点之后
        TaskNode newNode = new TaskNode();
        newNode.setNext(temp);
        // 在第一个节点后插入一个新节点
        node.setNext(newNode);

        // 重新递归链表，设置层级
        traverseSetNodeLevel(0, taskNode);
    }

    /**
     * 在指定的{level}层节点添加任务{task}
     *
     * @param level
     * @param task
     */
    public void addTask (int level, Task task) {
        // 找到层级增加任务
        TaskNode node = findTaskNode(level, 0, taskNode);
        // 给节点添加任务
        node.getTaskList().add(task);
    }

    private TaskNode findTaskNode (int level, int currentLevel, TaskNode node) {
        if ( node == null ) {
            return null;
        }
        // 设置当前链表节点层级
        node.setLevel(currentLevel);
        if ( node.getLevel().equals(level) ) {
            return node;
        }
        currentLevel += 1;
        return findTaskNode(level, currentLevel, node.getNext());
    }

    private TaskNode traverseSetNodeLevel (int currentLevel, TaskNode node) {
        if ( node == null ) {
            return null;
        }
        // 设置当前链表节点层级
        node.setLevel(currentLevel);
        currentLevel += 1;
        return traverseSetNodeLevel(currentLevel, node.getNext());
    }

    public void showTaskState () {
        getTaskNode(taskNode);
    }

    private void getTaskNode (TaskNode node) {
        if ( node == null ) {
            // 遍历到末尾退出递归
            return;
        }
        // 处理当前节点数据
        System.out.println("-----------------------------");
        System.out.println("当前节点：" + node.getLevel() + ",任务数量：" + node.getTaskList().size());
        for ( Task task : node.getTaskList() ) {
            System.out.println("任务id：" + task.getTaskId() + ",任务状态：" + task.getTaskState());
        }
        getTaskNode(node.getNext());
    }

    /**
     * 执行任务链
     *
     * @throws InterruptedException
     */
    public void executeTask () throws InterruptedException {
        recursionTaskNode(taskNode);
    }

    /**
     * 重试执行任务链
     */
    public void retryExecuteTask () throws InterruptedException {
        // 判断任务链中是否存在正在运行中的任务，如果有正在运行中的任务禁止重试
        if ( checkTaskNode(taskNode) ) {
            System.out.println("当前任务链中存在正在运行的任务，无法重试！");
        } else {
            // 执行任务
            executeTask();
        }
    }

    /**
     * 检查任务链中是否存在正在运行的任务
     * true：有，false：没有
     *
     * @param node
     * @return
     * @throws InterruptedException
     */
    private Boolean checkTaskNode (TaskNode node) throws InterruptedException {
        if ( node == null ) {
            return false;
        }
        // 遍历任务
        for ( Task task : node.getTaskList() ) {
            // 如果任务状态=正在运行中不让重试
            if ( task.getTaskStateEnum().equals(Task.TaskState.RUNNING) ) {
                System.out.println("任务" + task.getTaskId() + "正在运行中...");
                return true;
            }
        }
        return checkTaskNode(node.getNext());
    }

    /**
     * 递归遍历节点
     *
     * @param node
     */
    private void recursionTaskNode (TaskNode node) throws InterruptedException {
        if ( node == null ) {
            return;
        }
        // 运行当前节点的任务
        System.out.println("----------------" + "节点" + node.getLevel() + "----------------");
        node.executeTask();
        recursionTaskNode(node.getNext());
    }
}
