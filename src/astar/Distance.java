package astar;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class Distance implements Comparable<Distance> {

	public final Point p;
	public double g, f;
	public final double h;
	public final Point goal;
	public Distance parent;
	
	private final List<Point> neighbourPoints;

	public Distance (Point p, Point goal, Distance parent) {
		this.p = p;
		this.parent = parent;
		this.goal = goal;
		h = p.distance(goal);
		g = parent == null ? 0 : p.distance(parent.p) + parent.g;
		f = g + h;
		neighbourPoints = new LinkedList<Point>();
		for(int x = p.x-1 ; x <= p.x+1 ; x++) {
			for(int y = p.y-1 ; y <= p.y+1 ; y++) {
				neighbourPoints.add(new Point(x,y));
			}
		}
		neighbourPoints.remove(p);
		if(parent != null) {
			neighbourPoints.remove(parent.p);
		}
	}
	
	public List<Distance> getNeighbours(BufferedImage maze) {
		List<Distance> neighbours = new LinkedList<Distance>();
		for(Point np : neighbourPoints) {
			if(maze.getRGB(np.x, np.y) == 0xFF000000) {
				neighbours.add(new Distance(np, goal, parent));
			}
		}
		return neighbours;
	}

	@Override
	public int hashCode() {
		return p.hashCode();
	}

	@Override
	public boolean equals(Object that) {
		if(that instanceof Distance) {
			return this.p.equals(((Distance)that).p);
		}
		return false;
	}

	@Override
	public int compareTo(Distance that) {
		if(this.f < that.f) {
			return 1;
		}
		if(this.f > that.f) {
			return -1;
		}
		return 0;
	}
	
	
	
}
