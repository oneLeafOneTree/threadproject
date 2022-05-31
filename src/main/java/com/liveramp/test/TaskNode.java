package com.liveramp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskNode extends Thread {
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 20, 60,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(10),
            Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    private Boolean success = false;
    private Object pause = new Object();
    /**
     * 链表层级
     */
    private Integer level = 0;
    /**
     * 下一个任务节点
     */
    private TaskNode next;
    /**
     * 当前节点的任务列表
     */
    private List<Task> taskList = new ArrayList<>();
    /**
     * 当前节点至少有1个任务成功，才能继续下一个节点
     */
    private int requiredTaskSuccess = 1;

    public TaskNode getNext () {
        return next;
    }

    public void setNext (TaskNode next) {
        this.next = next;
    }

    public List<Task> getTaskList () {
        return taskList;
    }

    public Boolean getSuccess () {
        return success;
    }

    public void setSuccess (Boolean success) {
        this.success = success;
    }

    /**
     * 获取节点层级
     *
     * @return
     */
    public Integer getLevel () {
        return level;
    }

    /**
     * 标记层级
     *
     * @param level
     */
    public void setLevel (int level) {
        this.level = level;
    }

    public void executeTask () throws InterruptedException {
        // 获取任务列表
        // 放线程池里启动
        taskList.forEach(task -> {
            // 如果任务状态不是已完成，才需要丢进线程池执行
            if ( ! task.getTaskStateEnum().equals(Task.TaskState.SUCCESS) ) {
                threadPoolExecutor.execute(task);
            }
        });
        // 判断成功数量是否符合要求，如果符合要求阻塞放行
        // 题目有问题，可以加个超时时间，如果超时了，检测状态的线程直接出来
        new Thread(() -> {
            while ( true ) {
                if ( taskList.size() == 0 ) {
                    break;
                }
                long count = taskList.stream().filter(task -> task.getTaskStateEnum().equals(Task.TaskState.SUCCESS)).count();
                if ( count >= requiredTaskSuccess ) {
                    setSuccess(true);
                    synchronized ( pause ) {
                        pause.notify();
                    }
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch ( InterruptedException e ) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 阻塞线程
        // pauseEvent.waitOne();
        if ( taskList.size() > 0 ) {
            System.out.println("节点" + level + "阻塞开始");
            synchronized ( pause ) {
                pause.wait();

            }
            System.out.println("节点" + level + "阻塞结束");
        } else {
            System.out.println("节点" + level + "没有任务");
        }
    }
}
