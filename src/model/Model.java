package model;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

public class Model {
	
	private final BufferedImage maze;
	private final int width, height;
	private final Point start, goal;
	private final List<Point> waypoints;
	
	public Model (String filename) {
		Image img = new ImageIcon(filename).getImage();
		width = img.getWidth(null);
		height = img.getHeight(null);
		maze = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		maze.getGraphics().drawImage(img, 0, 0, null);
		start = getStart();
		goal = getGoal();
		waypoints = getWaypoints();
		System.out.println(waypoints);
	}
	
	public boolean isGoal(Node n) {
		for(Point w : waypoints) {
			if (w.equals(n.pos)) {
				waypoints.remove(w);
				return false;
			}
		}
		return n.pos.equals(goal);
	}
	
	public Node getStartNode() {
		Node n = new Node(start, null);
		n.setG(0);
		double h = start.distance(goal);
//		for(Point w : waypoints) {
//			double d = n.pos.distance(w);
//			if(d < h)
//				h = d;
//		}
		n.setH(h);
		return n;
	}
	
	public List<Node> getNeighbours(Node node) {
		List<Node> neighbours = new LinkedList<Node>();
		for(Point p : node.getNeighbours()) {
			if (maze.getRGB(p.x, p.y) != 0xFFFFFFFF) {
				Node n = new Node(p, node);
				n.setG(n.parent == null ? 0 : n.pos.distance(n.parent.pos) + n.parent.getG());
//				if(waypoints.isEmpty() || true) {
					n.setH(n.pos.distance(goal));
//				} else {
//					double h = Double.POSITIVE_INFINITY;
//					for(Point w : waypoints) {
//						double d = n.pos.distance(w);
//						if(d < h)
//							h = d;
//					}
//					n.setH(h);
//				}
				neighbours.add(n);
//				System.out.println(n);
			}
		}
		return neighbours;
	}

	private Point getStart() {
		for(int x = 0 ; x < maze.getWidth() ; x++) {
			for(int y = 0 ; y < maze.getHeight() ; y++) {
				if(maze.getRGB(x, y) == 0xFFFF0000) {
					return new Point(x,y);
				}
			}
		}
		return null;
	}
	
	private Point getGoal() {
		for(int x = 0 ; x < maze.getWidth() ; x++) {
			for(int y = 0 ; y < maze.getHeight() ; y++) {
				if(maze.getRGB(x, y) == 0xFF00FF00) {
					return new Point(x,y);
				}
			}
		}
		return null;
	}
	
	private List<Point> getWaypoints() {
		List<Point> waypoints = new LinkedList<Point>();
		for(int x = 0 ; x < maze.getWidth() ; x++) {
			for(int y = 0 ; y < maze.getHeight() ; y++) {
				if(maze.getRGB(x, y) == 0xFF0000FF) {
					waypoints.add(new Point(x,y));
					System.out.println("waypoint at " + x + ", " + y);
				}
			}
		}
		return waypoints;
	}

	public int getRGB(int x, int y) {
		return maze.getRGB(x, y);
	}
}
