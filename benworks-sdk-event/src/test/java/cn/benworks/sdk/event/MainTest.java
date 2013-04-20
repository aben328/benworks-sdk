package cn.benworks.sdk.event;

import cn.benworks.sdk.event.evt.PlayerLeaveSceneEvent;
import cn.benworks.sdk.event.evt.PlayerMoveEvent;
import cn.benworks.sdk.event.evt.PlayerMultiMoveEvent;
import cn.benworks.sdk.event.listener.BarPlayerEventListener;
import cn.benworks.sdk.event.listener.FooPlayerEventListener;
import cn.benworks.sdk.event.listener.MagicPlayerListener;
import cn.benworks.sdk.event.register.PlayerEventListenerRegister;

public class MainTest {

	public static void main(String[] args) {
		//
		PlayerEventListenerRegister register = new PlayerEventListenerRegister();
		register.register(new FooPlayerEventListener());
		register.register(new BarPlayerEventListener());
		register.register(new MagicPlayerListener());

		// dispatch...
		System.out.println("Dispatch PlayerMultiMoveEvent");
		register.dispatch(new PlayerMultiMoveEvent());
		System.out.println("========");
		System.out.println("Dispatch PlayerMoveEvent");
		register.dispatch(new PlayerMoveEvent());
		System.out.println("=========");
		System.out.println("Dispatch PlayerLeaveSceneEvent");
		register.dispatch(new PlayerLeaveSceneEvent());

	}
}
