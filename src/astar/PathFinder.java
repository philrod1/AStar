package astar;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import view.MazePanel;

public class PathFinder {
	
	private static MazePanel view;

	public static List<Point> getPath (String filename) {
		final Image temp = new ImageIcon(filename).getImage();
		final BufferedImage maze = new BufferedImage(temp.getWidth(null), temp.getHeight(null), BufferedImage.TYPE_INT_RGB);
		maze.getGraphics().drawImage(temp, 0, 0, null);
		final Point[] points = getPoints(maze);
		final Point start = points[0];
		final Point goal = points[1];
		view = new MazePanel(filename);
		JFrame frame = new JFrame(filename);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(view);
		frame.setVisible(true);
		frame.pack();
		return findPath(maze);
	}

	private static List<Point> findPath(BufferedImage maze) {
		final Point[] points = getPoints(maze);
		final Point start = points[0];
		final Point goal = points[1];
		final Map<Point, Double> gMap = new HashMap<Point, Double>();
		final Map<Point, Double> hMap = new HashMap<Point, Double>();
		final TreeMap<Double, Point> open = new TreeMap<Double, Point>();
		final Map<Point, Point> prev = new HashMap<Point, Point>();
		
		final Set<Point> closed = new HashSet<Point>();
		 
		gMap.put(start, 0.0);
		hMap.put(start, start.distance(goal));
		open.put(hMap.get(start), start);
		
		while(!open.isEmpty()) {
			Entry<Double, Point> first = open.firstEntry();
			open.remove(first.getKey());
			Point current = first.getValue();
			view.colourPixel(current.x, current.y, Color.GRAY);
			System.out.println(current);
			if(current.equals(goal)) {
				System.out.println("WOOT!");
				return null;
			}
			
			for (Point p : getNeighbours(current, maze, prev.get(current))) {
				double newG = p.distance(current) + gMap.get(current);
				if (!open.containsValue(p) || newG < gMap.get(p)) {
					prev.put(p,  current);
					gMap.put(p, newG);
					if(!open.containsValue(p)) {
						open.put(newG + p.distance(goal), p);
						view.colourPixel(p.x, p.y, Color.ORANGE);
					}
				}
			}
		}
		System.out.println("Shit!");
		return null;
	}

	private static List<Point> getNeighbours(Point current, BufferedImage maze, Point from) {
		List<Point> points = new LinkedList<Point>();
		for(int x = -1 ; x <= 1 ; x++) {
			for(int y = -1 ; y <= 1 ; y++) {
				if(maze.getRGB(current.x + x, current.y + y) == 0xFF000000 
						|| maze.getRGB(current.x + x, current.y + y) == 0xFFFF0000) {
					points.add(new Point(current.x + x, current.y + y));
				}
			}
		}
		points.remove(from);
		points.remove(current);
		return points;
	}

	private static Point[] getPoints(BufferedImage maze) {
		boolean foundStart = false, foundGoal = false;
		final Point[] points = new Point[2];
		for(int x = 0 ; x < maze.getWidth() ; x++) {
			for(int y = 0 ; y < maze.getHeight() ; y++) {
				if(!foundStart && maze.getRGB(x, y) == 0xFFFF0000) {
					points[0] = new Point(x,y);
					foundStart = true;
				} else if(!foundGoal && maze.getRGB(x, y) == 0xFF00FF00) {
					points[1] = new Point(x,y);
					foundGoal = true;
				}
				if(foundStart && foundGoal) {
					return points;
				}
			}
		}
		return null;
	}

}
