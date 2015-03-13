package app.ctrlyati.ywc.stroke;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import app.ctrlyati.ywc.stroke.controller.CtrlyatiHandler;
import app.ctrlyati.ywc.stroke.model.YwcUser;
import app.ctrlyati.ywc.stroke.model.YwcUser.GroupColor;

import com.leapmotion.leap.*;

public class Game{
	

	private static final boolean IS_TEST = false;
	private static final boolean IS_ALWAYS_ON_TOP = false;
	private static final boolean IS_RESIZABLE = false;
	
	private long mStartTime = 0;
	private long mTime = 0;
	
	private Vector mLastPosition;
	
	private String debugText = "";
	
	private Controller mController = new Controller();
	private Listener mListener = new Listener(){
		
		public void onConnect(Controller controller) {
			
		};
		
		public void onDisconnect(Controller controller) {
			
		};
		
		public void onFrame(Controller controller){
			if(controller.devices().count() > 0){
				debugText = ""+controller.frame().hands().count();
			}
				
			long time = controller.frame().timestamp();
			long deltaTime = time - mTime;
			mTime = time;
			
			update(controller, deltaTime/1000d);
		}
		
	};
	
	
	private JFrame mFrame;
	private JPanel 	mLoginPanel,
					mGamePanel,
					mEndPanel,
					mLoadingPanel;
	
	private JLabel	mTestText,
					mScoreText,
					mTimeText;
	
	//Handler
	private CtrlyatiHandler mHandler = new CtrlyatiHandler();
	
	//LoadingPanel
	private JTextPane mLoadingText;
	
	//LoginPanel
	private JTextField mIdField;
	
	//User Information
	private YwcUser mUser;
	
	private State mState = State.NONE;
	
	public static enum State{
		NONE, PREPARING, READY, PLAYING, END
	}
	
	private Dimension mScreenSize; 
	
	public Game(){
		
		mController.setPolicy(Controller.PolicyFlag.POLICY_BACKGROUND_FRAMES);
		
		mScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		mFrame = new JFrame();
		
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mFrame.setAlwaysOnTop(IS_ALWAYS_ON_TOP);
		mFrame.setSize(mScreenSize);
//		mFrame.setSize(300, 300);
		mFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		mFrame.setResizable(IS_RESIZABLE);
		
		mTestText = new JLabel();
		mTestText.setSize(mScreenSize.width, 60);
		mTestText.setCursor(Cursor.getDefaultCursor());
		mTestText.setBackground(Color.green);
		
		if(IS_TEST){	
			mFrame.add(mTestText, BorderLayout.SOUTH);
		}
		
	}
	
	public void prepare(){
		
		mLoadingPanel = new JPanel(new BorderLayout());
		
		mLoadingText = new JTextPane();
		mLoadingText.setText("Loading, Please Wait");
		mLoadingPanel.add(mLoadingText);
		
		mLoginPanel = new JPanel(new BorderLayout());
		
		mIdField = new JTextField(10);
		mIdField.setToolTipText("The YWC Reunion #6 User's ID");
		mIdField.setText("R00000000");
		
		mIdField.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				mIdField.setText("");
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {}
		});
		mIdField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mFrame.remove(mLoginPanel);
				check(mIdField.getText());
			}
		});
		
		mLoginPanel.add(mIdField, BorderLayout.CENTER);
		mFrame.add(mLoginPanel, BorderLayout.CENTER);
		
		mFrame.setVisible(true);
		mState = State.READY;
		
		
	}
	
	
	public void check(String user_id){
		//Check from the Internet
		YwcUser user = new YwcUser();
		user.setId(mIdField.getText());
		user.setName("Yati");
		user.setSurname("Dumrongsukit");
		user.setColor(GroupColor.ORANGE);
		start(user);
	}
	
	private SimpleAttributeSet mTextCenterAttrs;
	public void start(YwcUser user){
		mUser = user;
		
		mStartTime = new Date().getTime();
		
		Font font = new Font("Impact", Font.BOLD , 300);
		Font smallFont = new Font("Impact", Font.PLAIN, 30);
		
		JLabel id = new JLabel();
		id.setHorizontalAlignment(JLabel.CENTER);
		id.setFont(smallFont);
		id.setForeground(new Color(0x33ffffff));
		id.setText(user.getId());
		
		mTimeText = new JLabel();
		mTimeText.setHorizontalAlignment(JLabel.CENTER);
		mTimeText.setFont(smallFont);
		mTimeText.setForeground(new Color(0x33ffffff));
		
		mScoreText = new JLabel();
//		mScoreText.setBackground(new Color(0xff000000));
		mScoreText.setForeground(new Color(0xffffffff));
		mScoreText.setSize(500, 300);
		mScoreText.setFont(font);
		mScoreText.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		mScoreText.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		
		JPanel scorePanel = new JPanel(new GridBagLayout());
		scorePanel.add(mScoreText);
		
		mGamePanel = new JPanel(new BorderLayout());
		
		mGamePanel.add(scorePanel, BorderLayout.CENTER);
		mGamePanel.add(mTimeText, BorderLayout.NORTH);
		mGamePanel.add(id, BorderLayout.SOUTH);
		
		
		mFrame.add(mGamePanel, BorderLayout.CENTER);
		
		mFrame.setTitle("Playing as "+mUser.getName()+" "+mUser.getSurname());
		if(mUser.getColor()==GroupColor.ORANGE){
			mFrame.setBackground(new Color(0xffff9900));
			mScoreText.setBackground(new Color(0xffff9900));
		}

		mController.addListener(mListener);
		
		
	}
	
	private void update(Controller controller, double dt){
		if(controller.frame().hands().count()>0){
			Vector v = controller.frame().hands().get(0).palmPosition();
			debugText = ""+v.getZ();
			
			
			if(mLastPosition==null){
				mLastPosition = v;
			}
			
			double delta = v.distanceTo(mLastPosition);			
			mLastPosition = v;
			
			double newScore = mUser.getScore()+delta;
			
			mUser.setScore(newScore);
		}else{
			debugText = "0.0";
		}
		mTestText.setText(debugText);
		
		long currentTime = new Date().getTime();
		long remainTime = mStartTime+10*1000 - currentTime;
		
		if(remainTime > 0){
			mTimeText.setText("Time "+ (remainTime/1000/60) + ":"+ (remainTime/1000)%60);
			mScoreText.setText(""+Math.round(mUser.getScore()/100));
		}
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

}
