public class TrackResult {
    private String name;
    private String id;
    public TrackResult(String name, String id) {
        this.name = name;
        this.id = id;
    }
    @Override
    public String toString() {
        return this.name;
    }
    public String getName() {return this.name;}
    public String getId() {return this.id;}
}
