package controller;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import model.Model;
import model.Node;
import view.MazePanel;

public class Controller {

	private static final double H_F_RATIO = 0.0;
	
	public static void main(String[] args) {
		String filename = "res/maze4.png";
		Model model = new Model(filename);
//		Point start = model.getStart();
//		Point goal = model.getGoal();
//		List<Point> waypoints = model.getWaypoints();
		MazePanel view = new MazePanel(filename);
		JFrame frame = new JFrame(filename);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(view);
		frame.setVisible(true);
		frame.pack();
		
		LinkedHashSet<Node> open = new LinkedHashSet<>();
		Set<Node> closed = new HashSet<Node>();
		Node current = model.getStartNode();
		long start = System.currentTimeMillis();
		Map<Point, Node> map = new HashMap<Point, Node>();
		map.put(current.pos, current);
		open.add(current);
		boolean found = false;
		long iterations = 0;
		
		while(!open.isEmpty() && !found) {  // If open becomes empty, we have failed to find the goal.
			iterations++;
			// Find the node with the smallest value
			double bestValue = Double.POSITIVE_INFINITY;
			for(Node node : open) {
				double val = node.getH() * H_F_RATIO + node.getF() * (1-H_F_RATIO);
				if (val < bestValue) {
					bestValue = val;
					current = node;
				}
			}
//			System.out.println(current);
			closed.add(current);
//			if(model.isGoal(current)){
//				System.out.println("Found it!");
//				int count = 0;
//				List<Point> path = new ArrayList<Point>();
//				path.add(current.pos);
//				while(current.getParent() != null) {
////					view.colourPixel(current.getX(), current.getY(), Color.YELLOW);
//					current = current.getParent();
//					path.add(current.pos);
//					count++;
//				}
//				simplifyAndDraw(path,view,model);
//				System.out.println("Path is " + count + " pixels.  This took " + (System.currentTimeMillis() - start)/1000.0 + " seconds to find.");
//				found = true;
//				break;
//			}
			
			for(Node neighbour : model.getNeighbours(current)) {
				if (model.isGoal(neighbour)) {
					System.out.println("Found it!");
					int count = 0;
					List<Point> path = new ArrayList<Point>();
					path.add(current.pos);
					while(current.getParent() != null) {
						current = current.getParent();
						path.add(current.pos);
						count++;
					}
					simplifyAndDraw(path,view,model);
					System.out.println("Path is " + count + " pixels.  This took " + (System.currentTimeMillis() - start)/1000.0 + " seconds to find.");
					found = true;
					break;
				}
				if(map.containsKey(neighbour.pos)) {
					Node dup = map.get(neighbour.pos);
					if(neighbour.getG() < dup.getG()) {
						dup.setG(neighbour.getG());
						dup.parent = neighbour.parent;
					}
				} else if(!open.contains(neighbour) && !closed.contains(neighbour)) {
					open.add(neighbour);
					map.put(neighbour.pos, neighbour);
					view.colourPixel(neighbour.getX(), neighbour.getY(), Color.GRAY);
				}
			}
			
			open.remove(current);
			view.colourPixel(current.getX(), current.getY(), Color.DARK_GRAY);
			if (iterations % 5 == 0) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	private static void simplifyAndDraw(List<Point> path, MazePanel view, Model model) {
		List<Point> simplePath = new LinkedList<Point>();
		Point current = path.remove(0);
		Point goal = path.get(path.size()-1);
		simplePath.add(current);
		while(current != goal) {
			current = lastVisible(current, path, model);
			simplePath.add(current);
		}
		view.setPath(simplePath);
	}

	private static Point lastVisible(Point current, List<Point> path, Model model) {
		Point last = path.remove(0);
		while(!path.isEmpty()) {
			Point next = path.remove(0);
			if(!isVisible(current, next, model)) {
				return next;
			}
			last = next;
		}
		return last;
	}

	private static boolean isVisible(Point current, Point next, Model model) {
		List<Point> line = getLine(current, next);
		for (Point p : line) {
//		    System.out.println("(" + p.x + "," + p.y + ") = " + Integer.toHexString(model.getRGB(p.x, p.y)));
		    if(model.getRGB(p.x, p.y) == 0xFFFFFFFF) {
		    	return false;
		    }
		}
		return true;
	}

	public static List<Point> getLine(Point p1, Point p2) {
		List<Point> path = new LinkedList<Point>();
		path.add(p1);
		int x = p1.x;
		int y = p1.y;
	    int w = p2.x - x ;
	    int h = p2.y - y ;
	    int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
	    if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
	    if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
	    if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
	    int longest = Math.abs(w) ;
	    int shortest = Math.abs(h) ;
	    if (!(longest>shortest)) {
	        longest = Math.abs(h) ;
	        shortest = Math.abs(w) ;
	        if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
	        dx2 = 0 ;            
	    }
	    int numerator = longest >> 1 ;
	    for (int i=0;i<=longest;i++) {
	        path.add(new Point(x,y));
	        numerator += shortest ;
	        if (!(numerator<longest)) {
	            numerator -= longest ;
	            x += dx1 ;
	            y += dy1 ;
	        } else {
	            x += dx2 ;
	            y += dy2 ;
	        }
	    }
	    return path;
	}

}
