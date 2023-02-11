package org.example.model;

public class Cat {
    private long id;
    private String name;
    private int Weight;
    private boolean isAngry;

    @Override
    public String toString() {
        return "Cat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Weight=" + Weight +
                ", isAngry=" + isAngry +
                '}';
    }

    protected Cat() {
    }

    public Cat(long id, String name, int weight, boolean isAngry) {
        this.id = id;
        this.name = name;
        this.Weight = weight;
        this.isAngry = isAngry;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public boolean isAngry() {
        return isAngry;
    }

    public void setAngry(boolean angry) {
        isAngry = angry;
    }


}
