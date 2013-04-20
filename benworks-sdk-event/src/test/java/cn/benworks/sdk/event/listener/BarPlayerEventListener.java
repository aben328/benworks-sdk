package cn.benworks.sdk.event.listener;

import cn.benworks.sdk.event.anno.AfterProcess;
import cn.benworks.sdk.event.evt.PlayerEvent;
import cn.benworks.sdk.event.evt.PlayerLeaveSceneEvent;
import cn.benworks.sdk.event.evt.PlayerMultiMoveEvent;
import cn.benworks.sdk.event.evt.PlayerSitEvent;
import cn.benworks.sdk.event.evt.PlayerStandEvent;

public class BarPlayerEventListener extends GenericEventListener<PlayerEvent> {

	public void onStand(PlayerStandEvent event) {
		System.out.println("BarPlayerEventListener: On Stand!");
	}

	public void someOneSit(PlayerSitEvent event) {
		// logic
		System.out.println("BarPlayerEventListener.someOneSit " + event);
	}

	public void moveCheck(PlayerMultiMoveEvent event) {
		System.out.println("BarPlayerEventListener.moveCheck " + event);
	}

	// 记录漏网之鱼
	public void on(PlayerEvent other) {
		System.out.println("BarPlayerEventListener.on " + other);
	}

	@AfterProcess
	public void clearResourceAfterPlayerLeave(PlayerLeaveSceneEvent event) {
		// logic
		System.out.println("BarPlayerEventListener.clearResourceAfterPlayerLeave " + event);
	}
}
