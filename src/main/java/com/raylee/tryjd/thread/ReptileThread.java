package com.raylee.tryjd.thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReptileThread extends Thread {

	private String control = "";
	private boolean suspend = true;

	@Override
	public void run() {
		while (true) {
			if (suspend) {
				try {
					synchronized (control) {
						control.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				log.info("程序执行中...");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取线程状态
	 * 
	 * @return
	 */
	public boolean isSuspend() {
		return suspend;
	}

	/**
	 * 继续
	 * 
	 * @param suspend
	 */
	public void onPlay() {
		synchronized (control) {
			control.notify();
		}
		this.suspend = false;
	}

	/**
	 * 暂停
	 */
	public void onPause() {
		this.suspend = true;
	}
}
