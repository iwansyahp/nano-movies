package xyz.android.amrro.popularmovies.data.model;


public final class Trailer {

    private final String id;
    private final String key;
    private final String name;
    private final Integer size;

    public Trailer(String id, String key, String name, Integer size) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public Integer getSize() {
        return size;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trailer)) return false;

        Trailer trailer = (Trailer) o;

        return (getId() != null ? getId().equals(trailer.getId()) : trailer.getId() == null) &&
                (getKey() != null ? getKey().equals(trailer.getKey()) : trailer.getKey() == null) &&
                (getName() != null ? getName().equals(trailer.getName()) : trailer.getName() == null) &&
                (getSize() != null ? getSize().equals(trailer.getSize()) : trailer.getSize() == null);
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getKey() != null ? getKey().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getSize() != null ? getSize().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}