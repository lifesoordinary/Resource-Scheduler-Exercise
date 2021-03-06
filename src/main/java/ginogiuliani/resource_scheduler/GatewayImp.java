package ginogiuliani.resource_scheduler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import ginogiuliani.resource_schedule.priority.GatewayMessageProcess;

public class GatewayImp implements Gateway {
	private final ConcurrentHashMap<Integer, LinkedBlockingQueue<Message>> groupedMsgQueues = new ConcurrentHashMap<Integer, LinkedBlockingQueue<Message>>();
	private final Thread[] process;
	static int x = 1;

	public GatewayImp(int threads) {
		process = new Thread[threads];
		System.out.println("Gateway Processing Threads: " + threads);
		for (int i = 0; i < threads; i++) {
			process[i] = new Thread(new GatewayMessageProcess(groupedMsgQueues));
			process[i].start();

		}
	}

	public void send(Message msg) {
		int groupID = msg.getGroupID();

		synchronized (this) {
		}

		if (groupedMsgQueues.containsKey(groupID)) {
			LinkedBlockingQueue<Message> queue = groupedMsgQueues.get(groupID);
			queue.add(msg);
		} else {
			LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();
			try {
				queue.put(msg);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			groupedMsgQueues.put(groupID, queue);
		}
	}

}
