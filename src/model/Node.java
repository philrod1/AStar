package model;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;


public class Node {
	
	public final Point pos;
	private final List<Point> neighbours;
	public Node parent;
	private double h, g, f;
	
	public Node (Point pos, Node parent) {
		this.pos = pos;
		this.parent = parent;
		neighbours = new LinkedList<Point>();
		for(int x = pos.x-1 ; x <= pos.x+1 ; x++) {
			for(int y = pos.y-1 ; y <= pos.y+1 ; y++) {
				neighbours.add(new Point(x,y));
			}
		}
		neighbours.remove(pos);
		if(parent != null)
			neighbours.remove(parent.pos);
	}
	
	public int getX() {
		return pos.x;
	}
	
	public int getY() {
		return pos.y;
	}
	
	public double getG() {
		return g;
	}
	
	public double getH() {
		return h;
	}
	
	public void setH(double h) {
		this.h = h;
		f = g + h;
	}
	
	public double getF() {
		return f;
	}
	
	public void setG(double g) {
		this.g = g;
		f = g + h;
	}
	
	protected List<Point> getNeighbours() {
		return neighbours;
	}

	public Node getParent() {
		return parent;
	}

	// The following methods are for avoiding duplicates
	// in the open and closed sets
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return "Node (" + pos.x + "," + pos.y + ") g = " + g + ", h = " + h + ", f = " + f;
	}
	
	
}
