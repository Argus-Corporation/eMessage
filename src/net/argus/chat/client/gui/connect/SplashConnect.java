package net.argus.chat.client.gui.connect;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.UIManager;

import net.argus.chat.client.MainClient;
import net.argus.chat.client.event.ChatEvent;
import net.argus.chat.client.event.ChatListener;
import net.argus.chat.client.gui.GUIClient;
import net.argus.gui.Icon;
import net.argus.gui.Label;
import net.argus.gui.Panel;
import net.argus.gui.component.DialogComponent;
import net.argus.gui.gif.GIFComponent;
import net.argus.gui.gif.GIFListener;
import net.argus.lang.Lang;
import net.argus.util.countdown.CountDown;
import net.argus.util.countdown.CountDownEvent;
import net.argus.util.countdown.CountDownListener;

public class SplashConnect extends DialogComponent {
	
	private CountDown count;
	
	private GIFComponent gif;
	private Label statusText, endText;
	private Panel main, panGIF, panStatus;
	
	private boolean error;
	
	public SplashConnect(String title) {
		super(GUIClient.frame);
		
		count = new CountDown(5);
		count.addCountDownListener(getCountDownListener());
		
		addFocusListener(getFocusListener());
		
		setIcon(Icon.getIcon(GUIClient.icon16).getImage());
		
		
		setAlwaysOnTop(true);
		setResizable(false);
		setTitle(title);
		setClose(false);
		
		setSize(400, 150);
	}

	@Override
	public Panel getComponent() {
		main = new Panel();
		main.setLayout(new BorderLayout());
		
		/*GIF*/
		panGIF = getGIFPanel();
		
		/*STATUS*/
		panStatus = getStatusPanel();
		
		/**----**/
		
		MainClient.addChatListener(getChatListener());

		gif.start();
		
		main.add(BorderLayout.WEST, panGIF);
		main.add(BorderLayout.CENTER, panStatus);
		return main;
	}
	
	/**
	 * get gif panel
	 * @return
	 */
	private Panel getGIFPanel() {
		Panel pan = new Panel();
		pan.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
		
		gif = new GIFComponent(GUIClient.load);
		gif.setPreferredSize(new Dimension(50, 50));
		gif.addGIFListener(getGIFListener());
		gif.setLoop(true);
		
		pan.add(gif);
		
		return pan;
	}
	
	/**
	 * get status panel
	 * @return
	 */
	private Panel getStatusPanel() {
		Panel pan = new Panel();
		pan.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));
		
		Panel panText = new Panel();
		panText.setLayout(new BorderLayout());

		
		Label status = new Label(UIManager.getString("Text.status") + ": ", false);
		statusText = new Label("connection");
		
		
		status.setFont(status.getFont().deriveFont(20f));
		statusText.setFont(statusText.getFont().deriveFont(20f));
		
		panText.add(BorderLayout.WEST, status);
		panText.add(BorderLayout.CENTER, statusText);
		Panel endPan = new Panel();
		
		endText = count.getLabelCountDown(Lang.get("text.countclose.name"));
		endPan.add(endText);
		
		Panel center = new Panel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		
		center.add(panText);
		center.add(endPan);
		
		pan.add(center);
		
		return pan;
	}

	
	private CountDownListener getCountDownListener() {
		return new CountDownListener() {
			public void valueChange(CountDownEvent e) {}
			public void finish(CountDownEvent e) {
				setVisible(false);
			}
		};
	}
	
	private GIFListener getGIFListener() {
		return e -> {
			if(isVisible()) {
				gif.setGIF(error?GUIClient.invalid:GUIClient.valid);
				gif.setLoop(false);
				gif.start();
			}
		};
		
	}
	
	private ChatListener getChatListener() {
		return new ChatListener() {
			public void disconnect(ChatEvent e) {
				error = e.isError();
				end(e);
			}
			public void connect(ChatEvent e) {
				error = e.isError();
				end(e);
			}
			public void addMessage(ChatEvent e) {}
			public void sendMessage(ChatEvent e) {}
			public void receiveMessage(ChatEvent e) {}
			
			public void end(ChatEvent e) {
				if(isVisible()) {
					setClose(true);
					statusText.setText(Lang.get("info." + e.getMessage() + ".name"));
					
					count.start();
					gif.stop();
					
					GUIClient.frame.setEnabled(true);
				}
			}
		};
	}
	
	private FocusListener getFocusListener() {
		return new FocusListener() {
			public void focusLost(FocusEvent arg0) {
				if(GUIClient.getFrame().isEnabled())
					setVisible(false);
			}
			public void focusGained(FocusEvent arg0) {}
		};
	}
	
	@Override
	public void show() {
		GUIClient.frame.setEnabled(false);
		super.show();
	}
	
	@Override
	public void hide() {
		GUIClient.frame.setEnabled(true);
		super.hide();
	}

}
