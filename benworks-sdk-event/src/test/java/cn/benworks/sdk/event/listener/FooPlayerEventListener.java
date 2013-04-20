package cn.benworks.sdk.event.listener;

import cn.benworks.sdk.event.ApplicationEventListener;
import cn.benworks.sdk.event.anno.BeforeProcess;
import cn.benworks.sdk.event.evt.PlayerEvent;
import cn.benworks.sdk.event.evt.PlayerLeaveSceneEvent;

public class FooPlayerEventListener implements ApplicationEventListener<PlayerEvent> {

	@Override
	public void on(PlayerEvent event) {
		System.out.println("FooPlayerEventListener.on " + event);
	}

	@BeforeProcess
	public void beforeLeave(PlayerLeaveSceneEvent event) {
		System.out.println("FooPlayerEventListener.beforeLeave " + event);
	}
}
