import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

//任务调度室 CyclicBarrier 测试用例总指挥
public class CyclicBarrierTestCase {

	// 初始化任务
	public static List<String> initTaskNames() {
		List<String> taskNames = new ArrayList<String>();
		taskNames.add("组装IPod");
		taskNames.add("组装IPhone5");
		taskNames.add("组装IPad4");
		taskNames.add("组装IWatch");
		taskNames.add("组装HTC ONE");
		taskNames.add("组装SamSung GALAXY S4");
		return taskNames;
	}

	// 启动函数
	public static void main(String[] args) {
		List<String> taskNames = initTaskNames();
		int totalWorkerCount = 10;// 工人数
		int totalTaskCount = totalWorkerCount * taskNames.size();// 任务数
		CyclicBarrier barrier = new CyclicBarrier(totalWorkerCount + 1);// 加一个Boss发令（主线程）
		MonitorMechine.init(totalWorkerCount);// 初始化首批监控信息 同时执行的任务数等于工人数
		System.out.println(new Date() + " - Boss:开始干活 - " + taskNames.get(0));
		for (int i = 0; i < totalTaskCount; ++i) {
			new Wordker(barrier, i % totalWorkerCount, taskNames.get(i
					/ totalWorkerCount)).start();
			if ((i + 1) % totalWorkerCount == 0)// 一个批次结束后 等待主线程发令 才能继续执行下一批次指令
			{
				if (i + 1 < totalTaskCount)
					MonitorMechine.waitForNext(barrier, totalWorkerCount,
							taskNames.get(i / totalWorkerCount),
							taskNames.get((i + 1) / totalWorkerCount));
				else
					MonitorMechine.waitForNext(barrier, totalWorkerCount,
							taskNames.get(i / totalWorkerCount), null);
			}
		}
	}

	// 工人
	static class Wordker extends Thread {

		private CyclicBarrier barrier;// 集结点
		private int workNo;// 工号
		private String taskName;// 任务名称

		public Wordker(CyclicBarrier barrier, int workNo, String taskName) {
			this.barrier = barrier;
			this.workNo = workNo;
			this.taskName = taskName;
		}

		@Override
		public void run() {
			try {
				//Thread.sleep(100);
				System.out.println(new Date() + " - YHJ" + workNo + " "
						+ taskName + " 任务完成！");
				int index = barrier.await();// 干完活等待下一批次命令
				MonitorMechine.recive(index, taskName);// 通知监控平台进度
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 监控器
	static class MonitorMechine {

		public static int TASK_COUNT;// 任务数量

		// 初始化监控器
		public static void init(int taskCount) {
			MonitorMechine.TASK_COUNT = taskCount;
		}

		// 接受工人传递的信息 适时发出通知
		public static void recive(int index, String taskName) {
			if (index == 0)
				System.out
						.println(new Date() + " - 监控平台:" + taskName + "任务完成！");
		}

		// 监控 当对应批次任务完成后自动通知工人进行下一批任务操作
		public static void waitForNext(CyclicBarrier barrier, int workerCount,
				String oldTaskName, String newTaskName) {
			try {
				recive(barrier.await(), oldTaskName);
				notice(workerCount, oldTaskName, newTaskName);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}

		}

		// 监控 当对应批次任务完成后自动通知工人进行下一批任务操作
		public static void notice(int workerCount, String oldTaskName,
				String newTaskName) {
			if (newTaskName != null)// 等于NULL说明所以任务已经完成
			{
				MonitorMechine.init(workerCount);// 初始化下一批次的监控信息
				System.out.println(new Date() + " - 监控平台:开始 下个任务 - "
						+ newTaskName);
			} else
				System.out.println(new Date() + " - 监控平台:所有任务都已经完成，收工回家");
		}

	}
}