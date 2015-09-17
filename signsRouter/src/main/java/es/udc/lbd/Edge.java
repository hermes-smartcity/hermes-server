package es.udc.lbd;

public class Edge {
	public Long id;
	public Node origin;
	public Node dest;
	public Position posOrigin;
	public Position posDest;
	public boolean banned;
	
	public Edge(Long id, Node origin, Node dest, Position posOrigin, Position posDest) {
		super();
		this.id = id;
		this.origin = origin;
		this.dest = dest;
		this.posOrigin = posOrigin;
		this.posDest = posDest;
		this.banned = false;
	}
	
	public Edge reverse() {
		return new Edge(-id, dest, origin, posDest, posOrigin);
	}

	@Override
	public String toString() {
		return "Edge [id=" + id + ", origin=" + origin + ", dest=" + dest + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dest == null) ? 0 : dest.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
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
		Edge other = (Edge) obj;
		if (dest == null) {
			if (other.dest != null)
				return false;
		} else if (!dest.equals(other.dest))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		return true;
	}
	
	
}
