import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Created by Ole on 25/11/2014.
 */
public class PixelArt extends Canvas implements Runnable {

	private static final int WIDTH = 720;
	private static final int HEIGHT = WIDTH;
	private static final Dimension d = new Dimension(WIDTH, HEIGHT);

	private JFrame frame;
	private boolean running = true;
	private BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

	public static void main(String[] args) {
		PixelArt pa = new PixelArt();
		pa.start();
	}

	private void start() {
		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);

		frame = new JFrame("Pixel Art");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);

		new Thread(this, "main").start();
	}

	public void run() {
		while (running) {
//			Statement stmt = new Statement(inputString("skriv en funksjon med x og y"));
//			int xtest = inputInt("input a number, x");
//			int ytest = inputInt("input a number, y");
//			double i = stmt.parseStatement(xtest, ytest, WIDTH, HEIGHT);
//			output("At x = " + xtest+  " and y = " + ytest + ":   f(x, y) = " + stmt.getStatement() + " = " + i, "test");

			Statement stmtr;
			do {
				stmtr = new Statement(inputString("Write a function for red color with variables: x, y, w, h"));
			} while (!Statement.isValidStmt(stmtr.getStatement()));

			Statement stmtg;
			do {
				stmtg = new Statement(inputString("Write a function for green color with variables: x, y, w, h"));
			} while (!Statement.isValidStmt(stmtg.getStatement()));

			Statement stmtb;
			do {
				stmtb = new Statement(inputString("Write a function for blue color with variables: x, y, w, h"));
			} while (!Statement.isValidStmt(stmtb.getStatement()));

			int r, g, b;

			for (int y = 0; y < HEIGHT; y++) {
				for (int x = 0; x < WIDTH; x++) {
//					double xy1 = (x * y) / ((double) WIDTH * (double) HEIGHT);
//					double xy2 = ((WIDTH - x) * y) / ((double) WIDTH * (double) HEIGHT);
//					double xy3 = (x * (HEIGHT - y)) / ((double) WIDTH * (double) HEIGHT);
//					double cool = ((WIDTH - x) * (HEIGHT - y)) / ((double) WIDTH * (double) HEIGHT);
//					r = (int) ((xy1 * 255) - cool * 255);
//					g = (int) ((xy2 * 255) - cool * 255);
//					b = (int) ((xy3 * 255) - cool * 255);

					r = (int) stmtr.parseStatement(x, y, WIDTH, HEIGHT);
					b = (int) stmtb.parseStatement(x, y, WIDTH, HEIGHT);
					g = (int) stmtg.parseStatement(x, y, WIDTH, HEIGHT);

					pixels[x + y * WIDTH] = (r << 16) + (g << 8) + b;
				}
			}

			render();

			output("red   = " + stmtr.getStatement() +
				   "\ngreen = " + stmtg.getStatement() +
				   "\nblue  = " + stmtb.getStatement(),
				   "Color Functions");
		}
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			bs = getBufferStrategy();
		}

		Graphics g = bs.getDrawGraphics();

		g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);

		g.dispose();
		bs.show();
	}

	public static int inputInt(String msg, int min, int max) {
		boolean done = false;
		int faults = 0;

		int input = 0;
		while (!done) {
			try {
				String in = inputString(msg);
				input = Integer.parseInt(in);
				done = true;
				if (input < min || input > max) {
					output("Tallet må være mellom " + min + "-" + max, "Feil");
					done = false;
				}
			} catch (Exception e) {
				if (faults < 2) {
					faults++;
					output("Du må skrive inn et tall. ;)", "feil");
				} else {
					output("DU MÅ SKRIVE INN ET TALL!!!!! >:(", "feil");
				}
			}
		}
		return input;
	}

	public static int inputInt(String msg) {
		boolean done = false;
		int faults = 0;

		int input = 0;
		while (!done) {
			try {
				String in = inputString(msg);
				input = Integer.parseInt(in);
				done = true;
			} catch (Exception e) {
				if (faults < 2) {
					faults++;
					output("Du må skrive inn et tall. ;)", "feil");
				} else {
					output("DU MÅ SKRIVE INN ET TALL!!!!! >:(", "feil");
				}
			}
		}
		return input;
	}

	public static int inputYN(String msg, String title) {
		return JOptionPane.showConfirmDialog(null,
				msg != null
						? msg
						: "",
				title != null
						? title
						: "",
				JOptionPane.YES_NO_OPTION);
	}

	public static String inputString(String msg) {
		boolean done = false;

		String input = "";

		while (!done) {
			input = JOptionPane.showInputDialog(msg);
			if (input != null) {
				done = true;
			} else {
				if (inputYN("Er du sikker på at du vil avslutte?", "Avslutte?") == 0) System.exit(0);
				output("Bra du vil bli. :)", ":D");
			}
		}
		return input;
	}

	public static void output(String msg, String title) {
		if (msg != null && title != null)
			JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}

}
