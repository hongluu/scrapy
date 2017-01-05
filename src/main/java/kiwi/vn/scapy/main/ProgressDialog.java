package kiwi.vn.scapy.main;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.Timer;

public class ProgressDialog extends JDialog{
	private JLabel progresLabel;
	private JButton closeButton;
	private int flag = 3;
	private Timer timer;
	
	public ProgressDialog(){
		super();
		setTitle("商品情報収集ツール");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(400, 120);
		setLayout(null);
		progresLabel = new JLabel("商品情報収集中");
		progresLabel.setSize(new Dimension(380, 30));
		progresLabel.setLocation(10, 10);
		timer = new Timer(1000, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String ext = "";
				flag ++;
				if (flag > 6) flag = 3;
				switch (flag) {
				case 3:
					ext = "...";
					break;
				case 4:
					ext = "....";
					break;
				case 5:
					ext = ".....";
					break;
				case 6:
					ext = "......";
					break;
				}
				progresLabel.setText("商品情報収集中" + ext);
			}
		});
		timer.start();
		closeButton = new JButton("閉じる");
		closeButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		closeButton.setSize(new Dimension(60, 20));
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.setLocation(300, 40);
		add(progresLabel);
		add(closeButton);
	}
	
	public void stop(String filePath){
		timer.stop();
		progresLabel.setText("CSVファイル： " + filePath);
	}
}
