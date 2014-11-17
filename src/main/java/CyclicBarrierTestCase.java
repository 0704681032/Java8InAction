import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

//��������� CyclicBarrier ����������ָ��
public class CyclicBarrierTestCase {

	// ��ʼ������
	public static List<String> initTaskNames() {
		List<String> taskNames = new ArrayList<String>();
		taskNames.add("��װIPod");
		taskNames.add("��װIPhone5");
		taskNames.add("��װIPad4");
		taskNames.add("��װIWatch");
		taskNames.add("��װHTC ONE");
		taskNames.add("��װSamSung GALAXY S4");
		return taskNames;
	}

	// ��������
	public static void main(String[] args) {
		List<String> taskNames = initTaskNames();
		int totalWorkerCount = 10;// ������
		int totalTaskCount = totalWorkerCount * taskNames.size();// ������
		CyclicBarrier barrier = new CyclicBarrier(totalWorkerCount + 1);// ��һ��Boss������̣߳�
		MonitorMechine.init(totalWorkerCount);// ��ʼ�����������Ϣ ͬʱִ�е����������ڹ�����
		System.out.println(new Date() + " - Boss:��ʼ�ɻ� - " + taskNames.get(0));
		for (int i = 0; i < totalTaskCount; ++i) {
			new Wordker(barrier, i % totalWorkerCount, taskNames.get(i
					/ totalWorkerCount)).start();
			if ((i + 1) % totalWorkerCount == 0)// һ�����ν����� �ȴ����̷߳��� ���ܼ���ִ����һ����ָ��
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

	// ����
	static class Wordker extends Thread {

		private CyclicBarrier barrier;// �����
		private int workNo;// ����
		private String taskName;// ��������

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
						+ taskName + " ������ɣ�");
				int index = barrier.await();// �����ȴ���һ��������
				MonitorMechine.recive(index, taskName);// ֪ͨ���ƽ̨����
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// �����
	static class MonitorMechine {

		public static int TASK_COUNT;// ��������

		// ��ʼ�������
		public static void init(int taskCount) {
			MonitorMechine.TASK_COUNT = taskCount;
		}

		// ���ܹ��˴��ݵ���Ϣ ��ʱ����֪ͨ
		public static void recive(int index, String taskName) {
			if (index == 0)
				System.out
						.println(new Date() + " - ���ƽ̨:" + taskName + "������ɣ�");
		}

		// ��� ����Ӧ����������ɺ��Զ�֪ͨ���˽�����һ���������
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

		// ��� ����Ӧ����������ɺ��Զ�֪ͨ���˽�����һ���������
		public static void notice(int workerCount, String oldTaskName,
				String newTaskName) {
			if (newTaskName != null)// ����NULL˵�����������Ѿ����
			{
				MonitorMechine.init(workerCount);// ��ʼ����һ���εļ����Ϣ
				System.out.println(new Date() + " - ���ƽ̨:��ʼ �¸����� - "
						+ newTaskName);
			} else
				System.out.println(new Date() + " - ���ƽ̨:���������Ѿ���ɣ��չ��ؼ�");
		}

	}
}