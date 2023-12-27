import java.awt.*;
import java.awt.event.*;
import java.lang.Runnable;
import javax.swing.*;

public class SimpleCanvas implements ISimpleCanvas {
	static int CanvasCount = 0;

	private class CanvasCreator implements Runnable {
		private SimpleCanvas theCanvas;

		private CanvasCreator(SimpleCanvas c) {
			super();
			theCanvas = c;
		}

		public void run() {
			JFrame f = new JFrame("Postcode Map");
			if (CanvasCount == 0)
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			CanvasCount++;
			f.add(new MyPanel(theCanvas));
			f.pack();
			f.setVisible(true);
		}
	}

	private Color pixels[][] = new Color[1000][1000];
	private int lastMouseClick[] = new int[2];
	private boolean mouseClickPending = false;
	protected int xsize, ysize;

	public SimpleCanvas() {
		this(1000, 1000);
	}

	public SimpleCanvas(int xsize, int ysize) {
		this.xsize = xsize;
		this.ysize = ysize;
		for (int i = 0; i < xsize; i++) {
			for (int j = 0; j < ysize; j++) {
				pixels[i][j] = Color.white;
			}
		}
		SwingUtilities.invokeLater(new CanvasCreator(this));
	}

	public boolean isPointBlack(int x, int y) {
		return Color.black == getPoint(x, y);
	}

	public boolean isPointWhite(int x, int y) {
		return Color.white == getPoint(x, y);
	}

	public Color getPoint(int x, int y) {
		return pixels[x][y];
	}

	public void setPointBlack(int x, int y) {
		setPointColour(x, y, Color.black);
	}

	public void setPointWhite(int x, int y) {
		setPointColour(x, y, Color.white);
	}

	public void setPointColour(int x, int y, Color c) {
		pixels[x][y] = c;
	}

	public synchronized int[] getMouseClick() {
		while (!mouseClickPending) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		mouseClickPending = false;
		return lastMouseClick;
	}

	public synchronized void setMouseClick(int x, int y) {
		lastMouseClick[0] = x;
		lastMouseClick[1] = y;
		mouseClickPending = true;
		notifyAll();
	}

	class MyPanel extends JPanel {
		private SimpleCanvas theCanvas;

		public MyPanel(SimpleCanvas c) {
			theCanvas = c;
			addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					theCanvas.setMouseClick(e.getX(), theCanvas.ysize - 1 - e.getY());
				}
			});
		}

		public Dimension getPreferredSize() {
			return new Dimension(theCanvas.xsize, theCanvas.ysize);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			// Draw Text
			for (int i = 0; i < theCanvas.xsize; i++) {
				for (int j = 0; j < theCanvas.ysize; j++) {
					g.setColor(pixels[i][j]);
					g.drawRect(i, theCanvas.ysize - 1 - j, 1, 1);
				}
			}
		}
	}

}
