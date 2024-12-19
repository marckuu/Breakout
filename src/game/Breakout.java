package game;

import javax.swing.JFrame;

public class Breakout extends JFrame {

	public Breakout(Board board) {
		initUI(board);
	}

	private void initUI(Board board) {
		add(board);
		setTitle("Breakout");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		pack();
	}
}
