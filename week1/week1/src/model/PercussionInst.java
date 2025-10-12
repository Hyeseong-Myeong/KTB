package model;

public class PercussionInst extends Instrument {

    private final String material;

    private final String stickMaterial;

    public PercussionInst(String name, int price, String manufacturer, String material, String stickMaterial) {

        super(name, price, manufacturer);
        this.material = material;
        this.stickMaterial = stickMaterial;
    }

    public String getMaterial() {

        return material;
    }

    public String getStickMaterial() {

        return stickMaterial;
    }

    @Override
    public void getInfo() {

        super.getInfo();
        System.out.println("몸체 재질: " + this.material);
        System.out.println("채 재질: " + this.stickMaterial);
    }
}
