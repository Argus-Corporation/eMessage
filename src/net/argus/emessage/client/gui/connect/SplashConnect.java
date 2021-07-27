package net.argus.emessage.client.gui.connect;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.argus.emessage.client.ClientResources;
import net.argus.emessage.client.MainClient;
import net.argus.emessage.client.event.ChatEvent;
import net.argus.emessage.client.event.ChatListener;
import net.argus.emessage.client.gui.GUIClient;
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
	private Label status, statusText, endText;
	private Panel panGIF, panStatus;
	
	private boolean error;
	
	public SplashConnect(String title) {
		super();
		
		addFocusListener(getFocusListener());
		
		setIcon(ClientResources.ICON.getImage());
		setDialogIcon(ClientResources.ICON.getImage());
		
		getDialog().setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setAlwaysOnTop(true);
		setResizable(false);
		setTitle(title);
		
		setSize(400, 150);
		setLocationRelativeTo(GUIClient.FRAME);
	}

	@Override
	public Panel getComponent() {
		Panel main = new Panel();
		main.setLayout(new BorderLayout());
		
		/*GIF*/
		panGIF = getGIFPanel();
		
		/*STATUS*/
		count = new CountDown(5);
		count.addCountDownListener(getCountDownListener());
		
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
		
		gif = new GIFComponent(ClientResources.LOAD_GIF);
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

		
		status = new Label(UIManager.getString("Text.status") + ": ", false);
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
				gif.setGIF(error?ClientResources.INVALID_GIF:ClientResources.VALID_GIF);
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
					getDialog().setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					statusText.setText(Lang.get("info." + e.getMessage() + ".name"));
										
					SwingUtilities.invokeLater(() -> {
						int w = status.getWidth() + statusText.getWidth() + panGIF.getWidth() + 40;
						if(w > getDialog().getWidth())
							setSize(w, getDialog().getHeight());
						
					});
					
					count.start();
					gif.stop();
					
					GUIClient.FRAME.setEnabled(true);
				}
			}
		};
	}
	
	private FocusListener getFocusListener() {
		return new FocusListener() {
			public void focusLost(FocusEvent e) {
				if(GUIClient.FRAME.isEnabled())
					setVisible(false);
			}
			public void focusGained(FocusEvent e) {}
		};
	}
	
	@Override
	public void show() {
		GUIClient.FRAME.setEnabled(false);
		super.show();
	}
	
	@Override
	public void hide() {
		GUIClient.FRAME.setEnabled(true);
		super.hide();
	}

}
