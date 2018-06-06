
public class Tile {
	private TileType type;
	private boolean walkable;
	private int relatedRow;
	private int relatedCol;
	private boolean hasKey;
	
	public Tile(TileType type){
		this.type = type;
		if (this.type == TileType.WALL) {
			this.walkable = false;
		}
		else if (this.type == TileType.DOOR) {
			this.hasKey = true;
			this.walkable = false;
		}
		else if (this.type == TileType.LOCKED_DOOR) {
			this.hasKey = false;
			this.walkable = false;
		}
		else {
			this.walkable = true;
		}
	}
	
	public Tile(TileType type, int relatedRow, int relatedCol){ //key constructor
		this.type = type;
		this.walkable = walkable;
		this.walkable = true;
		this.relatedRow = relatedRow;
		this.relatedCol = relatedCol;
	}
	
	public boolean isWalkable() {
		return this.walkable;
	}
	
	public void open(){
		this.walkable = true;
	}
	
	public void close(){
		this.walkable = false;
	}
	
	public TileType getType() {
		return this.type;
	}
	
	public boolean unlock() {
		if (this.type == TileType.LOCKED_DOOR && this.hasKey) {
			this.type = TileType.DOOR;
			return true;
		}
		else {
			return false;
		}
	}
	
	public int relatedRow() {
		return this.relatedRow;
	}
	
	public int relatedCol() {
		return this.relatedCol;
	}
	
	public void getKey() {
		if (!(this.type == TileType.LOCKED_DOOR)) {
			System.out.println("This isn't a locked door!");
		}
		this.hasKey = true;
	}
	
}
