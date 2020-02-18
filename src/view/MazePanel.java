package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MazePanel extends JPanel {
	private static final long serialVersionUID = -7582476271051605776L;
	private final Image maze;
	private final BufferedImage overlay;
	private List<Point> path = null;
	
	public MazePanel (String filename) {
		maze = new ImageIcon(filename).getImage();
		overlay = new BufferedImage(maze.getWidth(null), maze.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		setPreferredSize(new Dimension(maze.getWidth(null), maze.getHeight(null)));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(path != null) {
			Graphics2D g2d = overlay.createGraphics();
			g2d.setColor(new Color(0xFFFFFF00));
			for(int i= 0 ; i < path.size()-1 ; i++) {
				g2d.drawLine(path.get(i).x, path.get(i).y, path.get(i+1).x, path.get(i+1).y);
			}
		}
		g.drawImage(maze, 0, 0, getWidth(), getHeight(), null);
		g.drawImage(overlay, 0, 0, getWidth(), getHeight(), null);
	}
	
	public void colourPixel(int x, int y, Color c) {
		overlay.setRGB(x, y, c.getRGB());
		repaint();
	}

	public void setPath(List<Point> simplePath) {
		path = simplePath;
		repaint();
	}
}